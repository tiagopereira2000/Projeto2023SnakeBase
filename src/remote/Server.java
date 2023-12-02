package remote;

import environment.LocalBoard;
import game.HumanSnake;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private LocalBoard gameState;

    public static final int PORTO = 8080;


    public Server(LocalBoard gameState) {
        this.gameState = gameState;
    }


    public class ClientHandler extends Thread{
        private ObjectInputStream in;
        private ObjectOutputStream out;
        Socket cliente;

        int count;  //Contagem dos IDS das snakes

        HumanSnake humanSnake; //cada client handler tem uma snake associada que vai ser o objeto do jogo de cada player

        ClientHandler(Socket socket){
            this.cliente = socket;
        }

        public void run(){
            try{
                doConnections(cliente);
                initializeSnake();
                serve();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }

        void doConnections(Socket socket) throws IOException{
            try{
                in = new ObjectInputStream(socket.getInputStream());
                out = new ObjectOutputStream(socket.getOutputStream());




            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }

        void initializeSnake(){
            count++; // itera o contador de ids de snakes

            humanSnake = new HumanSnake(count,gameState);
        }

        private void serve() throws IOException{
            try {


                while (in.readObject() != null) {
                    //Aqui recebe as intruçoes do jogador e executa o necessario para que o jogo seja atualizado de maneira certa


                    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

                    //Envia a Board
                    out.writeObject(gameState);
                    out.flush();
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                        cliente.close();
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }




    }


    public void startServing() throws IOException {
        ServerSocket ss = new ServerSocket(PORTO);

        try {
            while (true) {
                Socket socket = ss.accept();


                // Aceita a conecção
                ClientHandler clienteSock = new ClientHandler(socket);
                clienteSock.start();

            }
        } finally {
            // Fecha o socket
            ss.close();
        }
    }






    /**
     *  começará o jogo LocalBoard.init()
     *  recebe pedidos de ligação de servidores e aceita-os --> cria socket.
     *  começará a receber pedidos de movimentos dos jogadores
     *  enviará estado do jogo a todas as Sockets a cada intervalo {@link environment.Board}.REMOTE_REFRESH_INTERVAL.
     * @param args
     */
    public static void main(String[] args) {

    }
    
}
