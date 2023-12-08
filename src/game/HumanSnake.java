package game;

import environment.Board;
import environment.BoardPosition;
import environment.Cell;

import java.awt.event.KeyEvent;

/** Class for a remote snake, controlled by a human
  * 
  * @author luismota
  *
  */
public class HumanSnake extends Snake {
	private int nextMoveCode = 0; //default
	public HumanSnake(int id, Board board) {
		super(id,board);
	}

	/**
	 * Tarefa da thread de {@link HumanSnake}
	 * Esta thread estará a correr do Lado do {@link remote.Server}.
	 * Realiza a sua posição inicial na {@link Board}
	 * Após o posicionamento começa o seu movimento começando no sentido da esquerda para a direita.
	 * A cada PLAYER_PLAY_INTERVAL lê o último código do sentido lido do teclado e realiza o movimento,
	 * chamando readMovement.
	 * Esta thread apenas é interrompida em caso do jogo ter terminado.
	 */
	@Override
	 public void run() {
		 doInitialPositioning();
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
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
	 * Este 'setter' será chamado pelo {@link environment.LocalBoard}
	 * pelo que será necessário sincronizar o objeto nextMoveCode,
	 * prevenindo leituras e escritas no mesmo instante.
	 * @param keyCode inteiro que vai ser introduzido pelo ClientHandler associado a esta snake.
	 */
	public void setNextMoveCode(int keyCode) {
		nextMoveCode = keyCode;
	}

	/**
	 * Processa o movimento realizado pelo ‘player’,
	 * lendo a chave transmitida da janela ({@link java.awt.event.KeyListener}
	 * Verifica qual dos sentidos foi lido do teclado e realiza o pedido de movimento para
	 * a {@link Cell} correspondente (nextCell).
	 * @param key recebido pela thread da HumanSnake a cada jogada.
	 */
	public void readMovement(int key) throws InterruptedException {
		BoardPosition nextPosition = getCells().getLast().getPosition();
		Cell nextCell;


		switch (key){
			case KeyEvent.VK_RIGHT:
				nextPosition = nextPosition.getCellRight();
				break;
			case KeyEvent.VK_LEFT:
				nextPosition = nextPosition.getCellLeft();
				break;
			case KeyEvent.VK_UP:
				nextPosition = nextPosition.getCellAbove();
				break;
			case KeyEvent.VK_DOWN:
				nextPosition = nextPosition.getCellBelow();
				break;
		}

		if(!getBoard().isOutOfBounds(nextPosition)){
			nextCell = getBoard().getCell(nextPosition);
			move(nextCell);
		}

	}
}
