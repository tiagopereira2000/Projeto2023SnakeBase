package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;

import environment.Board;
import environment.LocalBoard;
/**
 *  Class to create and configure GUI.
 *  Only the listener to the button should be edited, see TODO below.
 * 
 * @author luismota
 *
 */
public class SnakeGui implements Observer {
	public static final int BOARD_WIDTH = 800;
	public static final int BOARD_HEIGHT = 800;
	public static final int NUM_COLUMNS = 40;
	public static final int NUM_ROWS = 30;
	private JFrame frame;
	private BoardComponent boardGui;
	private Board board;

	public BoardComponent getBoardGui() {
		return boardGui;
	}

	public SnakeGui(Board board, int x, int y) {
		super();
		this.board=board;
		frame= new JFrame("The Snake Game: "+(board instanceof LocalBoard?"Local":"Remote"));
		frame.setLocation(x, y);
		buildGui();
	}

	private void buildGui() {
		frame.setLayout(new BorderLayout());
		
		boardGui = new BoardComponent(board);
		boardGui.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
		frame.add(boardGui,BorderLayout.CENTER);

		JButton resetObstaclesButton=new JButton("Reset snakes' directions");

		/*Após o clique no botão "reset snakes" a gui perde o foco na janela e
		* respetivos keyListener. É necessário então fazer um pedido de foco na janela
		* para continuar a ler o teclado.*/
		resetObstaclesButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				board.wakeLazySnakes();
				boardGui.requestFocusInWindow(); //foi me aconselhado usar este método.
			}
		});

		frame.add(resetObstaclesButton,BorderLayout.SOUTH);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void init() {
		frame.setVisible(true);
		board.addObserver(this);
		board.init();
	}

	@Override
		public void update(Observable o, Object arg) {
			boardGui.repaint();
		}
}
