package com.matteoveroni.simplebreak.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ControllerTest {

    private static final Logger LOG = LoggerFactory.getLogger(ControllerTest.class);

    @FXML private BorderPane test;

    @FXML private TextField hours;

    @FXML
    private void initialize() {
        LOG.debug("INITIALIZE");
    }

}
