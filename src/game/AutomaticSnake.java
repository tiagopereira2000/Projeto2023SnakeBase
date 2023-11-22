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

	/**
	 * <h3>run</h3>
	 * the automatic snakes will iterate every 100ms and request for a move, until is interrupted.
	 * There are 2 types of interruption on AutomaticSnakes:
	 * <p>the game was <b>terminated</b> and the Snakes will be terminated as well</p>
	 * <p>the button to <b>handle locked</b> movements</p>
	 *
	 */
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
				if(getBoard().isFinished())
					break;
				System.out.println("Snake interrompida -> reset direção");
				resetMove = true;
			}
		}
	}

	/**
	 * <h3>getNextPosition</h3>
	 * options =
	 * <p>if not Interrupted : { neighbors and (cells not ocupied by the snake) } (1)</p>
	 * <p>if Interrupted : { (1) and (cells not ocupied by obstacles or other snakes) } (2)</p>
	 * <P>Then, from the options left lets take the nearest to the goal (3)</P>
	 * @return	best option to move ({@link BoardPosition})
	 **/
	private BoardPosition getNextPosition(){
		BoardPosition goal = getBoard().getGoalPosition();
		List<BoardPosition> options = getBoard().getNeighboringPositions(cells.getLast());

		options.removeIf(i -> getPath().contains(i)); //(1)
		BoardPosition bestOption = options.get(0); //inicializar com a primeira opção

		if(resetMove){ 	//snake interrupted -> excluir células com obstaculos
			options.removeIf(p -> getBoard().getCell(p).isOcupiedByObstacle()); // (2)
			resetMove = false;
		}
		BoardPosition ini = new BoardPosition(0,0);
		BoardPosition end= new BoardPosition(Board.NUM_COLUMNS, Board.NUM_ROWS);
		double min = ini.distanceTo(end);

		if(!options.isEmpty()){
			for (BoardPosition p: options) {
				double distance = p.distanceTo(goal); // (3)
				if(distance < min){
					min = distance;
					bestOption = p;
				}
			}

		}
		return bestOption;
	}

	
}
