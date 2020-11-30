package com.matteoveroni.simplebreak;

import java.io.IOException;
import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class App extends Application {

    private static final Logger LOG = LoggerFactory.getLogger(App.class);
    private static final String APP_NAME = "SimpleBreak";
    private static final String RESOURCE_FOLDER_STRUCTURE_BASE = "com/matteoveroni/simplebreak";

    public static final void main(String... args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        LOG.info("App \'" + APP_NAME + "\' started!");
        FXMLLoader loader = new FXMLLoader();
        String scenePath = RESOURCE_FOLDER_STRUCTURE_BASE + "/fxml/view_test.fxml";
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(scenePath)) {
            Parent root = loader.load(inputStream);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setWidth(800);
            stage.setHeight(600);
            stage.setTitle(APP_NAME);
            stage.show();
        } catch (IOException e) {
            throw new IllegalStateException("Cannot load FXML \'" + scenePath + "\'", e);
        }
    }
}
