package gui;

import environment.LocalBoard;
import remote.Client;
import remote.Server;

import java.awt.*;
import java.io.IOException;

/**
 * ‘App’ do jogo do lado do cliente, pede na consola para inserir o ip e porto do servidor.
 * @author Tiago Pereira, Gonçalo Lopes
 */
public class Main {
	public static void main(String[] args) throws IOException {
		LocalBoard svBoard = new LocalBoard();
		Server server = new Server(svBoard);
		server.start();
		String address = "localhost";
		int port = 8888;
		new Client(address, port).start();
		new Client(address, port).start();
	}
}
