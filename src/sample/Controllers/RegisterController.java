package sample.Controllers;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.apache.commons.codec.digest.DigestUtils;
import sample.DB.DBConnector;
import sample.Models.User;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    @FXML private JFXTextField usernameTextField, mailTextField;
    @FXML private JFXPasswordField passwordTextField;
    @FXML private Label alertLabel;

    private List<User> users;

    /**
     * @summary
     * Register user
     * */
    public void register() throws IOException {
        if(usernameTextField.getText().isEmpty()){
            alertLabel.setText("PLEASE ENTER USERNAME!");
        }else if(mailTextField.getText().isEmpty() || !mailTextField.getText().matches("^(.+)@(.+)$")){
            alertLabel.setText("PLEASE ENTER VALID E-MAIL!");
        }else if(passwordTextField.getText().isEmpty()) {
            alertLabel.setText("PLEASE ENTER PASSWORD!");
        }else if(!checkUsernameAndEmail())
            alertLabel.setText("THIS E-MAIL IS ALREADY USED!");
        else{
            DBConnector conn = new DBConnector();
            conn.registerUser(usernameTextField.getText(), mailTextField.getText(), encrypt(passwordTextField.getText()));
            goToLogin();
        }
    }

    /**
     * @summary
     * Go to login page
     * */
    public void goToLogin() throws IOException {
        Stage stage = (Stage) usernameTextField.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXMLs/LoginPage.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setTitle("PULSE/Login");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * @summary
     * Check if e-mail and username are not used
     * */
    private boolean checkUsernameAndEmail(){
        for (User user:users) {
            if (usernameTextField.getText().equals(user.getUsername())) {
                alertLabel.setText("THIS USERNAME IS ALREADY USED!");
                return false;
            } else if(mailTextField.getText().equals(user.getMail())) {
                alertLabel.setText("THIS EMAIL IS ALREADY USED!");
                return false;
            }
        }
        return true;
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