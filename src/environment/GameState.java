package environment;

import game.Snake;

import java.io.Serializable;
import java.util.LinkedList;

public class GameState implements Serializable {
    private final Cell[][] cells;
    private final LinkedList<Snake> snakes;
    private final boolean isFinished;

    public GameState(Cell[][] cells, LinkedList<Snake> snakes, boolean isFinished) {
        this.cells = cells;
        this.snakes = snakes;
        this.isFinished = isFinished;
    }

    public Cell[][] getCells() {
        return cells;
    }

    public LinkedList<Snake> getSnakes() {
        return snakes;
    }

    public boolean isFinished() {
        return isFinished;
    }
}
