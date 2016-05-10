package imageprocessor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;


public class MainApp extends Application {

    static Stage primaryStage;
    private BorderPane mainLayout;


    @Override
    public void start(Stage primaryStage) throws Exception {
        MainApp.primaryStage = primaryStage;
        MainApp.primaryStage.setTitle("CS 555 Image Processor");
        showMainView();
    }


    private void showMainView() {
        try {
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(getClass().getResource("/imageprocessor/MainView.fxml"));
            mainLayout = loader.load();

            primaryStage.setScene(new Scene(mainLayout));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
