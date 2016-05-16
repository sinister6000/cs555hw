package imageprocessor;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;


public class MainApp extends Application {

    static Stage primaryStage;
    private BorderPane mainLayout;


    @Override
    public void start(Stage primaryStage) throws Exception {
        MainApp.primaryStage = primaryStage;
        MainApp.primaryStage
              .setTitle("·.,_,.·-·.,_,.·-·.,_,.·-·.,_,.·[ CS 555 Image Processor ]·.,_,.·-·.,_,.·-·.,_,.·-·.,_,.·");
        showMainView();
    }


    public void showMainView() {
        try {
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(getClass().getResource("/imageprocessor/MainView.fxml"));
            mainLayout = loader.load();

            primaryStage.setScene(new Scene(mainLayout));

            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent t) {
                    Platform.exit();
                    System.exit(0);
                    try {
                        stop();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            //set Stage boundaries to visible bounds of the main screen
            Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
            primaryStage.setX(primaryScreenBounds.getMinX());
            primaryStage.setY(primaryScreenBounds.getMinY());
            primaryStage.setWidth(primaryScreenBounds.getWidth());
            primaryStage.setHeight(primaryScreenBounds.getHeight());

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
