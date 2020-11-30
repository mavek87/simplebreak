package com.matteoveroni.simplebreak.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerProva {

    private static final Logger LOG = LoggerFactory.getLogger(ControllerProva.class);

    @FXML private Button btn_elimina;
    @FXML private Button btn_modifica;
    @FXML private Button btn_aggiungi;

    @FXML
    private void initialize() {
        LOG.debug("INITIALIZE");
    }

}
