package game;

import environment.LocalBoard;

public class ObstacleMover implements Runnable {
	private Obstacle obstacle;
	private LocalBoard board;
	
	public ObstacleMover(Obstacle obstacle, LocalBoard board) {
		super();
		this.obstacle = obstacle;
		this.board = board;
	}

	@Override
	public void run() {
		// TODO
	}
}
