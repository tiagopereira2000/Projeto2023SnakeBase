package game;

import environment.Board;
import environment.BoardPosition;
import environment.Cell;
import environment.LocalBoard;

import java.io.Serializable;

public class Goal extends GameElement implements Serializable {
	private static Goal instance;
	private int value=1;
	private Board board;
	public static final int MAX_VALUE=10;
	private static Thread goalThread;
	public Goal(Board board2) {
		this.board = board2;
		goalThread = new Thread(() -> {
			try {
				Thread.sleep((MAX_VALUE-1) * Board.PLAYER_PLAY_INTERVAL);
			}catch (InterruptedException ignored){}
			board.terminate();
		});
	}
	
	public int getValue() {
		return value;
	}

	public void incrementValue() {
		//TODO
		value += 1;
	}

	public static Goal getInstance(Board board){
		if(instance == null){
			instance = new Goal(board);
		}
		return instance;
	}

	public void relocateGoal() {
		incrementValue();
		board.addGameElement(this);
		if(value == MAX_VALUE)
			goalThread.start();
	}

}
