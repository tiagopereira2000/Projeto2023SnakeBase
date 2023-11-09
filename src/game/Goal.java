package game;

import environment.Board;
import environment.BoardPosition;
import environment.Cell;
import environment.LocalBoard;

public class Goal extends GameElement  {
	private int value=1;
	private Board board;
	public static final int MAX_VALUE=10;
	public Goal(Board board2) {
		this.board = board2;
	}
	
	public int getValue() {
		return value;
	}
	public void incrementValue() throws InterruptedException {
		//TODO
		value += 1;
	}

	public int captureGoal() {
//		TODO ao ser capturado vai surgir numa nova posição com um novo valor
		BoardPosition goalPosition = board.getGoalPosition();
		Cell goalCell = board.getCell(goalPosition);
		//
		goalCell.removeGoal();
		return value;
	}

}
