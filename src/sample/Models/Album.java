package sample.Models;

import javafx.scene.image.Image;
import static sample.Extensions.TimeExtensions.ConvertSecondsToStringTime;

public class Album {

    private int albumId;
    private String albumName;
    private int songsNumber;
    private int totalLengthInSeconds;
    private String totalLength;
    private String genreName;
    private String artistName;
    private Image albumCover;

    public Album(int albumId, String albumName, int songsNumber, int totalLengthInSeconds, String genreName, String artistName, Image albumCoverUrl) {
        this.albumId = albumId;
        this.albumName = albumName;
        this.songsNumber = songsNumber;
        this.totalLengthInSeconds = totalLengthInSeconds;
        this.totalLength = ConvertSecondsToStringTime(totalLengthInSeconds);
        this.genreName = genreName;
        this.artistName = artistName;
        this.albumCover = albumCoverUrl;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public int getSongsNumber() {
        return songsNumber;
    }

    public void setSongsNumber(int songsNumber) {
        this.songsNumber = songsNumber;
    }

    public int getTotalLengthInSeconds() {
        return totalLengthInSeconds;
    }

    public void setTotalLengthInSeconds(int totalLengthInSeconds) {
        this.totalLengthInSeconds = totalLengthInSeconds;
    }

    public String getTotalLength() {
        return totalLength;
    }

    public void setTotalLength(String totalLength) {
        this.totalLength = totalLength;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public Image getAlbumCover() {
        return albumCover;
    }

    public void setAlbumCover(Image albumCover) {
        this.albumCover = albumCover;
    }
}
