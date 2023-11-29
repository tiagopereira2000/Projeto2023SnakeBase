package game;


import environment.BoardPosition;

public abstract class GameElement{
    private BoardPosition position;

    public BoardPosition getPosition() {
        return position;
    }

//    public boolean isSettedUp(){
//        return getPosition() != null;
//    }

    public void setPosition(BoardPosition position) {
        this.position = position;
    }
}
