package environment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

import game.*;
//import jdk.vm.ci.meta.Local;

public abstract class Board extends Observable implements Serializable{
	protected Cell[][] cells;
	private BoardPosition goalPosition;
	public static final long PLAYER_PLAY_INTERVAL = 200;
	public static final long REMOTE_REFRESH_INTERVAL = 200;
	public static final int NUM_COLUMNS = 30;
	public static final int NUM_ROWS = 30;
	protected LinkedList<Snake> snakes = new LinkedList<>();
	private LinkedList<Obstacle> obstacles = new LinkedList<>();
	protected boolean isFinished = false;

	public Cell[][] getCells() {
		return cells;
	}

	public Board() {
		cells = new Cell[NUM_COLUMNS][NUM_ROWS];
		for (int x = 0; x < NUM_COLUMNS; x++) {
			for (int y = 0; y < NUM_ROWS; y++) {
				cells[x][y] = new Cell(new BoardPosition(x, y));
			}
		}

	}

	public boolean isFinished() {
		return isFinished;
	}

	public Cell getCell(BoardPosition cellCoord) {
		return cells[cellCoord.x][cellCoord.y];
	}

	public boolean isOutOfBounds(BoardPosition p){
		if (p.x >= NUM_COLUMNS || p.x < 0 || p.y >= NUM_ROWS || p.y < 0)
			return true;
		return false;
	}

	protected BoardPosition getRandomPosition() {
		return new BoardPosition((int) (Math.random() *NUM_COLUMNS),(int) (Math.random() * NUM_ROWS));
	}

	public BoardPosition getGoalPosition() {
		return goalPosition;
	}

	public void setGoalPosition(BoardPosition goalPosition) {
		this.goalPosition = goalPosition;
	}
	
	public void addGameElement(GameElement gameElement) {
		boolean placed=false;
		while(!placed) {
			BoardPosition pos = getRandomPosition();
			if(getCell(pos).setGameElement(gameElement)) {
				if(gameElement instanceof Goal) {
					setGoalPosition(pos);
				}
				placed=true;
			}
		}
	}

	/**
	 * Implementado da mesma forma que addGameElement.
	 * Não tendo em conta elementos do tipo Goal.
	 * @param obstacle
	 */
	public void moveObstacle(Obstacle obstacle){
		boolean placed=false;
		while(!placed) {
			BoardPosition pos=getRandomPosition();
			if(getCell(pos).setGameElement(obstacle)) {
					placed=true;
			}
		}
	}

	public List<BoardPosition> getNeighboringPositions(Cell cell) {
		ArrayList<BoardPosition> possibleCells=new ArrayList<BoardPosition>();
		BoardPosition pos=cell.getPosition();
		if(pos.x>0)
			possibleCells.add(pos.getCellLeft());
		if(pos.x<NUM_COLUMNS-1)
			possibleCells.add(pos.getCellRight());
		if(pos.y>0)
			possibleCells.add(pos.getCellAbove());
		if(pos.y<NUM_ROWS-1)
			possibleCells.add(pos.getCellBelow());
		return possibleCells;
	}


	protected Goal addGoal() {
		Goal goal= Goal.getInstance(this); // uma só instância do goal
		addGameElement(goal);
		return goal;
	}

	protected void addObstacles(int numberObstacles) {
		// clear obstacle list , necessary when resetting obstacles.
		obstacles.clear();
		while(numberObstacles>0) {
			Obstacle obs=new Obstacle(this);
			addGameElement(obs);
			obstacles.add(obs);
			numberObstacles--;
		}
	}
	
	public LinkedList<Snake> getSnakes() {
		return snakes;
	}


	@Override
	public void setChanged() {
		super.setChanged();
		notifyObservers();
	}


	public LinkedList<Obstacle> getObstacles() {
		return obstacles;
	}

	public void setSnakes(LinkedList<Snake> snakes) {
		this.snakes = snakes;
	}

	public void setCells(Cell[][] cells) {
		this.cells = cells;
	}

	/**
	 * métodos abstract implementados em {@link LocalBoard}.
	 */
	public abstract void init(); 
	
	public abstract void handleKeyPress(int keyCode);

	public abstract void handleKeyRelease();
	

	public void addSnake(Snake snake) {
		snakes.add(snake);
	}

	/**
	 * Método vai ser chamado após clique no botão "reset snakes".
	 * Interruperá todas as {@link AutomaticSnake}.
	 * return: void
	 */
	public void wakeLazySnakes()  {
		for (Snake s: snakes) {
			if(s instanceof AutomaticSnake)
				s.interrupt();
		}
	}

	public void interruptSnakes(){
		for (Snake s:
			 snakes) {
			s.interrupt();
		}
	}

	public void terminate() {
		isFinished=true;
		interruptSnakes();
	}
}