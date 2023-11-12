package game;


import environment.BoardPosition;

public abstract class GameElement{
    private BoardPosition position;

    public BoardPosition getPosition() {
        return position;
    }

    public void setPosition(BoardPosition position) {
        this.position = position;
    }
}
