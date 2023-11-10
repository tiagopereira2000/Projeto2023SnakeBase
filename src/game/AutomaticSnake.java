package game;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.text.Position;

import environment.LocalBoard;
import gui.SnakeGui;
import environment.Cell;
import environment.Board;
import environment.BoardPosition;

public class AutomaticSnake extends Snake {
	protected boolean resetMove = false; //se a snake tiver sido interrompida para adotar outro caminho
	public AutomaticSnake(int id, LocalBoard board) {
		super(id,board);

	}

	@Override
	public void run() {
		doInitialPositioning();
		System.err.println("initial size:"+cells.size());

		//TODO: automatic movement
		while(true){
			// buscar ultima posição das celulas para realizar movimento
			Cell nextCell = getBoard().getCell(getNextPosition());
			try {
				move(nextCell);
				Thread.sleep(Board.PLAYER_PLAY_INTERVAL);
			}catch (InterruptedException e){
				if(getBoard().isFinished()){
					break;
				}
				System.out.println("Snake interrompida -> reset direção");
				resetMove = true;
			}
		}
	}

	private BoardPosition getNextPosition(){
		// fazer movimentação para o premio
		BoardPosition goal = getBoard().getGoalPosition();
		List<BoardPosition> options = getBoard().getNeighboringPositions(cells.getLast());
		if(resetMove){ 	//snake interrupted -> excluir células com obstaculos
			options.removeIf(p -> getBoard().getCell(p).isOcupied());
			resetMove = false;
		}
		options.removeIf(i -> getPath().contains(i)); // options - getPath()
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
