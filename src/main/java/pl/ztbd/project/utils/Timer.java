package pl.ztbd.project.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class Timer {
    private long startTime;
    private long pauseTime;
    private long stopTime;
    private StepEnum step;

    private Timer(StepEnum step) {
        startTime = System.currentTimeMillis();
        this.step = step;
    }

    public static Timer start(StepEnum step) {
        return new Timer(step);
    }

    public void pause() {
        pauseTime = System.currentTimeMillis();
    }

    public void play() {
        startTime = startTime + (pauseTime - startTime);
    }

    public void stop() {
        stopTime = System.currentTimeMillis();
        log.info("{}|{}", step.name(), getTime());
    }

    public long getTime() {
        return stopTime - startTime;
    }

}
