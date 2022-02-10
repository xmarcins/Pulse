package sample.ListView;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import sample.Models.Album;
import java.io.IOException;

/**
 * @summary
 * Class that fill data to custom album cell
 * */
public class AlbumListView extends ListCell<Album> {

    @FXML private GridPane albumCell;
    @FXML private Label albumName, genreName, songsNumber, totalLength, artistName;
    @FXML private ImageView albumCover;

    private FXMLLoader loader;

    /**
     * @summary
     * Setup data
     * */
    @Override
    protected void updateItem(Album album, boolean empty) {
        super.updateItem(album, empty);

        if(empty || album == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (loader == null) {
                loader = new FXMLLoader(getClass().getResource("../ListView/AlbumCell.fxml"));
                loader.setController(this);

                try {
                    loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            albumCover.setImage(album.getAlbumCover());
            albumName.setText(album.getAlbumName());
            genreName.setText(album.getGenreName());
            artistName.setText(album.getArtistName());
            songsNumber.setText("Songs: " +album.getSongsNumber());
            totalLength.setText("Total length: " +album.getTotalLength());

            setText(null);
            setGraphic(albumCell);
        }
    }
}
