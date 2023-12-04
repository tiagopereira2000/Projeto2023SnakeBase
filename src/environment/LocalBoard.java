package environment;

import game.*;

import java.io.Serializable;

/** Class representing the state of a game running locally
 * 
 * @author luismota
 *
 */
public class LocalBoard extends Board {
	
	public static final int NUM_SNAKES = 5;
	private static final int NUM_OBSTACLES = 20;
	private static final int NUM_SIMULTANEOUS_MOVING_OBSTACLES = 3;
	private static ThreadPool pool = new ThreadPool(NUM_SIMULTANEOUS_MOVING_OBSTACLES);


	public LocalBoard() {
		//threadpool já instanciada. sendo estática
 		createSnakes();
		addObstacles(NUM_OBSTACLES);
		submitObstacleMovers();
		addGoal();
	}

	public void init() {
		for(Snake s:snakes)
			s.start();
		pool.execute();
		setChanged();
	}

	public void createSnakes(){
		for (int i = 0; i < NUM_SNAKES; i++) {
			AutomaticSnake snake = new AutomaticSnake(i, this);
			addSnake(snake);
		}
	}

	public void submitObstacleMovers(){
		for(Obstacle o: getObstacles()){
			ObstacleMover obsMov = new ObstacleMover(o,this);
			pool.submit(obsMov);
		}
	}

	@Override
	public void handleKeyPress(int keyCode) {
	}

	@Override
	public void handleKeyRelease() {
		// do nothing... No keys relevant in local game
	}

	/**
	 * Passa a Board.isFinished para true e interrompe todas as snakes,
	 * começando pelas {@link AutomaticSnake} e depois interrompe
	 */
	@Override
	public void terminate() {
		super.terminate();
		pool.shutdownNow();
	}
}
