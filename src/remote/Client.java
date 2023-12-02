package remote;


import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/** Remore client, only for part II
 * 
 * @author luismota
 *
 */

public class Client {
	RemoteBoard myBoard;

	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Socket socket;

	public Client(RemoteBoard remoteBoard) {
		myBoard = remoteBoard;
	}

	public void runClient() {
		try {
			connectToServer();
			sendIntructions();
		} catch (IOException e) {// ERRO...
		} finally {//a fechar...
			try {
				socket.close();
			} catch (IOException e) {//...
			}
		}
	}


	void connectToServer() throws IOException {
			InetAddress endereco = InetAddress.getByName(null);
			System.out.println("Endereco:" + endereco);
			socket = new Socket(endereco, Server.PORTO);
			System.out.println("Socket:" + socket);
			System.out.println("teste em connect to server <1>");
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			System.out.println("teste em connect to server <2>");


			System.out.println("teste em connect to server <3>");

	}

	void sendIntructions(){

	}

	public static void main(String[] args) {

		// TODO criará uma RemoteBoard para receber a janela do servidor
		// TODO criar objeto SnakeGui e abrir janela do jogo
		// TODO vai fazer um pedido de ligação com o Server --> Criar Socket para receber estado do jogo (Board)
		runClient();
		// TODO começa a ler o estado do jogo
	}

}
