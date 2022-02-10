package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.net.URL;
import java.net.URLConnection;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        // show error when there is no internet connection, as data are downloaded from firebase
        if(!checkInternetConnection()){
            showAlert();
            System.exit(0);
        }

        Parent root = FXMLLoader.load(getClass().getResource("FXMLs/LoginPage.fxml"));
        primaryStage.setTitle("PULSE");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /**
     * @summary
     * Check internet connection
     * @return
     * TRUE - internet connection
     * FALSE - no internet connection
     * */
    private boolean checkInternetConnection(){
        try {
            URL url = new URL("http://www.google.com");
            URLConnection connection = url.openConnection();
            connection.connect();
            return true;
        } catch (Exception e){
            return false;
        }
    }

    /**
     * @summary
     * Show alert
     * */
    private void showAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText("Please connect to the internet!");
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
