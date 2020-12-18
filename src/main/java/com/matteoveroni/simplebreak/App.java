package com.matteoveroni.simplebreak;

import com.matteoveroni.simplebreak.gui.controller.ControllerTest;
import com.matteoveroni.simplebreak.jobs.SimplePomodoroJob;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.knowm.sundial.SundialJobScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public final class App extends Application {

    private static final Logger LOG = LoggerFactory.getLogger(App.class);
    private static final String APP_NAME = "SimpleBreak";
    private static final String RESOURCE_FOLDER_STRUCTURE_BASE = "com/matteoveroni/simplebreak";
    private static final FXMLLoader FXML_LOADER = new FXMLLoader();
    private static final String MAIN_VIEW = RESOURCE_FOLDER_STRUCTURE_BASE + "/fxml/view_test.fxml";

    private ControllerTest controllerTest;

    public static void main(String... args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        super.init();
        SundialJobScheduler.startScheduler();
    }

    @Override
    public void start(Stage stage) {
        LOG.info("App '" + APP_NAME + "' started!");
        
        loadMainScene(stage);

        SundialJobScheduler.addJob("WorkJob", SimplePomodoroJob.class);
    }

    public final void loadMainScene(Stage stage) {
        try (InputStream inputStream = App.class.getClassLoader().getResourceAsStream(MAIN_VIEW)) {
            Parent root = FXML_LOADER.load(inputStream);
            controllerTest = FXML_LOADER.getController();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setWidth(800);
            stage.setHeight(600);
            stage.setTitle(APP_NAME);
            stage.show();
        } catch (IOException e) {
            throw new IllegalStateException("Cannot load FXML '" + MAIN_VIEW + "'", e);
        }
    }

    @Override
    public void stop() throws Exception {
        SundialJobScheduler.shutdown();
        controllerTest.dispose();
        super.stop();
    }
}
