package sample.Models;

public class Review {
    private int reviewId;
    private String reviewText;
    private String userName;
    private String songName;
    private String albumName;
    private int songId;
    private int userId;
    private int rating;

    public Review(int reviewId, String reviewText, String userName, String songName,String albumName, int songId,int userId, int rating) {
        this.reviewId = reviewId;
        this.reviewText = reviewText;
        this.userName = userName;
        this.songName = songName;
        this.albumName = albumName;
        this.songId = songId;
        this.userId=userId;
        this.rating = rating;
    }

    public Review() {
        this.reviewId = 0;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
