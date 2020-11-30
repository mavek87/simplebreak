package com.matteoveroni.simplebreak.gui.controller;

import com.matteoveroni.simplebreak.gui.utils.ModificatoreTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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
    }

}
