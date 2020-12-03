package com.matteoveroni.simplebreak.jobs;

import java.util.Date;
import org.knowm.sundial.Job;
import org.knowm.sundial.exceptions.JobInterruptException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CalcAlarmPercentageJob extends Job {

    private static final Logger LOG = LoggerFactory.getLogger(CalcAlarmPercentageJob.class);

    @Override
    public void doRun() throws JobInterruptException {
        double percentage;
        Date now = new Date();
        long startAlarmTime = getJobContext().get("startAlarmTime");
        long fireAlarmTime = getJobContext().get("fireAlarmTime");
        LOG.info("startAlarmTime => " + startAlarmTime);
        LOG.info("fireAlarmTime => " + fireAlarmTime);

        percentage = calculatePercentage(startAlarmTime, now.getTime(), fireAlarmTime);

        LOG.info("percentage: " + percentage);
//        EventBus.getDefault().postSticky(new EventPercentageUpdate(percentage));
    }

    private double calculatePercentage(long startAlarmTime, long currentTime, long fireAlarmTime) {
        LOG.info("-----------------------------------------------------------------------------------------------");
        LOG.info("startTime " + startAlarmTime);
        LOG.info("currentTime " + currentTime);
        LOG.info("fireAlarmTime " + fireAlarmTime);
        long timeBetweenStartAndEnd = fireAlarmTime - startAlarmTime;
        LOG.info("timeBetweenStartAndEnd " + timeBetweenStartAndEnd);
        long timeBetweenStartAndToday = currentTime - startAlarmTime;
        LOG.info("timeBetweenStartAndToday " + timeBetweenStartAndToday);

        long percentage = Math.round(((double) timeBetweenStartAndToday / (double) timeBetweenStartAndEnd) * 100);
//        double percentage = ((double) timeBetweenStartAndToday / (double) timeBetweenStartAndEnd) * 100;
        LOG.info("percentage " + percentage);
        LOG.info("-----------------------------------------------------------------------------------------------");
        return percentage;
    }

//    public void setStartAlarmTime(Date startAlarmTime) {
//        this.startAlarmTime = startAlarmTime;
//    }
//
//    public void setFireAlarmTime(Date fireAlarmTime) {
//        this.fireAlarmTime = fireAlarmTime;
//    }
//
//    private double calculatePercentage(Date startAlarmTime, Date currentTime, Date fireAlarmTime) {
//        long timeBetweenStartAndEnd = fireAlarmTime.getTime() - startAlarmTime.getTime();
//        long timeBetweenStartAndToday = currentTime.getTime() - startAlarmTime.getTime();
//        return Math.round(((double) timeBetweenStartAndToday / (double) timeBetweenStartAndEnd) * 100);
//    }
}
