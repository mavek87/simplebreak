package com.matteoveroni.simplebreak.gui.controller;

import com.matteoveroni.simplebreak.audio.AudioManager;
import com.matteoveroni.simplebreak.domain.Trigger;
import com.matteoveroni.simplebreak.events.EventEndPomodoroJob;
import com.matteoveroni.simplebreak.gui.utils.ModificatoreTextField;
import com.matteoveroni.simplebreak.jobs.CalcAlarmPercentageJob;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.knowm.sundial.Job;
import org.knowm.sundial.SundialJobScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.matteoveroni.simplebreak.audio.AudioManager.Sound;
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

    private final Map<String, Job> jobMap = new HashMap<>();
    private final Map<String, Object> triggerMap = new HashMap<>();

    private final AudioManager audioManager = new AudioManager();

    private Alert alert;

    @FXML
    private void initialize() {
        LOG.debug("INITIALIZE");
        ModificatoreTextField.settaRegoleTesto(txt_workHours, SOLONUMERICA, 2, 0, 24);
        ModificatoreTextField.settaRegoleTesto(txt_workMinutes, SOLONUMERICA, 2, 0, 60);
        ModificatoreTextField.settaRegoleTesto(txt_workSeconds, SOLONUMERICA, 2, 0, 60);
        ModificatoreTextField.settaRegoleTesto(txt_breakHours, SOLONUMERICA, 2, 0, 24);
        ModificatoreTextField.settaRegoleTesto(txt_breakMinutes, SOLONUMERICA, 2, 0, 60);
        ModificatoreTextField.settaRegoleTesto(txt_breakSeconds, SOLONUMERICA, 2, 0, 60);

        EventBus.getDefault().register(this);

        btn_start.setOnAction(this::onButtonStartClicked);
        btn_stop.setOnAction(this::onButtonStopClicked);
        btn_stop.disableProperty().bind(btn_start.disableProperty().not());
    }

    public void dispose() {
        clearAllTriggers();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(EventEndPomodoroJob event) {
        Platform.runLater(() -> {
            audioManager.playSound(Sound.ALARM1);
            SundialJobScheduler.removeTrigger("CalcAlarmPercentageTrigger");
            Stage stage = (Stage) btn_start.getScene().getWindow();
            alert.close();
            btn_start.setDisable(false);
            stage.setIconified(false);
        });
    }

    private void onButtonStartClicked(ActionEvent event) {
        Stage stage = (Stage) btn_start.getScene().getWindow();
        stage.setIconified(true);

        ViewInputAdapter viewInputAdapter = new ViewInputAdapter(txt_workSeconds, txt_workMinutes, txt_workHours);
        LocalDateTime localDateTimeWork = LocalDateTime.now()
                .plus(viewInputAdapter.getSeconds(), ChronoUnit.SECONDS)
                .plus(viewInputAdapter.getMinutes(), ChronoUnit.MINUTES)
                .plus(viewInputAdapter.getHours(), ChronoUnit.HOURS);

        Date workTime = Date.from(ZonedDateTime.of(localDateTimeWork, ZoneId.systemDefault()).toInstant());

        Trigger workTrigger = new Trigger("WorkJobTrigger", "WorkJob", 0, 0, workTime, null);
        SundialJobScheduler.addSimpleTrigger(workTrigger.getTriggerName(), workTrigger.getJobName(), workTrigger.getRepeatCount(), workTrigger.getRepeatInterval(), workTrigger.getStartTime(), workTrigger.getEndTime());
        triggerMap.put(workTrigger.getTriggerName(), workTrigger);

        HashMap<String, Object> props = new HashMap<>();
        props.put("startAlarmTime", new Date().getTime());
        props.put("fireAlarmTime", workTime.getTime());
        SundialJobScheduler.addJob("CalcAlarmPercentageJob", CalcAlarmPercentageJob.class, props, false);
        Trigger percentageTrigger = new Trigger("CalcAlarmPercentageTrigger", "CalcAlarmPercentageJob", -1, 1000, null, null);
        SundialJobScheduler.addSimpleTrigger(percentageTrigger.getTriggerName(), percentageTrigger.getJobName(), percentageTrigger.getRepeatCount(), percentageTrigger.getRepeatInterval(), percentageTrigger.getStartTime(), percentageTrigger.getEndTime());
        triggerMap.put(percentageTrigger.getTriggerName(), percentageTrigger);

        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Do a break");
        alert.setContentText("The time for work is over!!!");
        alert.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);

//        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.isPresent() && buttonType.get().equals(ButtonType.OK)) {
            LOG.info("button pressed");
        } else {
            LOG.info("button not pressed");
        }

    }

    private void onButtonStopClicked(ActionEvent event) {

    }

    private void clearAllTriggers() {
        triggerMap.keySet().forEach(SundialJobScheduler::removeTrigger);
        triggerMap.clear();
    }

    private class ViewInputAdapter {
        private final int seconds;
        private final int minutes;
        private final int hours;

        public ViewInputAdapter(TextField txt_seconds, TextField txt_minutes, TextField txt_hours) {
            this(txt_seconds.getText(), txt_minutes.getText(), txt_hours.getText());
        }

        public ViewInputAdapter(String seconds, String minutes, String hours) {
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
