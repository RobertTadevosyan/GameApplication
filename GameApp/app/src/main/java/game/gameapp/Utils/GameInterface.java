package game.gameapp.Utils;

public interface GameInterface {
    void gameOverBecauseOfEnemies(float f, float f2, int i, int i2);

    void gameOverBecauseOfOutOfBounds();

    void startGame();

    void timerMethod(double d);
}
