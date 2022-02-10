package sample.ListView;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;
import sample.Models.Review;
import java.io.IOException;

/**
 * @summary
 * Class that will fill data to custom review cell for song
 * */
public class SongReviewsListView extends ListCell<Review> {

    @FXML private GridPane songReviewCell;
    @FXML private Label reviewText, reviewRating, username;

    private FXMLLoader loader;

    /**
     * @summary
     * Setup data
     * */
    @Override
    protected void updateItem(Review review, boolean empty) {
        super.updateItem(review, empty);

        if(empty || review == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (loader == null) {
                loader = new FXMLLoader(getClass().getResource("../ListView/SongReviewCell.fxml"));
                loader.setController(this);
                try {
                    loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            reviewText.setText(review.getReviewText());
            reviewRating.setText(String.valueOf(review.getRating()));
            username.setText(review.getUserName());

            setText(null);
            setGraphic(songReviewCell);
        }
    }
}