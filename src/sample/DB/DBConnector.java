package sample.DB;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import sample.Constantts.Images;
import sample.Extensions.ArrayExtensions;
import sample.Models.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static sample.Extensions.ArrayExtensions.ConvertStringToIntegerList;

public class DBConnector {
    private Connection connection;

    /**
     * @summary
     * Get connection to DB
     * @return
     * Current connection
     * */
    public Connection getConnection() {
        return connection;
    }

    /**
     * @summary
     * Set connection to DB
     * @param connection
     * Connection to set
     * */
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * @summary
     * Connect DB
     * */
    public void connect() {
        try {
            String url = "jdbc:sqlite:src/sample/db/maturitnyProjektDemoDB.db";
            this.connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @summary
     * Disconnect DB
     * */
    public void disconnect(){
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * @summary
     * Insert new user to DB after valid registration
     * @param mail
     * User e-mail
     * @param name
     * User name
     * @param pass
     * User password*/
    public void registerUser(String name, String mail, String pass){
        connect();
        String SQL="INSERT INTO users(username, email, password, registration_date, is_admin, banned, liked_songs) VALUES(?,?,?,?,?,?,?)";
        try (PreparedStatement pstm=connection.prepareStatement(SQL)){
            pstm.setString(1,name);
            pstm.setString(2,mail);
            pstm.setString(3,pass);
            pstm.setString(4, new Timestamp(System.currentTimeMillis()).toString());
            pstm.setBoolean(5, false);
            pstm.setBoolean(6, false);
            pstm.setString(7, "");
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            disconnect();
            System.out.println("User '" +name +"' successfully registered!");
        }
    }

    /**
     * @summary
     * Get all users from DB
     * @return
     * List of users
     * */
    public ObservableList<User> selectAllUsers(){
        connect();
        String SQL="SELECT user_id, username, email, password, registration_date, is_admin, banned, liked_songs FROM users";
        ObservableList<User> data=null;
        try {
            data= FXCollections.observableArrayList();
            Statement stmt=connection.createStatement();
            ResultSet rs=stmt.executeQuery(SQL);
            while(rs.next()){
                data.add(new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("email"),
                        rs.getString("password"), rs.getString("registration_date"), rs.getBoolean("is_admin"),
                        rs.getBoolean("banned"), rs.getString("liked_songs")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            disconnect();
            System.out.println("Successfully selected all users!");
        }
        return data;
    }

    /**
     * @summary
     * Get all songs from DB
     * @return
     * List of songs*/
    public ObservableList<Song> selectAllSongs(){
        connect();
        String SQL="SELECT songs.song_id AS songId, songs.name AS songName, songs.length AS songLength," +
                " artists.name AS artistName, albums.name AS albumName, genres.name AS genreName," +
                " songs.download_url AS downloadUrl, albums.album_cover_url AS albumCoverUrl," +
                " songs.lyrics_url AS lyricsUrl FROM songs JOIN artists ON songs.artist_id = artists.artist_id" +
                " JOIN albums ON songs.album_id = albums.album_id JOIN genres ON songs.genre_id = genres.genre_id;";
        ObservableList<Song> data=null;
        try {
            data= FXCollections.observableArrayList();
            Statement stmt=connection.createStatement();
            ResultSet rs=stmt.executeQuery(SQL);
            while(rs.next()){
                String albumCoverUrl = rs.getString("albumCoverUrl");
                // add song cover only if it does not exists, improve loading time
                if (!Images.albumCovers.containsKey(albumCoverUrl)) Images.albumCovers.put(albumCoverUrl, new Image(albumCoverUrl));

                data.add(new Song(rs.getInt("songId"), rs.getString("songName"),
                        rs.getString("artistName"), rs.getString("albumName"),
                        rs.getString("genreName"), rs.getInt("songLength"),
                        rs.getString("downloadUrl"), Images.albumCovers.get(albumCoverUrl), rs.getString("lyricsUrl")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            disconnect();
            System.out.println("Successfully selected all songs!");
        }
        return data;
    }

    /**
     * @summary
     * Get all albums from DB
     * @return
     * List of albums
     * */
    public ObservableList<Album> selectAllAlbums(){
        connect();
        String SQL="SELECT albums.album_id AS albumId, albums.name AS albumName, albums.number_of_songs AS songsNumber, " +
                "albums.total_length AS totalLength, genres.name AS genreName, artists.name AS artistName, " +
                "albums.album_cover_url AS albumCoverUrl FROM albums " +
                "JOIN genres ON albums.main_genre_id = genres.genre_id JOIN artists ON albums.artist_id = artists.artist_id;";
        ObservableList<Album> data=null;
        try {
            data= FXCollections.observableArrayList();
            Statement stmt=connection.createStatement();
            ResultSet rs=stmt.executeQuery(SQL);
            while(rs.next()){
                String albumCoverUrl = rs.getString("albumCoverUrl");
                // add album cover only if it does not exist, improve loading time
                if (!Images.albumCovers.containsKey(albumCoverUrl)) Images.albumCovers.put(albumCoverUrl, new Image(albumCoverUrl));

                data.add(new Album(rs.getInt("albumId"), rs.getString("albumName"),
                        rs.getInt("songsNumber"), rs.getInt("totalLength"),
                        rs.getString("genreName"), rs.getString("artistName"), Images.albumCovers.get(albumCoverUrl)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            disconnect();
            System.out.println("Successfully selected all albums!");
        }
        return data;
    }

    /**
     * @summary
     * Get all artists from DB
     * @return
     * List of artists
     * */
    public ObservableList<Artist> selectAllArtists(){
        connect();
        String SQL="SELECT artists.artist_id AS artistId, artists.name AS artistName FROM artists;";
        ObservableList<Artist> data=null;
        try {
            data= FXCollections.observableArrayList();
            Statement stmt=connection.createStatement();
            ResultSet rs=stmt.executeQuery(SQL);
            while(rs.next()){
                data.add(new Artist(rs.getInt("artistId"), rs.getString("artistName")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            disconnect();
            System.out.println("Successfully selected all artists!");
        }
        return data;
    }

    /**
     * @summary
     * Select reviews from DB
     * @param songId
     * Song ID for which we need to select reviews
     * ID = 0 - select all reviews
     * @return
     * List of reviews
     * */
    public ObservableList<Review> selectReviewsByIdOrALL(int songId){
        connect();
        String SQL="SELECT reviews.review_id AS reviewId, reviews.review_text AS reviewText, reviews.rating AS rating,"
                +" users.username AS userName, users.user_id AS userId, songs.name AS songName,songs.song_id AS songId,albums.name AS albumName"
                 +" FROM reviews JOIN users ON reviews.user_id = users.user_id JOIN songs ON reviews.song_id = songs.song_id "
                +"JOIN albums ON songs.album_id = albums.album_id";
        ObservableList<Review> data=null;

        // select reviews for specific song
        if (songId>0) SQL=SQL+" WHERE reviews.song_id = "+songId;

        try{
            data = FXCollections.observableArrayList();
            Statement stmt=connection.createStatement();
            ResultSet rs=stmt.executeQuery(SQL);
            while(rs.next()){
                data.add(new Review(rs.getInt("reviewId"),rs.getString("reviewText"),
                        rs.getString("userName"),rs.getString("songName"),
                        rs.getString("albumName"),rs.getInt("songId"),rs.getInt("userId"),rs.getInt("rating")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnect();
            if (songId == 0)
                System.out.println("Successfully selected all reviews!");
            else
                System.out.println("Successfully selected reviews for song with ID: " + songId);
        }
        return data;
    }

    /**
     * @summary
     * Ban user in DB
     * @param userId
     * User ID of banned user
     * */
    public void setBanned(int userId){
        connect();
        String SQL="UPDATE users SET banned ='1' WHERE user_id = '"+userId+"' ";
        try {
            Statement stmt=connection.createStatement();
            stmt.execute(SQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            disconnect();
            System.out.println("Successfully banned user with ID: " +userId);
        }
    }

    /**
     * @summary
     * Unban user in DB
     * @param userId
     * User ID of user to unban
     * */
    public void setUnbanned(int userId){
        connect();
        String SQL="UPDATE users SET banned = '0' WHERE user_id = '"+userId+"' ";
        try {
            Statement stmt=connection.createStatement();
            stmt.execute(SQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            disconnect();
            System.out.println("Successfully unbanned user with ID: " +userId);
        }
    }

    /**
     * @summary
     * Update liked songs in DB after change
     * @param userId
     * ID of user who has changed liked songs
     * @param liked
     * List of liked songs that will be inserted
     * */
    public void updateLikedSongs(List<Integer> liked, int userId){
        connect();
        String SQL = "UPDATE users SET liked_songs = ? WHERE user_id = ?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, ArrayExtensions.ConvertIntegerListToString(liked));
            preparedStatement.setInt(2, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnect();
            System.out.println("Successfully updated liked songs for user with ID: " +userId +", liked songs: " +ArrayExtensions.ConvertIntegerListToString(liked));
        }
    }

    /**
     * @summary
     * Get liked songs for current user
     * @param userId
     * User ID
     * @return
     * List of song IDs*/
    public List<Integer> selectLikedSongs(int userId){
        List<Integer> result = null;
        connect();
        String SQL = "SELECT liked_songs FROM users WHERE users.user_id = " +userId;
        try {
            result = new ArrayList<>();
            Statement statement = connection.createStatement();
            ResultSet resultSet=statement.executeQuery(SQL);
            result = ConvertStringToIntegerList(resultSet.getString("liked_songs"));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }
        return result;
    }

    /**
     * @summary
     * Get review for current user
     * @param userId
     * User ID
     * @param songId
     * Song ID
     * @return
     * Review
     * null = review created by user for current song does not exists*/
    public Review selectSongReviewForCurrentUser(int userId, int songId){
        connect();
        String SQL="SELECT reviews.review_id AS reviewId, reviews.review_text AS reviewText, reviews.rating AS rating " +
                "FROM reviews WHERE reviews.user_id = " +userId +" AND reviews.song_id = " +songId;
        Review review = null;
        try{
            review = new Review();
            Statement stmt=connection.createStatement();
            ResultSet rs=stmt.executeQuery(SQL);
            if (rs.next()){
                review.setReviewId(rs.getInt("reviewId"));
                review.setReviewText(rs.getString("reviewText"));
                review.setRating(rs.getInt("rating"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnect();
            System.out.print("Review for song with ID: " +songId);
            if (review == null)
                System.out.println(" does not exists!");
            else
                System.out.println(" exits!");
        }
        return review;
    }

    /**
     * @summary
     * Update review in DB
     * @param review
     * Review*/
    public void updateReview(Review review){
        connect();
        String SQL = "\n" +
                "UPDATE reviews SET review_text = ?, rating = ? WHERE reviews.review_id = ?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, review.getReviewText());
            preparedStatement.setInt(2, review.getRating());
            preparedStatement.setInt(3, review.getReviewId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnect();
            System.out.println("Successfully updated review with id " +review.getReviewId());
        }
    }

    /**
     * @summary
     * Insert new review to DB
     * @param songId
     * Song ID
     * @param userId
     * User ID
     * @param rating
     * Rating of song
     * @param reviewText
     * Text of review*/
    public void createNewReview(String reviewText, int rating, int userId, int songId){
        connect();
        String SQL="INSERT INTO reviews(review_text, rating, user_id, song_id) VALUES (?,?,?,?)";
        try (PreparedStatement pstm=connection.prepareStatement(SQL)){
            pstm.setString(1, reviewText);
            pstm.setInt(2, rating);
            pstm.setInt(3, userId);
            pstm.setInt(4, songId);
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            disconnect();
            System.out.println("Successfully created review for song with ID: " +songId);
        }
    }

    /**
     * @summary
     * Get all users without admins
     * @return
     * List of users
     * */
    public ObservableList<User> selectUsersForAdmin(){
        connect();
        String SQL="SELECT users.user_id AS userId, users.username, users.email AS mail, users.registration_date AS registrationDate, users.is_admin AS isAdmin, banned FROM users "+
                "WHERE users.is_admin = 0";
        ObservableList<User> data=null;
        try {
            data= FXCollections.observableArrayList();
            Statement stmt=connection.createStatement();
            ResultSet rs=stmt.executeQuery(SQL);
            while(rs.next()){
                data.add(new User(rs.getInt("userId"), rs.getString("username"),rs.getString("mail"),
                        rs.getString("registrationDate"),rs.getBoolean("isAdmin"),rs.getBoolean("banned")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            disconnect();
            System.out.println("Successfully selected all usersForAdmin!");
        }
        return data;
    }
}
