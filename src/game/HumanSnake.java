package game;

import environment.Board;
import environment.BoardPosition;
import environment.Cell;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/** Class for a remote snake, controlled by a human
  * 
  * @author luismota
  *
  */
public class HumanSnake extends Snake {
	private int nextMoveCode = KeyEvent.VK_RIGHT; //default
	public final Color color;
	private transient final Socket client;
	private transient BufferedReader reader;
	private transient Thread handleMoves;

	public HumanSnake(int id, Board board, Color color, Socket client) throws IOException {
		super(id,board);
		this.color = color;
		this.client = client;
		createReader();
		generateHandleMoves();
		board.addSnake(this);
		doInitialPositioning();
	}

	private void generateHandleMoves() {
		handleMoves = new Thread(()->{
			while (true){
				try {
					buffering();
				} catch (IOException e) {
					e.printStackTrace();
					break;
				}
			}
		});
	}

	void createReader() throws IOException{
		reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
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
		handleMoves.start();
		while (true){
			 try {
				 int key = nextMoveCode;
				 readMovement(key);
				 Thread.sleep(Board.PLAYER_PLAY_INTERVAL);
			 } catch (InterruptedException e) {
				 System.out.println("Interrupoted Human");
				 if(getBoard().isFinished()){
					 try {
						 client.close();
					 } catch (IOException ex) {
						 ex.printStackTrace();
					 }
					 handleMoves.interrupt();
					 break;
				 }
			 }
		 }
	 }

	private void buffering() throws IOException {
		setNextMoveCode(Integer.parseInt(reader.readLine()));
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
