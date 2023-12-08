package remote;

import environment.Board;
import environment.Cell;
import game.HumanSnake;
import game.Snake;

import java.awt.*;

/** Remote representation of the game, no local threads involved.
 * Game state will be changed when updated info is received from Srver.
 * Only for part II of the project.
 * @author luismota
 *
 */
public class RemoteBoard extends Board{
	private Client client;
	public RemoteBoard(Client client) {
		this.client = client;
	}


	@Override
	public void handleKeyPress(int keyCode) {
		//O cliente vai pressionar uma tecla e vai enviá-la para o servidor realizar o seu movimento
		client.sendIntruction(keyCode);
	}

	@Override
	public void handleKeyRelease() {
		// em princípio vai ser ignorado
	}

	@Override
	public void addSnake(Snake snake) {

	}

	@Override
	public void wakeLazySnakes() {

	}

	@Override
	public void interruptSnakes() {

	}

	@Override
	public void init() {
		// faz um pedido de inserção do player na board do servidor
		client.requestSnake();
	}


	

}
