package remote;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.LinkedList;

import environment.LocalBoard;
import environment.Board;
import environment.BoardPosition;
import environment.Cell;
import game.Goal;
import game.Obstacle;
import game.Snake;

/** Remote representation of the game, no local threads involved.
 * Game state will be changed when updated info is received from Srver.
 * Only for part II of the project.
 * @author luismota
 *
 */
public class RemoteBoard extends Board{
	public RemoteBoard() {

	}

	@Override
	public void handleKeyPress(int keyCode) {
		//TODO O cliente vai pressionar uma tecla e vai enviá-la para o servidor realizar o seu movimento
	}

	@Override
	public void handleKeyRelease() {
		// TODO em princípio vai ser ignorado
	}

	@Override
	public void init() {
		// TODO faz um pedido de inserção do player na board do servidor
		// TODO recebe do cliente a board
	}


	

}
