package game;

import environment.LocalBoard;

public class ObstacleMover extends Thread {
	private Obstacle obstacle;
	private LocalBoard board;
	
	public ObstacleMover(Obstacle obstacle, LocalBoard board) {
		super();
		this.obstacle = obstacle;
		this.board = board;
	}

	@Override
	public void run(){
		while(obstacle.hasRemainingMoves()){
			//mover obstaculo movimento random
			board.moveObstacle(obstacle);
			obstacle.decrementRemaingMoves();
			board.setChanged();
			try{
				Thread.sleep(Obstacle.OBSTACLE_MOVE_INTERVAL);
			}catch (InterruptedException e){
				break;
			}
		}
	}
}
