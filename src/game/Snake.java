package game;

import java.io.Serializable;
import java.util.LinkedList;

import environment.LocalBoard;
import gui.SnakeGui;
import environment.Board;
import environment.BoardPosition;
import environment.Cell;
/** Base class for representing Snakes.
 * Will be extended by HumanSnake and AutomaticSnake.
 * Common methods will be defined here.
 * @author luismota
 *
 */
public abstract class Snake extends Thread implements Serializable{
	private static final int DELTA_SIZE = 10;
	protected LinkedList<Cell> cells = new LinkedList<Cell>();	// células ocupadas pela snake
	protected int size = 5;
	private int id;
	private Board board;
	
	public Snake(int id,Board board) {
		this.id = id;
		this.board=board;
	}

	public int getSize() {
		return size;
	}

	public int getIdentification() {
		return id;
	}

	public int getLength() {
		return cells.size();
	}
	
	public LinkedList<Cell> getCells() {
		return cells;
	}
	protected void move(Cell cell) throws InterruptedException {
		// TODO
	}
	
	public LinkedList<BoardPosition> getPath() {
		LinkedList<BoardPosition> coordinates = new LinkedList<>();
		for (Cell cell : cells) {
			coordinates.add(cell.getPosition());
		}

		return coordinates;
	}	
	protected void doInitialPositioning() {
		// Random position on the first column. 
		// At startup, snake occupies a single cell
		BoardPosition at = new BoardPosition(0,0);
		int posX = 0;
		boolean done = false;

		while (!done){
			int posY = (int) (Math.random() * Board.NUM_ROWS);
			at = new BoardPosition(posX, posY);
			if(board.getCell(at).initialRequest(this)){
				done = true;
			}
		}

//		try {
//			board.getCell(at).initialRequest(this);
//		} catch (InterruptedException e1) {
//			e1.printStackTrace();
//		}

		cells.add(board.getCell(at));
		System.err.println("Snake "+getIdentification()+" starting at:"+getCells().getLast());		
	}
	
	public Board getBoard() {
		return board;
	}
	
	
}
