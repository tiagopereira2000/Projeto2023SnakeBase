package remote;

import environment.Board;
import environment.GameState;
import environment.LocalBoard;
import game.HumanSnake;
import gui.SnakeGui;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Server{
    private Board board;
    public static final int PORTO = 8888;
    private int idCount = LocalBoard.NUM_SNAKES; //1st client id

    public Server(Board gameState) {
        this.board = gameState;
    }

    public class ClientHandler extends Thread {
        private BufferedReader in;
        private ObjectOutputStream out;
        private Socket cliente;
        private HumanSnake humanSnake; //cada client handler tem uma snake associada que vai ser o objeto do jogo de cada player

        ClientHandler(Socket socket) {
            this.cliente = socket;
        }

        public void run() {
            try {
                doConnections(cliente);
                initializeSnake();
                serve();

            } catch (IOException e) {
//                throw new RuntimeException(e);
            }


        }

        void doConnections(Socket socket) throws IOException {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new ObjectOutputStream(socket.getOutputStream());
            new SendStateMulticast(board, out).start();
        }

        void initializeSnake() throws IOException {
            idCount++; // itera o contador de ids de snakes
            humanSnake = new HumanSnake(idCount, board);
            board.addSnake(humanSnake);
            humanSnake.start();
        }

        private void serve() throws IOException {
            while (true) {
                try {
                    humanSnake.setNextMoveCode(Integer.parseInt(in.readLine()));
                }catch (IOException e){
                    in.close();
                    break;
                }
            }
        }


    }

    public void startServing() throws IOException {
        ServerSocket ss = new ServerSocket(PORTO);
        board.init();
        try {
            while (true) {
                Socket socket = ss.accept(); //acept connection with client
                ClientHandler clienteSock = new ClientHandler(socket); //generate new handler for client
                clienteSock.start();
            }
        } finally {
            ss.close();// Fecha o socket
        }
    }






    /**
     *  começará o jogo LocalBoard.init()
     *  recebe pedidos de ligação de servidores e aceita-os --> cria socket.
     *  começará a receber pedidos de movimentos dos jogadores
     *  enviará estado do jogo a todas as Sockets a cada intervalo {@link environment.Board}.REMOTE_REFRESH_INTERVAL.
     * @param args
     */
    public static void main(String[] args) throws IOException {
        LocalBoard board = new LocalBoard();
        Server server = new Server(board);
        server.startServing();
    }
    
}
