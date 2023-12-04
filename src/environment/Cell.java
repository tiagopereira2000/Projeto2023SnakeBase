package environment;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import game.*;

/** Main class for game representation.
 * 
 * @author luismota
 *
 */
public class Cell implements Serializable {
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

	public boolean request(Snake snake) throws InterruptedException {
		//TODO coordination and mutual exclusion
		lock.lock();

		if(snake instanceof HumanSnake && isOcupied()){
			lock.unlock();
			return false;
		}else{
			while(isOcupied()){
				free.await();
			}
			ocuppyingSnake=snake;
			snake.addCell(this);
			if (isOcupiedByGoal()){
				Goal goal = getGoal();
				ocuppyingSnake.eat(goal.getValue());
				gameElement = null;
				goal.relocateGoal();
			}
		}
		lock.unlock();
		return true;
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

	public void release(Snake snake) {
		lock.lock();
		try{
			if(isOcupiedBySnake() && ocuppyingSnake.equals(snake)) {
				snake.removeFirstCell();
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


	public boolean setGameElement(GameElement element) {
		lock.lock();
		if(!isOcupied() && !isOcupiedByGoal()) {
			element.setPosition(position);
			gameElement = element;
			lock.unlock();
			return true;
		}
		lock.unlock();
		return false;
	}

	public boolean isOcupied() {
		return isOcupiedBySnake() || isOcupiedByObstacle();
	}


	public Snake getOcuppyingSnake() {
		return ocuppyingSnake;
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
