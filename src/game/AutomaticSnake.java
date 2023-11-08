package game;

import java.util.LinkedList;
import java.util.List;

import javax.swing.text.Position;

import environment.LocalBoard;
import gui.SnakeGui;
import environment.Cell;
import environment.Board;
import environment.BoardPosition;

public class AutomaticSnake extends Snake {
	public AutomaticSnake(int id, LocalBoard board) {
		super(id,board);

	}

	@Override
	public void run() {
		doInitialPositioning();
		System.err.println("initial size:"+cells.size());
//		try {
//			cells.getLast().request(this);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}

		//TODO: automatic movement
		while(true){


			// buscar ultima posição das celulas para realizar movimento
			Cell nextCell = getBoard().getCell(getNextPosition());
			try {
				move(nextCell);
				Thread.sleep(Board.PLAYER_PLAY_INTERVAL);
			}catch (InterruptedException e){
				System.out.println("Snake interrompida");
				break; // fora do while
			}
		}
	}

	private BoardPosition getNextPosition(){
		// fazer movimentação para o premio
		BoardPosition goal = getBoard().getGoalPosition();
		List<BoardPosition> options = getBoard().getNeighboringPositions(cells.getLast());
		double min = 30;
		BoardPosition bestOption = options.get(0); //inicializar com a primeira opção
		for (BoardPosition p: options) {
			double distance = p.distanceTo(goal);
			if(distance < min){
				min = distance;
				bestOption = p;
			}
		}
		return bestOption;
	}

	
}
