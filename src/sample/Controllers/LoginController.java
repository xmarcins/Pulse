package sample.Controllers;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.commons.codec.digest.DigestUtils;
import sample.DB.DBConnector;
import sample.Models.User;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML private Pane pane;
    @FXML private JFXTextField usernameTextField;
    @FXML private JFXPasswordField passwordTextField;
    @FXML private Label alertLabel;

    private List<User> users;

    /**
     * @summary
     * Login user
     * */
    public void login() throws IOException {
        for (User user : users) {
            if (usernameTextField.getText().equals(user.getUsername()) && encrypt(passwordTextField.getText()).equals(user.getPassword())) {
                Stage stage = (Stage) pane.getScene().getWindow();
                Parent root;

                if(!user.isBanned()){
                    if (user.isAdmin())
                        root = new FXMLLoader(getClass().getResource("../FXMLs/AdminPage.fxml")).load();
                    else {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXMLs/MusicPlayerPage.fxml"));
                        root = loader.load();
                        ((PlayerController) loader.getController()).sentData(user);
                    }

                    Scene scene = new Scene(root);
                    stage.setTitle("PULSE/Music Player");
                    stage.setScene(scene);
                    stage.setResizable(false);
                    stage.show();
                }
                else
                    alertLabel.setText("YOUR ACCOUNT HAS BEEN BANNED!");
            }
            else
                alertLabel.setText("WRONG USERNAME OR PASSWORD!");
        }
    }

    /**
     * @summary
     * Logout user
     * */
    public void goToRegistration() throws IOException{
        Stage stage = (Stage) pane.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXMLs/RegistrationPage.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setTitle("PULSE/Registration");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * @summary
     * Encrypt password - sha512
     * */
    public String encrypt(String pass){
        return DigestUtils.sha512Hex(pass);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        users = new DBConnector().selectAllUsers();
    }
}
