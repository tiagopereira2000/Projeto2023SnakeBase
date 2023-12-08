package game;


import environment.BoardPosition;

import java.io.Serializable;

public abstract class GameElement implements Serializable {
    private BoardPosition position;

    public BoardPosition getPosition() {
        return position;
    }

    public void setPosition(BoardPosition position) {
        this.position = position;
    }
}
