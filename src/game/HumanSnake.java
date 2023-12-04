package game;

import environment.Board;
import environment.BoardPosition;
import environment.Cell;
import remote.Server;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.Serializable;

/** Class for a remote snake, controlled by a human
  * 
  * @author luismota
  *
  */
public class HumanSnake extends Snake implements Serializable {
	private int nextMoveCode = 0; //default
	public HumanSnake(int id, Board board) {
		super(id,board);
	}

	/**
	 * Tarefa da thread de {@link HumanSnake}
	 * Esta thread estará a correr do Lado do {@link remote.Server}.
	 * realiza a sua posição inicial na {@link Board}
	 * Após o posicionamento começa o seu movimento começando no sentido da esquerda para a direita.
	 * A cada PLAYER_PLAY_INTERVAL lê o último código do sentido lido do teclado e realiza o movimento,
	 * chamando readMovement.
	 * Esta thread apenas é interrompida em caso do jogo ter terminado.
	 */
	@Override
	 public void run() {
		 doInitialPositioning();

		 while (true){
			 try {
				 int keyCode = nextMoveCode;
				 readMovement(keyCode);
				 Thread.sleep(Board.PLAYER_PLAY_INTERVAL);
			 } catch (InterruptedException e) {
				 System.out.println("Interrupoted Human");
				 if(getBoard().isFinished())
					 break;
			 }
		 }
	 }

	/**
	 * Este setter será chamado pelo {@link environment.LocalBoard}
	 * pelo que será necessário sincronizar o objeto nextMoveCode,
	 * prevenindo leituras e escritas no mesmo instante.
	 * @param keyCode
	 */
	public void setNextMoveCode(int keyCode) {
		nextMoveCode = keyCode;
	}

	/**
	 * Processa o movimento realizado pelo o player,
	 * lendo a chave transmitida da janela ({@link java.awt.event.KeyListener}
	 * Verifica qual dos sentidos foi lido do teclado e realiza o pedido de movimento para
	 * a {@link Cell} correspondente (nextCell).
	 * @param key
	 * @throws InterruptedException
	 */
	public void readMovement(int key) throws InterruptedException {
		BoardPosition nextPosition = getCells().getLast().getPosition();
		Cell nextCell;

		if (key == KeyEvent.VK_RIGHT){
			nextPosition = nextPosition.getCellRight();
		} else if (key == KeyEvent.VK_LEFT) {
			nextPosition = nextPosition.getCellLeft();
		} else if (key == KeyEvent.VK_UP) {
			nextPosition = nextPosition.getCellAbove();
		} else if (key == KeyEvent.VK_DOWN){
			nextPosition = nextPosition.getCellBelow();
		} else {
//			System.out.println("nada");
			return; //se key for 0 neste caso sai do readmovemnt
		}

		if(!getBoard().isOutOfBounds(nextPosition)){
			nextCell = getBoard().getCell(nextPosition);
			move(nextCell);
		}

	}
}
