package com.matteoveroni.simplebreak.gui.controller;

import com.matteoveroni.simplebreak.gui.utils.ModificatoreTextField;
import com.matteoveroni.simplebreak.jobs.SimplePomodoroJob;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.knowm.sundial.SundialJobScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.matteoveroni.simplebreak.gui.utils.ModificatoreTextField.TipoTextField.SOLONUMERICA;


public class ControllerTest {

    private static final Logger LOG = LoggerFactory.getLogger(ControllerTest.class);

    @FXML private Button btn_start;
    @FXML private TextField txt_workHours;
    @FXML private TextField txt_workMinutes;
    @FXML private TextField txt_workSeconds;
    @FXML private TextField txt_breakHours;
    @FXML private TextField txt_breakMinutes;
    @FXML private TextField txt_breakSeconds;

    @FXML
    private void initialize() {
        LOG.debug("INITIALIZE");

        ModificatoreTextField.settaRegoleTesto(txt_workHours, SOLONUMERICA, 2, 0, 24);
        ModificatoreTextField.settaRegoleTesto(txt_workMinutes, SOLONUMERICA, 2, 0, 60);
        ModificatoreTextField.settaRegoleTesto(txt_workSeconds, SOLONUMERICA, 2, 0, 60);
        ModificatoreTextField.settaRegoleTesto(txt_breakHours, SOLONUMERICA, 2, 0, 24);
        ModificatoreTextField.settaRegoleTesto(txt_breakMinutes, SOLONUMERICA, 2, 0, 60);
        ModificatoreTextField.settaRegoleTesto(txt_breakSeconds, SOLONUMERICA, 2, 0, 60);

        btn_start.setOnAction(action -> {
            SundialJobScheduler.addJob("WorkJob", SimplePomodoroJob.class);
            Date fiveSecondsAfter = new Date(System.currentTimeMillis() + 10000L);
            LOG.info("now " + LocalDateTime.now());
            int seconds = Integer.valueOf(txt_workSeconds.getText());
            LOG.info("seconds " + seconds);
            int minutes = Integer.valueOf(txt_workMinutes.getText());
            LOG.info("minutes " + minutes);
            int hours = Integer.valueOf(txt_workHours.getText());
            LOG.info("hours " + hours);
            LocalDateTime localDateTimeWork = LocalDateTime.now()
                    .plus(seconds, ChronoUnit.SECONDS)
                    .plus(minutes, ChronoUnit.MINUTES)
                    .plus(hours, ChronoUnit.HOURS);
            LOG.info("localDateTimeWork " + localDateTimeWork);
            Date workTime = Date.from(ZonedDateTime.of(localDateTimeWork, ZoneId.systemDefault()).toInstant());
            LOG.info("workTime " + workTime);
            SundialJobScheduler.addSimpleTrigger("WorkJobTrigger", "WorkJob", 0, 0, workTime, null);
        });
    }

}
