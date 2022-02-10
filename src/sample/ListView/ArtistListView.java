package sample.ListView;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;
import sample.Models.Artist;
import java.io.IOException;

/**
 * @summary
 * Class that will fill data to custom artist cell
 * */
public class ArtistListView extends ListCell<Artist>{

    @FXML private GridPane artistCell;
    @FXML private Label artistName;

    private FXMLLoader loader;

    /**
     * @summary
     * Setup data
     * */
    @Override
    protected void updateItem(Artist artist, boolean empty) {
        super.updateItem(artist, empty);

        if(empty || artist == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (loader == null) {
                loader = new FXMLLoader(getClass().getResource("../ListView/ArtistCell.fxml"));
                loader.setController(this);

                try {
                    loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            artistName.setText(artist.getArtistName());

            setText(null);
            setGraphic(artistCell);
        }
    }
}
