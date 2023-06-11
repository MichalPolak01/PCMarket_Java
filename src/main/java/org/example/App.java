package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX application entry point.
 */
public class App extends Application {

    private static Scene scene;

    /**
     * The main entry point for the JavaFX application.
     *
     * @param stage The primary stage for this application.
     * @throws IOException If an error occurs during loading of FXML or initializing the stage.
     */
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("login"), 960, 540);
        stage.setScene(scene);
        stage.setTitle("PCMarket logowanie");
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Sets the root FXML file for the scene.
     *
     * @param fxml The name of the FXML file to load.
     * @throws IOException If an error occurs during loading of FXML.
     */
    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    /**
     * Loads an FXML file and returns its root element as a Parent object.
     *
     * @param fxml The name of the FXML file to load.
     * @return The root element of the loaded FXML file.
     * @throws IOException If an error occurs during loading of FXML.
     */
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    /**
     * The main method, launches the JavaFX application.
     *
     * @param args The command-line arguments.
     */
    public static void main(String[] args) {
        launch();
    }

}