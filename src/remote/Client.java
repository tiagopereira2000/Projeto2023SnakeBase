package remote;


import environment.Board;
import gui.SnakeGui;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.io.IOException;

/** Remore client, only for part II
 * 
 * @author luismota
 *
 */

public class Client {
	private Board myBoard;
	private SnakeGui game;
	private ObjectInputStream in; //game state in
	private PrintWriter out; //integer out
	private Socket socket;

	public Client() {
		myBoard = new RemoteBoard(this);
		game = new SnakeGui(myBoard,600,0);
	}

	public void runClient() {
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
			myBoard = (Board) in.readObject();
			System.out.println(myBoard.getSnakes().toString());
			System.out.println("received gamestate");
			myBoard.addObserver(game);
			myBoard.setChanged();


			Thread.sleep(Board.REMOTE_REFRESH_INTERVAL);
		}
	}


	void connectToServer() throws IOException {
			InetAddress endereco = InetAddress.getByName(null);
			System.out.println("Endereco:" + endereco);
			socket = new Socket(endereco, Server.PORTO);
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
		Client client = new Client();
		client.runClient();
	}

}
