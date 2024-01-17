package remote;

import environment.Board;
import environment.LocalBoard;
import game.HumanSnake;

import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
    private SendStateMulticast multicast;

    public Server(Board board) {
        this.board = board;
        multicast = new SendStateMulticast(board);
    }


    class ClientHandler extends Thread{
        Socket client;
        ObjectOutputStream stream;
        InputStreamReader reader;

        public ClientHandler(Socket client) throws IOException {
            this.client = client;
            stream = new ObjectOutputStream(client.getOutputStream());
        }

        @Override
        public void run() {
            try {
                connecting();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void connecting() throws IOException {
            idCount++; // itera o contador de ids de snakes
//            new SendStateUnicast(board, stream).start();
            multicast.addStreamToMulticast(stream);
            HumanSnake mySnake = new HumanSnake(idCount, board, generateColor(), client);
            ((LocalBoard) board).signalHumanPlayer(mySnake);
        }


        public Color generateColor(){
            Random random = new Random();
            float hue = random.nextFloat();
            return Color.getHSBColor(hue, 0.9f, 1.0f);
        }

    }



    /**
     *
     */
    @Override
    public void run() {
        try {
            ss = new ServerSocket(PORTO);
            new Thread(() -> {
//                try {
//                    multicast.start();
//                    Thread.sleep(3000);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }finally {
//                    board.init();
//                }
                multicast.start();
                board.init();
            }).start();

            while (true) {
                try {
                     new ClientHandler(ss.accept()).start();
                }catch (IOException ignored){
                    System.out.println("socket refused");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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
