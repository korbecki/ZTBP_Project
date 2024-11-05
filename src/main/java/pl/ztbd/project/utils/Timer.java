package pl.ztbd.project.utils;

public class Timer {
    private long startTime;
    private long pauseTime;

    private Timer() {
        startTime = System.currentTimeMillis();
    }

    public static Timer start() {
        return new Timer();
    }

    public void pause() {
        pauseTime = System.currentTimeMillis();
    }

    public void play() {
        startTime = startTime + (pauseTime - startTime);
    }

    public long getTime() {
        return System.currentTimeMillis() - startTime;
    }
}
