package sample.ListView;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import sample.DB.DBConnector;
import sample.Models.Review;
import java.io.IOException;
import java.util.Optional;

/**
 * @summary
 * Class that will fill data to custom review cell, for admin page
 * */
public class ReviewsListView extends ListCell<Review> {

    @FXML private Label reviewId, reviewText, userName, songName, albumName, rating;
    @FXML private Button banned;
    @FXML private GridPane reviewCell;

    private FXMLLoader loader;
    private DBConnector connector=new DBConnector();

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
                loader = new FXMLLoader(getClass().getResource("../ListView/ReviewCell.fxml"));
                loader.setController(this);

                try {
                    loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            reviewId.setText(String.valueOf(review.getReviewId()));
            reviewText.setText(review.getReviewText());
            userName.setText(review.getUserName());
            songName.setText(review.getSongName());
            albumName.setText(review.getAlbumName());
            rating.setText(String.valueOf(review.getRating()));
            setText(null);
            setGraphic(reviewCell);

            // create on click listener that allows to ban user for review
            banned.setOnAction(event -> {
                Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText(null);
                alert.setTitle("Ban user");
                alert.setContentText("Are you sure that you want ban this user?");
                Optional<ButtonType> answer=alert.showAndWait();
                if (answer.get() == ButtonType.OK){
                    connector.setBanned(review.getUserId());
                    System.out.println("Banned");
                    alert=new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("User successfully banned");
                    alert.showAndWait();
                }
            });
        }
    }
}
