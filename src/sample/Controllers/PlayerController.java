package sample.Controllers;

import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.Constantts.Images;
import sample.DB.DBConnector;
import sample.ListView.AlbumListView;
import sample.ListView.ArtistListView;
import sample.ListView.SongListView;
import sample.ListView.SongReviewsListView;
import sample.Models.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.function.Predicate;
import static sample.Extensions.TimeExtensions.ConvertSecondsToStringTime;
import static sample.Extensions.TimeExtensions.ConvertToMillis;

public class PlayerController implements Initializable {

    @FXML private Pane songPane;
    @FXML private Label songNameLabel, actualTimeLabel, songLengthLabel, showUserName, lyricsText;
    @FXML private ImageView playPause;
    @FXML private ImageView star1, star2, star3, star4, star5;
    @FXML private JFXSlider timeSlider, volumeSlider;
    @FXML private JFXTextField searchField;
    @FXML private JFXTextArea reviewText;
    @FXML private ListView<Song> songsListView;
    @FXML private ListView<Album> albumsListView;
    @FXML private ListView<Artist> artistListView;
    @FXML private ListView<Review> songReviewsListView;

    private MediaPlayer mediaPlayer;
    private Media media;
    private ObservableList<Song> songs;
    private ObservableList<Album> albums;
    private Map<Integer, String> lyrics = null;
    private User user;
    private Song actualSong;
    private boolean playing;
    private int songRating;

    /**
     * @summary
     * Stop song and move to start*/
    public void stop(){
        if (mediaPlayer == null) return;
        mediaPlayer.stop();
        mediaPlayer.seek(Duration.ZERO);
        timeSlider.setValue(0);
        playing = false;
        playPause.setImage(Images.play);
        lyricsText.setText("");
    }

    /**
     * @summary
     * Play and pause actual song
     * */
    public void playAndPause(){
        if(mediaPlayer == null) return;

        // update UI every second
        mediaPlayer.currentTimeProperty().addListener((Observable ov) -> {
            updateValues();
        });

        if(playing){
            mediaPlayer.pause();
            playPause.setImage(Images.play);
        } else {
            mediaPlayer.play();
            playPause.setImage(Images.pause);
        }
        playing = !playing;
        startLyrics();
    }

    /**
     * @summary
     * Logout user
     * */
    public void backToLogin() throws IOException {
        if (mediaPlayer != null) mediaPlayer.stop();
        Stage stage = (Stage) songNameLabel.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXMLs/LoginPage.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setTitle("PULSE/Login");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * @summary
     * Submit and send review to DB
     * */
    public void submitReview() {
        if (reviewText.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Bad input for review!");
            alert.setContentText("Please write some text, before you submit review!");
            alert.show();
            return;
        }

        DBConnector connector = new DBConnector();
        Review review = connector.selectSongReviewForCurrentUser(user.getUserId(), actualSong.getSongId());

        // check if review for current song exists
        if (review.getReviewId() > 0){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.NO);
            alert.setHeaderText(null);
            alert.setTitle("Song already reviewed by you!");
            alert.setContentText("Do you want to rewrite old review? \n" +
                    "Review text: " +review.getReviewText() +"\n" +
                    "Rating: " +review.getRating());
            Optional<ButtonType> result = alert.showAndWait();

            // we want to rewrite existing review
            if (result.get() == ButtonType.YES){
                review.setReviewText(reviewText.getText());
                review.setRating(songRating);
                connector.updateReview(review);
            }
        } else
            connector.createNewReview(reviewText.getText(), songRating, user.getUserId(), actualSong.getSongId());

        // clear UI
        reviewText.clear();
        fillStars(0);
        songReviewsListView.setItems(connector.selectReviewsByIdOrALL(actualSong.getSongId()));
    }

    /**
     * @summary
     * Method that allow user to rate song from 1 - 5
     * @param mouseEvent
     * Used to get id of clicked star
     *
     * */
    public void starClicked(MouseEvent mouseEvent) {
        // get id of clicked star
        int lastStar = Integer.parseInt(String.valueOf(((ImageView) mouseEvent.getSource()).getId().charAt(4)));
        fillStars(lastStar);
    }

    /**
     * @summary
     * Update UI each second
     * */
    private void updateValues(){
        Platform.runLater(() -> {
            timeSlider.setValue(mediaPlayer.getCurrentTime().toSeconds());
            actualTimeLabel.setText(ConvertSecondsToStringTime((int) mediaPlayer.getCurrentTime().toSeconds()));
        });
    }

    /**
     * @summary
     * Method that will provide filling of stars
     * @param lastStar
     * ID of clicked star (1-5)
     * */
    private void fillStars(int lastStar){
        // if same star was clicked again we want to decrease rating by 1
        if (songRating == lastStar)
            lastStar--;

        for (int i = 1; i <= 5; i++){
            try {
                // reflection used instead of switch with 5 cases
                Field field = PlayerController.class.getDeclaredField("star" +i);

                if (i <= lastStar)
                    ((ImageView) field.get(this)).setImage(Images.starFilled);
                else
                    ((ImageView) field.get(this)).setImage(Images.starUnfilled);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

        // store id of last star if user decided to decrease rating by 1
        songRating = lastStar;
    }

    /**
     * @summary
     * Load albums
     * */
    public void loadAlbums(){
        // load all albums because there can be only albums for one artist
        albumsListView.setItems(albums);
        albumsListView.toFront();
    }

    /**
     * @summary
     * Load songs
     * */
    public void loadSongs(){
        // need to refill list view because it can only contains like songs
        songsListView.setItems(songs);
        songsListView.toFront();
    }

    /**
     * @summary
     * Load artists
     * List view to front as it has been loaded at start
     * */
    public void loadArtists(){
        artistListView.toFront();
    }

    /**
     * @summary
     * Load favourite songs
     * */
    public void loadFavourites(){
        DBConnector connector = new DBConnector();
        List<Integer> ids = connector.selectLikedSongs(user.getUserId());

        // show all songs if there are no liked songs
        if (ids.size() <= 0){
            songsListView.setItems(songs);
            songsListView.toFront();
            return;
        }

        ObservableList<Song> likedSongs = FXCollections.observableArrayList();
        songs.forEach(song -> {
            if (ids.contains(song.getSongId()))
                likedSongs.add(song);
        });

        songsListView.setItems(likedSongs);
        songsListView.toFront();
    }

    /**
     * @summary
     * Stores data about logged in user
     * @param user
     * User sent from login page
     * */
    public void sentData(User user){
        this.user = user;
        showUserName.setText(user.getUsername());
    }

    /**
     * @summary
     * Show songs in current album
     * @param album
     * Clicked album
     * */
    private void showAlbumSongs(Album album){
        ObservableList<Song> songsInAlbum = FXCollections.observableArrayList();

        songs.forEach(song -> {
            if (song.getAlbumName().equals(album.getAlbumName())) songsInAlbum.add(song);
        });

        songsListView.setItems(songsInAlbum);
        songsListView.toFront();
    }

    /**
     * @summary
     * Show songs for current artist
     * @param artist
     * Clicked artist
     * */
    private void showArtistAlbums(Artist artist){
        ObservableList<Album> artistsAlbums = FXCollections.observableArrayList();

        albums.forEach(album -> {
            if (album.getArtistName().equals(artist.getArtistName())) artistsAlbums.add(album);
        });

        albumsListView.setItems(artistsAlbums);
        albumsListView.toFront();
    }

    /**
     * @summary
     * Enable and show lyrics of current song
     * */
    private void startLyrics(){
        if (actualSong.getLyricsUrl().isEmpty()){
            lyricsText.setText("No lyrics for this song!");
            return;
        }

        URL url;
        URLConnection connection;
        BufferedReader bufferedReader;
        String line;

        try {
            // get lyrics from firebase storage
            url = new URL(actualSong.getLyricsUrl());
            connection = url.openConnection();
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            lyrics = new TreeMap<>();

            // create map with lyrics for every millisecond
            // lyrics format
            // 00:00:00,000 --> 00:00:00,000
            // Few rows of text
            // Empty row
            while ((line = bufferedReader.readLine()) != null){
                if (line.contains("-->")){
                    int from = ConvertToMillis(line.split("-->")[0]);
                    int to = ConvertToMillis(line.split("-->")[1]);

                    // get text after time
                    String text = "";
                    while (!(line = bufferedReader.readLine()).isEmpty()){
                        text = text + line + "\n";
                    }

                    // insert same text for every millisecond during show time
                    // for case when user change playing time of song
                    for (int i = from; i < to; i++)
                        lyrics.put(i, text);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // crate thread that will run every millisecond and show lyrics
        Thread thread = new Thread(() -> {
            while (true){
                if (!playing)
                    return;

                try {
                    Thread.sleep(1);
                    // show lyrics if we have some for current time
                    if (lyrics.containsKey((int) mediaPlayer.getCurrentTime().toMillis()))
                        Platform.runLater(() -> lyricsText.setText(lyrics.get((int) mediaPlayer.getCurrentTime().toMillis())));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    playing = false;
                }
            }
        });

        thread.start();
    }

    /**
     * @summary
     * Setup layout for current song
     * Play song
     * */
    private void setupAndPlaySong(Song song){
        songPane.toFront();

        // only show layout if same song is playing
        if (song == actualSong){
            return;
        }

        actualSong = song;
        playing = false;
        songRating = 0;
        timeSlider.setMax(song.getLengthInSeconds());
        timeSlider.setValue(0);
        timeSlider.setDisable(false);
        songNameLabel.setText(song.getName());
        actualTimeLabel.setText("00:00");
        songLengthLabel.setText(song.getLength());

        // show reviews for current song
        DBConnector connector = new DBConnector();
        songReviewsListView.setCellFactory(songReviewsListView -> new SongReviewsListView());
        songReviewsListView.setItems(connector.selectReviewsByIdOrALL(song.getSongId()));

        // setup media player and play song
        media = new Media(song.getDownloadUrl());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(volumeSlider.getValue()/100);
        mediaPlayer.seek(Duration.ZERO);
        playAndPause();

        // move to start after end of song
        mediaPlayer.setOnEndOfMedia(() -> {
            stop();
        });

        // pause music when changing position with slider
        timeSlider.setOnMousePressed(event ->{
            mediaPlayer.pause();
        });

        // unpause music after change of position and update song
        timeSlider.setOnMouseReleased(event ->{
            mediaPlayer.play();
            mediaPlayer.seek(mediaPlayer.getMedia().getDuration().multiply(timeSlider.getValue()/song.getLengthInSeconds()));
        });

        // set volume listener
        volumeSlider.valueProperty().addListener(observable -> mediaPlayer.setVolume(volumeSlider.getValue()/100));
    }

    /**
     * @summary
     * Method that will provide search in songs
     * */
    private void searchInSongs(){
        DBConnector connector = new DBConnector();
        FilteredList<Song> filteredData = new FilteredList<>(connector.selectAllSongs(), e -> true);

        searchField.setOnKeyPressed(e -> {
            songsListView.toFront();
            searchField.textProperty().addListener((observableValue, oldValue, newValue) -> {
                String LC = newValue.toLowerCase();
                // if one of these conditions is true we want to show song
                filteredData.setPredicate((Predicate<? super Song>) song->{
                    if(newValue == null || newValue.isEmpty())
                        return true;
                    if(null != song.getName() && song.getName().toLowerCase().contains(LC))
                        return true;
                    if(null != song.getAlbumName() && song.getAlbumName().toLowerCase().contains(LC))
                        return true;
                    if(null != song.getArtistName() && song.getArtistName().toLowerCase().contains(LC))
                        return true;
                    if(null != song.getGenreName() && song.getGenreName().toLowerCase().contains(LC))
                        return true;
                    return false;
                });
            });

            SortedList<Song> sortedData = new SortedList<>(filteredData);
            songsListView.setItems(sortedData);
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // enable search in songs and setup values
        searchInSongs();
        timeSlider.setValue(0);
        timeSlider.setDisable(true);
        playing = false;

        DBConnector connector = new DBConnector();
        albums = connector.selectAllAlbums();
        songs = connector.selectAllSongs();

        songsListView.setItems(songs);
        albumsListView.setItems(albums);
        artistListView.setItems(connector.selectAllArtists());

        // album list view is default view
        albumsListView.toFront();

        // enable double click on albums and setup album list view
        albumsListView.setCellFactory(albumsListView -> new AlbumListView());
        albumsListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && (! albumsListView.getSelectionModel().getSelectedItems().isEmpty()))
                showAlbumSongs(albumsListView.getSelectionModel().getSelectedItem());
        });

        // enable double click on songs and setup songs list view
        songsListView.setCellFactory(songListView -> new SongListView(user.getUserId()));
        songsListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && (! songsListView.getSelectionModel().getSelectedItems().isEmpty()) ) {
                Song clicked = songsListView.getSelectionModel().getSelectedItem();

                // if we clicked on playing song we do not want to restart it
                if(mediaPlayer != null && clicked != actualSong)
                    mediaPlayer.stop();
                setupAndPlaySong(clicked);
            }
        });

        // enable double click on artists and setup artists list view
        artistListView.setCellFactory(artistListView -> new ArtistListView());
        artistListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && (! artistListView.getSelectionModel().getSelectedItems().isEmpty()))
                showArtistAlbums(artistListView.getSelectionModel().getSelectedItem());
        });
    }
}