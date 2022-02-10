package sample.ListView;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import sample.Constantts.Images;
import sample.DB.DBConnector;
import sample.Models.Song;
import java.io.IOException;
import java.util.List;

/**
 * @summary
 * Class that will fill data to custom song cell
 * */
public class SongListView extends  ListCell<Song>{

    @FXML private Label songName, albumName, artistName, genreName, songLength;
    @FXML private GridPane songCell;
    @FXML private ImageView albumCover, likeButton;

    private FXMLLoader loader;
    private int userId;
    private boolean firstRun;

    /**
     * @summary
     * Store user ID after creation
     * @param userId
     * User ID
     * */
    public SongListView (int userId){
        this.userId = userId;
    }

    /**
     * @summary
     * Setup data
     * */
    @Override
    protected void updateItem(Song song, boolean empty) {
        super.updateItem(song, empty);

        if(empty || song == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (loader == null) {
                loader = new FXMLLoader(getClass().getResource("../ListView/SongCell.fxml"));
                loader.setController(this);

                try {
                    loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            albumCover.setImage(song.getAlbumCover());
            songName.setText(song.getName());
            albumName.setText(song.getAlbumName());
            artistName.setText(song.getArtistName());
            genreName.setText(song.getGenreName());
            songLength.setText(song.getLength());

            // set like or unliked icon for every song
            DBConnector connector = new DBConnector();
            List<Integer> likedSongs = connector.selectLikedSongs(userId);
            if (likedSongs.contains(song.getSongId()))
                likeButton.setImage(Images.liked);
            else
                likeButton.setImage(Images.unLiked);

            // enable like function
            likeButton.setOnMouseClicked(event -> {
                List<Integer> likedSongsAfterClick = connector.selectLikedSongs(userId);
                if (likedSongsAfterClick.contains(song.getSongId())){
                    likeButton.setImage(Images.unLiked);
                    likedSongsAfterClick.remove(likedSongsAfterClick.indexOf(song.getSongId()));
                }else {
                    likeButton.setImage(Images.liked);
                    likedSongsAfterClick.add(song.getSongId());
                }

                connector.updateLikedSongs(likedSongsAfterClick, userId);
            });

            setText(null);
            setGraphic(songCell);
        }
    }
}
