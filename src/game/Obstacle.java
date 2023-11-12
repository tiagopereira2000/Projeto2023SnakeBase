package game;

import environment.Board;

public class Obstacle extends GameElement {
	
	
	private static final int NUM_MOVES=3;
	public static final int OBSTACLE_MOVE_INTERVAL = 400;
	private int remainingMoves=NUM_MOVES;
	private Board board;
	public Obstacle(Board board) {
		super();
		this.board = board;
	}
	
	public int getRemainingMoves() {
		return remainingMoves;
	}

	public void decrementRemaingMoves(){
		remainingMoves -= 1;
	}
	public boolean hasRemainingMoves(){
		return remainingMoves > 0;
	}


}
