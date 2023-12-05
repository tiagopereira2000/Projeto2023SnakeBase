package remote;

import environment.Board;
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
    private Board gameState;
    public static final int PORTO = 8080;
    private int idCount = LocalBoard.NUM_SNAKES; //1st client id
    private ArrayList<ObjectOutputStream> outputStreams;

    private ReentrantLock svLock = new ReentrantLock();
    private Condition outsEmpty = svLock.newCondition();



    private Thread multicastGameState = new Thread( () -> {
        System.out.println("Multicast iniciado!");
            svLock.lock();
            while(outputStreams.isEmpty()) {

                try {
                    outsEmpty.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
            svLock.unlock();


            while (true){
                svLock.lock();
                for (ObjectOutputStream o: outputStreams) {
                    try{
                        o.writeObject(gameState);
                        //o.refresh();
                        o.flush();
                    } catch (IOException e) {
                        System.out.println("o.writeObject() throws IOEx");

                        try {
                            o.close();
                        } catch (IOException ignored) {
                            System.out.println("o.close() throws IOEx");
                        }
                    }
                } //end foreach
                try{
                    svLock.unlock();
                    Thread.sleep(Board.REMOTE_REFRESH_INTERVAL);
                }catch (InterruptedException e){
                    System.out.println("terminating game");
                    gameState.terminate();
                    break; //break while
                }
        }//end while


     //end while
    }
    );

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
            svLock.lock();
            outputStreams.add(out);
            outsEmpty.signalAll();
            svLock.unlock();



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
