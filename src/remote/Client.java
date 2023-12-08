package remote;


import environment.Board;
import environment.GameState;
import gui.SnakeGui;

import java.awt.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.io.IOException;
import java.net.UnknownHostException;

/** Remore client, only for part II
 * 
 * @author luismota
 *
 */

public class Client extends Thread{
	private Board myBoard;
	private SnakeGui game;
	private ObjectInputStream in; //game state in
	private PrintWriter out; //integer out
	private Socket socket;
	private InetAddress address;
	private final int port;

	public Client(String addr, String port) throws UnknownHostException {
		address = InetAddress.getByName(addr);
		this.port = Integer.parseInt(port);
		myBoard = new RemoteBoard(this);
		game = new SnakeGui(myBoard,600,0);
	}

	@Override
	public void run() {
		try {
			connectToServer();
			game.init();
			readGameState();
		} catch (IOException | ClassNotFoundException | InterruptedException e) {// ERRO...
		} finally {//a fechar...
			try {
				socket.close();
			} catch (IOException e) {//...
			}
		}
	}

	private void readGameState() throws IOException, ClassNotFoundException, InterruptedException {
		while(true){
			GameState gameState = (GameState) in.readObject();
			if(gameState.isFinished()) break;
			myBoard.setCells(gameState.getCells());
			myBoard.setSnakes(gameState.getSnakes());
			myBoard.setChanged();
		}
	}


	void connectToServer() throws IOException {
			System.out.println("Endereco:" + address);
			socket = new Socket(address, port);
			System.out.println("Socket:" + socket);
			in = new ObjectInputStream(socket.getInputStream());
			out = new PrintWriter(
					new BufferedWriter(
							new OutputStreamWriter(socket.getOutputStream())),
					true );
	}

	void sendIntruction(int keyCode) {
		out.println(keyCode);
	}

	void requestSnake(){
		out.println(0);
	}

	public static void main(String[] args) {
	}

}
