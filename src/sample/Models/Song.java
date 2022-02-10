package sample.Models;

import javafx.scene.image.Image;
import static sample.Extensions.TimeExtensions.ConvertSecondsToStringTime;

public class Song {

    private int songId;
    private String name;
    private String artistName;
    private String albumName;
    private String genreName;
    private int lengthInSeconds;
    private String length;
    private String downloadUrl;
    private Image albumCover;
    private String lyricsUrl;

    public Song(int songId, String name, String artistName, String albumName, String genreName, int lengthInSeconds, String downloadUrl, Image albumCoverUrl, String lyricsUrl) {
        this.songId = songId;
        this.name = name;
        this.artistName = artistName;
        this.albumName = albumName;
        this.genreName = genreName;
        this.lengthInSeconds = lengthInSeconds;
        this.length = ConvertSecondsToStringTime(lengthInSeconds);
        this.downloadUrl = downloadUrl;
        this.albumCover = albumCoverUrl;
        this.lyricsUrl = lyricsUrl;
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    public int getLengthInSeconds() {
        return lengthInSeconds;
    }

    public void setLengthInSeconds(int lengthInSeconds) {
        this.lengthInSeconds = lengthInSeconds;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public Image getAlbumCover() {
        return albumCover;
    }

    public void setAlbumCover(Image albumCover) {
        this.albumCover = albumCover;
    }

    public String getLyricsUrl() {
        return lyricsUrl;
    }

    public void setLyricsUrl(String lyricsUrl) {
        this.lyricsUrl = lyricsUrl;
    }
}