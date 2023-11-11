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

/**
 *
 * @author ASUS-Tiago
 * */
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
			Cell nextCell = getBoard().getCell(getNextPosition()); //NextPosition() -> (BP) bestoption -> (Cell) bestoption
			try {
				move(nextCell); // Snake.move()
				Thread.sleep(Board.PLAYER_PLAY_INTERVAL);
			}catch (InterruptedException e){
				/*
				* 2 interrupt types:
				* 	- terminate game() -> Board.isFinished = true -> break: out of while
				* 	- reset snake move() -> resetMove = true -> continue on while
				* */
				if(getBoard().isFinished()){
					break;
				}
				System.out.println("Snake interrompida -> reset direção");
				resetMove = true;
			}
		}
	}

	/**
	 * getNextPosition:
	 * @return	best option to move ({@link BoardPosition})
	 *
	 * options =
	 * 	if not Interrupted : { neighbors and (cells not ocupied by the snake) } (1)
	 * 	if Interrupted : { (1) and (cells not ocupied by obstacles or other snakes) } (2)
	 * Then, from the options left lets take the nearest to the goal (3)
	 **/
	private BoardPosition getNextPosition(){
		BoardPosition goal = getBoard().getGoalPosition();
		List<BoardPosition> options = getBoard().getNeighboringPositions(cells.getLast());
		if(resetMove){ 	//snake interrupted -> excluir células com obstaculos
			options.removeIf(p -> getBoard().getCell(p).isOcupied()); // (2)
			resetMove = false;
		}
		options.removeIf(i -> getPath().contains(i)); //(1)
		double min = 100; // ~= infinite
		BoardPosition bestOption = options.get(0); //initialize bestOption = options[0]
		for (BoardPosition p: options) {
			double distance = p.distanceTo(goal); // (3)
			if(distance < min){
				min = distance;
				bestOption = p;
			}
		}
		return bestOption;
	}

	
}
