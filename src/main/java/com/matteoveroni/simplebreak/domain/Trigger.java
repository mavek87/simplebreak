package com.matteoveroni.simplebreak.domain;

import java.util.Date;

public class Trigger {

    private final String triggerName;
    private final String jobName;
    private final int repeatCount;
    private final int repeatInterval;
    private final Date startTime;
    private final Date endTime;

    public Trigger(String triggerName, String jobName, int repeatCount, int repeatInterval, Date startTime, Date endTime) {
        this.triggerName = triggerName;
        this.jobName = jobName;
        this.repeatCount = repeatCount;
        this.repeatInterval = repeatInterval;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public String getJobName() {
        return jobName;
    }

    public int getRepeatCount() {
        return repeatCount;
    }

    public int getRepeatInterval() {
        return repeatInterval;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }
}
