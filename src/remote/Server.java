package remote;

import environment.LocalBoard;

import java.io.IOException;

public class Server {
    private LocalBoard gameState;

    public Server(LocalBoard gameState) {
        this.gameState = gameState;
    }



    public void startServing() throws IOException{
        //TODO
    }
    void doConnections() throws IOException{
        //TODO
    }

    private void serve() throws IOException{
        //TODO
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
