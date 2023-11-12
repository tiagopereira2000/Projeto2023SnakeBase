package environment;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import game.GameElement;
import game.Goal;
import game.Obstacle;
import game.Snake;

/** Main class for game representation.
 * 
 * @author luismota
 *
 */
public class Cell {
	private BoardPosition position;
	private Snake ocuppyingSnake = null;
	private GameElement gameElement=null;
	private ReentrantLock lock = new ReentrantLock();
	private Condition free = lock.newCondition();
	public GameElement getGameElement() {
		return gameElement;
	}


	public Cell(BoardPosition position) {
		super();
		this.position = position;
	}

	public BoardPosition getPosition() {
		return position;
	}

	// passámos de void para boolean para a aceitar/recusar o rquest
	public void request(Snake snake) throws InterruptedException {
		//TODO coordination and mutual exclusion
		lock.lock();
		try{
			while(isOcupied()){
				free.await();
			}
			ocuppyingSnake=snake;
			if (isOcupiedByGoal()){
				ocuppyingSnake.eat(getGoal().captureGoal());
			}


		} finally {
			lock.unlock();
		}
	}

	public boolean initialRequest(Snake snake){
		boolean accepted=false;
		lock.lock();
		if(!isOcupied()){
			ocuppyingSnake=snake;
			accepted = true;
		}
		lock.unlock();
		return accepted;
	}

	public void release() {
		//TODO
		lock.lock();
		try{
			if(isOcupiedBySnake() && ocuppyingSnake.equals(Snake.currentThread())) {
				ocuppyingSnake = null;
				free.signalAll();
			}

		}catch (IllegalMonitorStateException e){
			System.out.println("notifyAll erro");
		}finally {
			lock.unlock();
		}
	}

	public boolean isOcupiedBySnake() {
		return ocuppyingSnake!=null;
	}


	public void setGameElement(GameElement element) {
		//TODO coordination and mutual exclusion
		lock.lock();
		if(!isOcupied() || !isOcupiedByGoal())
			gameElement=element;
		lock.unlock();
	}

	public boolean isOcupied() {
		return isOcupiedBySnake() || isOcupiedByObstacle();
	}


	public Snake getOcuppyingSnake() {
		return ocuppyingSnake;
	}


	public void removeGoal() {
		lock.lock();
		if(isOcupiedByGoal()){
			gameElement = null;
		}
		free.signalAll();
		lock.unlock();
	}

	public void removeObstacle() {
		lock.lock();
		if(isOcupiedByObstacle())
			gameElement = null;
		free.signalAll();
		lock.unlock();
	}


	public Goal getGoal() {
		return (Goal)gameElement;
	}


	public boolean isOcupiedByGoal() {
		return (gameElement!=null && gameElement instanceof Goal);
	}

	public boolean isOcupiedByObstacle(){
		return (gameElement != null && gameElement instanceof Obstacle);
	}
	
	

}
