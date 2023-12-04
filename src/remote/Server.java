package remote;

import environment.Board;
import environment.LocalBoard;
import game.HumanSnake;
import gui.SnakeGui;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Server{
    private Board gameState;
    public static final int PORTO = 8080;
    private int idCount = LocalBoard.NUM_SNAKES; //1st client id
    private ArrayList<ObjectOutputStream> outputStreams;

    private Thread multicastGameState = new Thread( () -> {
        while(outputStreams.isEmpty()){
            try {
                synchronized (outputStreams){
                    outputStreams.wait();
                }
            } catch (InterruptedException ignored) {}
        }

        while (!outputStreams.isEmpty()){
            for (ObjectOutputStream o: outputStreams) {
                try{
                    o.writeObject(gameState);
                    o.flush();
                } catch (IOException e) {
                    System.out.println("o.writeObject() throws IOEx");
                }finally {
                    outputStreams.remove(o);
                    try {
                        o.close();
                    } catch (IOException ignored) {
                        System.out.println("o.close() throws IOEx");
                    }
                }
            } //end foreach
            try{
                Thread.sleep(Board.REMOTE_REFRESH_INTERVAL);
            }catch (InterruptedException e){
                System.out.println("terminating game");
                gameState.terminate();
                break;
            }
        } //end while
    });

    public Server(Board gameState) {
        this.gameState = gameState;
        outputStreams = new ArrayList<>();
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
                throw new RuntimeException(e);
            }


        }

        void doConnections(Socket socket) throws IOException {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new ObjectOutputStream(socket.getOutputStream());
            synchronized (outputStreams){
                outputStreams.add(out);
                outputStreams.notify();
            }
        }

        void initializeSnake() throws IOException {
            idCount++; // itera o contador de ids de snakes
            humanSnake = new HumanSnake(idCount, gameState);
            gameState.addSnake(humanSnake);
            humanSnake.start();
        }

        private void serve() throws IOException {
            while (true) {
                try {
                    humanSnake.setNextMoveCode(Integer.parseInt(in.readLine()));
                }catch (IOException e){
                }finally {
                    in.close();
                    break;
                }
            }
        }


    }

    public void startServing() throws IOException {
        ServerSocket ss = new ServerSocket(PORTO);
        try {
            multicastGameState.start();
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
        SnakeGui game = new SnakeGui(board, 200,0);
        Server server = new Server(board);
        game.init();
        server.startServing();
    }
    
}
