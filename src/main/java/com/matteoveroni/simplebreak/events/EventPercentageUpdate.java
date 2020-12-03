package com.matteoveroni.simplebreak.events;

public class EventPercentageUpdate {

    private final double percentage;

    public EventPercentageUpdate(double percentage) {
        this.percentage = percentage;
    }

    public double getPercentage() {
        return percentage;
    }
}
