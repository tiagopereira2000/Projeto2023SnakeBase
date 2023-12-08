package remote;

import environment.Board;
import environment.LocalBoard;
import game.HumanSnake;

import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/**
 *
 * @author Tiago Pereira e Gonçalo Lopes
 */
public class Server extends Thread{
    private final Board board;
    public static final int PORTO = 8888;
    private int idCount = LocalBoard.NUM_SNAKES; //1st client id
    private ServerSocket ss;
    public Server(Board board) {
        this.board = board;
    }

    public class ClientHandler extends Thread {
        private BufferedReader in;
        private final Socket cliente;
        private HumanSnake humanSnake; //cada client handler tem uma snake associada que vai ser o objeto do jogo de cada ‘player’

        ClientHandler(Socket socket) {
            this.cliente = socket;
        }

        @Override
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
            new SendStateMulticast(board, new ObjectOutputStream(socket.getOutputStream())).start();
        }

        void initializeSnake() throws IOException {
            idCount++; // itera o contador de ids de snakes

            //gerar uma nova cor para a HumanSnake
            Random random = new Random();
            float hue = random.nextFloat();
            Color color = Color.getHSBColor(hue, 0.9f, 1.0f);

            humanSnake = new HumanSnake(idCount, board, color);
            board.addSnake(humanSnake);
            humanSnake.start();
        }

        private void serve() throws IOException {
            while(true){
                try {
                    humanSnake.setNextMoveCode(Integer.parseInt(in.readLine()));
                }catch (IOException e){
                    in.close();
                    break;
                }
            }
        }


    }

    @Override
    public void run() {
        try {
            ss = new ServerSocket(PORTO);
            board.init();
            while (true) {
                try {
                    Socket socket = ss.accept();
                    ClientHandler clienteSock = new ClientHandler(socket); //generate new handler for client
                    clienteSock.start();
                }catch (IOException ignored){
                    System.out.println("socket refused");
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e); //ss = new ServerSocket(PORTO);
        }
        finally {
            try {
                ss.close();
            } catch (IOException e) {
                System.out.println("ss.close erro");
            }
        }
    }






    /**
     *  Começará o jogo LocalBoard.init()
     *  recebe pedidos de ligação de servidores e aceita-os --> cria socket.
     *  Começará a receber pedidos de movimentos dos jogadores
     *  enviará estado do jogo a todas os ‘Sockets’ a cada intervalo {@link environment.Board}.REMOTE_REFRESH_INTERVAL.
     */
    public static void main(String[] args)  {
        LocalBoard board = new LocalBoard();
        Server server = new Server(board);
        server.start();
    }
    
}
