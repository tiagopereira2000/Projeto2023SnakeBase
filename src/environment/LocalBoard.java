package environment;

import game.*;

/** Class representing the state of a game running locally
 * 
 * @author luismota
 *
 */
public class LocalBoard extends Board{
	
	private static final int NUM_SNAKES = 5;
	private static final int NUM_OBSTACLES = 20;
	private static final int NUM_SIMULTANEOUS_MOVING_OBSTACLES = 3;
	private static ThreadPool pool = new ThreadPool(NUM_SIMULTANEOUS_MOVING_OBSTACLES);
	

	public LocalBoard() {
		for (int i = 0; i < NUM_SNAKES; i++) {
			AutomaticSnake snake = new AutomaticSnake(i, this);
			snakes.add(snake);
		}
		addObstacles(NUM_OBSTACLES);
		for(Obstacle o: getObstacles()){
			ObstacleMover obsMov = new ObstacleMover(o,this);
			pool.submit(obsMov);
		}

		Goal goal = addGoal();
	}

	public void init() {
		for(Snake s:snakes)
			s.start();
		pool.execute();
		setChanged();
	}

	

	@Override
	public void handleKeyPress(int keyCode) {
		// do nothing... No keys relevant in local game
	}

	@Override
	public void handleKeyRelease() {
		// do nothing... No keys relevant in local game
	}

	@Override
	public void terminate() {
		super.terminate();
		pool.shutdownNow();
	}
}
