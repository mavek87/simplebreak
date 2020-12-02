package com.matteoveroni.simplebreak.gui.controller;

import com.matteoveroni.simplebreak.events.EventEndPomodoroJob;
import com.matteoveroni.simplebreak.gui.utils.ModificatoreTextField;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.knowm.sundial.SundialJobScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.matteoveroni.simplebreak.gui.utils.ModificatoreTextField.TipoTextField.SOLONUMERICA;

public class ControllerTest {

    private static final Logger LOG = LoggerFactory.getLogger(ControllerTest.class);

    @FXML private Label lbl_title;
    @FXML private Button btn_start;
    @FXML private Button btn_stop;
    @FXML private TextField txt_workHours;
    @FXML private TextField txt_workMinutes;
    @FXML private TextField txt_workSeconds;
    @FXML private TextField txt_breakHours;
    @FXML private TextField txt_breakMinutes;
    @FXML private TextField txt_breakSeconds;

    @FXML
    private void initialize() {
        LOG.debug("INITIALIZE");
        EventBus.getDefault().register(this);

        btn_start.setOnAction(click -> {
            onButtonStartClicked();
        });
        btn_stop.setDisable(true);

        ModificatoreTextField.settaRegoleTesto(txt_workHours, SOLONUMERICA, 2, 0, 24);
        ModificatoreTextField.settaRegoleTesto(txt_workMinutes, SOLONUMERICA, 2, 0, 60);
        ModificatoreTextField.settaRegoleTesto(txt_workSeconds, SOLONUMERICA, 2, 0, 60);
        ModificatoreTextField.settaRegoleTesto(txt_breakHours, SOLONUMERICA, 2, 0, 24);
        ModificatoreTextField.settaRegoleTesto(txt_breakMinutes, SOLONUMERICA, 2, 0, 60);
        ModificatoreTextField.settaRegoleTesto(txt_breakSeconds, SOLONUMERICA, 2, 0, 60);
    }

    public void dispose() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onMessageEvent(EventEndPomodoroJob event) {
        Platform.runLater(() -> {
            btn_start.setDisable(false);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Do a break");
            alert.setContentText("The time for work is over!!!");

            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.setIconified(false);

            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.isPresent() && buttonType.get().equals(ButtonType.OK)) {
                LOG.info("button pressed");
            } else {
                LOG.info("button not pressed");
            }
        });
    }

    private void onButtonStartClicked() {
        Stage stage = (Stage) btn_start.getScene().getWindow();
        stage.setIconified(true);

        JobData workData = new JobData(txt_workSeconds, txt_workMinutes, txt_workHours);
        LocalDateTime localDateTimeWork = LocalDateTime.now()
                .plus(workData.getSeconds(), ChronoUnit.SECONDS)
                .plus(workData.getMinutes(), ChronoUnit.MINUTES)
                .plus(workData.getHours(), ChronoUnit.HOURS);
        LOG.info("localDateTimeWork " + localDateTimeWork);
        Date workTime = Date.from(ZonedDateTime.of(localDateTimeWork, ZoneId.systemDefault()).toInstant());
        LOG.info("workTime " + workTime);
        SundialJobScheduler.addSimpleTrigger("WorkJobTrigger", "WorkJob", 0, 0, workTime, null);

        btn_start.setDisable(true);
    }

    private class JobData {
        private final int seconds;
        private final int minutes;
        private final int hours;

        public JobData(TextField txt_seconds, TextField txt_minutes, TextField txt_hours) {
            this(txt_seconds.getText(), txt_minutes.getText(), txt_hours.getText());
        }

        public JobData(String seconds, String minutes, String hours) {
            this.seconds = convertNumericStringToInteger(seconds);
            this.minutes = convertNumericStringToInteger(minutes);
            this.hours = convertNumericStringToInteger(hours);
        }

        public int getSeconds() {
            return seconds;
        }

        public int getMinutes() {
            return minutes;
        }

        public int getHours() {
            return hours;
        }

        private int convertNumericStringToInteger(String numericString) {
            try {
                return Integer.parseInt(numericString);
            } catch (NumberFormatException ex) {
                return 0;
            }
        }
    }

}
