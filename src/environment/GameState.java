package environment;

import game.Snake;

import java.io.Serializable;
import java.util.LinkedList;

public class GameState implements Serializable {
    private Cell[][] cells;
    private LinkedList<Snake> snakes = new LinkedList<>();
    private boolean isFinished = false;

    public GameState(Cell[][] cells, LinkedList<Snake> snakes, boolean isFinished) {
        this.cells = cells;
        this.snakes = snakes;
        this.isFinished = isFinished;
    }

    public Cell[][] getCells() {
        return cells;
    }

    public void setCells(Cell[][] cells) {
        this.cells = cells;
    }


    public LinkedList<Snake> getSnakes() {
        return snakes;
    }

    public void setSnakes(LinkedList<Snake> snakes) {
        this.snakes = snakes;
    }

    public boolean isFinished() {
        return isFinished;
    }
}
