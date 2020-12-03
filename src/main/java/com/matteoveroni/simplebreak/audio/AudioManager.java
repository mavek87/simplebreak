package com.matteoveroni.simplebreak.audio;

import javafx.scene.media.AudioClip;

public class AudioManager {

    private static final String PATH_AUDIO_FOLDER = "com/matteoveroni/simplebreak/audio/";
    private static final String FILE_NAME_ALARM_1 = "alarm.mp3";
    private static final String FILE_NAME_ALARM_2 = "alarm2.wav";

    private final ClassLoader classLoader;

    public enum Sound {
        ALARM1(FILE_NAME_ALARM_1),
        ALARM2(FILE_NAME_ALARM_2);

        private final String fileName;

        Sound(String fileName) {
            this.fileName = fileName;
        }

        public String getPath() {
            return PATH_AUDIO_FOLDER + fileName;
        }
    }

    public AudioManager() {
        this.classLoader = getClass().getClassLoader();
    }

    // TODO: to improve use a cache
    public void playSound(Sound sound) {
        new AudioClip(getAbsolutePath(sound)).play();
    }

    private String getAbsolutePath(Sound sound) {
        return classLoader.getResource(sound.getPath()).toString();
    }
}
