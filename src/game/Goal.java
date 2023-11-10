package game;

import environment.Board;
import environment.BoardPosition;
import environment.Cell;
import environment.LocalBoard;

public class Goal extends GameElement {
	private static Goal instance;
	private int value=1;
	private Board board;
	public static final int MAX_VALUE=10;
	private static Thread goalThread;
	public Goal(Board board2) {
		this.board = board2;
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

	public int captureGoal() {
//		TODO ao ser capturado vai surgir numa nova posição com um novo valor
		int prizeSize = value;

		if(value < MAX_VALUE-1) {
			incrementValue();
			BoardPosition goalPosition = board.getGoalPosition();
			Cell goalCell = board.getCell(goalPosition);
			goalCell.removeGoal();
			board.addGameElement(this);
		} else board.terminate();

		return prizeSize;
	}

}
