package com.matteoveroni.simplebreak.jobs;

import com.matteoveroni.simplebreak.App;
import com.matteoveroni.simplebreak.gui.controller.ControllerTest;
import org.knowm.sundial.Job;
import org.knowm.sundial.exceptions.JobInterruptException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimplePomodoroJob extends Job {

    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    @Override
    public void doRun() throws JobInterruptException {
        LOG.info("JOB completed");
        ControllerTest.notificaFineLavoro();
    }
}