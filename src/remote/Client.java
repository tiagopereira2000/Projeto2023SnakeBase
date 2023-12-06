package remote;


import environment.Board;
import gui.SnakeGui;

import javax.sound.sampled.Port;
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

public class Client {
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
			Board serverBoard = (Board) in.readObject();
			myBoard.setCells(serverBoard.getCells());
			myBoard.setSnakes(serverBoard.getSnakes());
			myBoard.setChanged();

			if(serverBoard.isFinished()) break;
		}
	}


	void connectToServer() throws IOException {
//			InetAddress endereco = InetAddress.getByName(null);
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

	public static void main(String[] args) throws UnknownHostException {
		Client client = new Client(args[0],args[1]);
		client.runClient();
	}

}
