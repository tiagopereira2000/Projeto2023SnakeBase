package gui;

import environment.LocalBoard;

public class Main {
	public static void main(String[] args) {
		LocalBoard board=new LocalBoard();
		SnakeGui game = new SnakeGui(board,600,0);
		game.init();
		// Launch server
		// TODO
		
	}
}
