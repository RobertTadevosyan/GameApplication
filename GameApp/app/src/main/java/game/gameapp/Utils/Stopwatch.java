package game.gameapp.Utils;

public class Stopwatch {
    private long start;

    public Stopwatch() {
        this.start = System.currentTimeMillis();
    }

    public void restart() {
        this.start = System.currentTimeMillis();
    }

    public double elapsedTime() {
        return ((double) (System.currentTimeMillis() - this.start)) / 1000.0d;
    }
}
