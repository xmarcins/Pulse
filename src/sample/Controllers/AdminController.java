package sample.Controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import sample.DB.DBConnector;
import sample.ListView.ReviewsListView;
import sample.ListView.UsersListView;
import sample.Models.Review;
import sample.Models.User;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

    @FXML private BorderPane borderPane;
    @FXML private ListView adminListView, usersListViewForAdmin;

    private ObservableList<Review>reviews;
    private ObservableList<User>users;

    /**
     * @summary
     * Logout user
     * */
    public void backToLogin() throws IOException {
        Stage stage = (Stage) borderPane.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXMLs/LoginPage.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setTitle("PULSE/Login");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DBConnector connector =new DBConnector();

        // select all reviews and setup list view for reviews
        reviews = connector.selectReviewsByIdOrALL(0);
        adminListView.setCellFactory(reviewsListView -> new ReviewsListView());
        adminListView.setItems(reviews);

        // select all users and setup list view for users
        users = connector.selectUsersForAdmin();
        usersListViewForAdmin.setCellFactory(usersListViewForAdmin -> new UsersListView());
        usersListViewForAdmin.setItems(users);
    }
}
