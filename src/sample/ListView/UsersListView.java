package sample.ListView;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import sample.DB.DBConnector;
import sample.Models.User;
import java.io.IOException;
import java.util.Optional;

/**
 * @summary
 * Class that will fill data to custom user cell
 * */
public class UsersListView extends ListCell<User> {

    @FXML private GridPane userCell;
    @FXML private Label userId, username, mail, registrationDate, isAdmin, banned;
    @FXML private Button ban, unbanned;

    private FXMLLoader loader;

    /**
     * @summary
     * Setup data
     * */
    @Override
    protected void updateItem(User user, boolean empty) {
        super.updateItem(user, empty);

        if(empty || user == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (loader == null) {
                loader = new FXMLLoader(getClass().getResource("../ListView/UserCell.fxml"));
                loader.setController(this);

                try {
                    loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            userId.setText(String.valueOf(user.getUserId()));
            username.setText(user.getUsername());
            mail.setText(user.getMail());
            registrationDate.setText(user.getRegistrationDate());
            isAdmin.setText(String.valueOf(user.isAdmin()));
            banned.setText(String.valueOf(user.isBanned()));

            DBConnector connector = new DBConnector();

            // enable ban function
            ban.setOnAction(event -> {
                Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText(null);
                alert.setTitle("Ban user");
                alert.setContentText("Are you sure that you want ban this user?");
                Optional<ButtonType> answer=alert.showAndWait();
                if (answer.get() == ButtonType.OK){
                    connector.setBanned(user.getUserId());
                    banned.setText("true");
                    alert=new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("User successfully banned");
                    alert.showAndWait();
                }
            });

            // enable unban function
            unbanned.setOnAction(event -> {
                Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText(null);
                alert.setTitle("Unban user");
                alert.setContentText("Are you sure that you want unban this user?");
                Optional<ButtonType> answer=alert.showAndWait();
                if (answer.get() == ButtonType.OK){
                    connector.setUnbanned(user.getUserId());
                    banned.setText("false");
                    alert=new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(null);
                    alert.setContentText("User successfully unbanned");
                    alert.showAndWait();
                }
            });

            setText(null);
            setGraphic(userCell);
        }
    }
}
