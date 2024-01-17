package remote;

import environment.Board;
import environment.GameState;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class SendStateUnicast extends Thread{
    private Board board;
    private ObjectOutputStream stream;

    public SendStateUnicast(Board board, ObjectOutputStream stream) {
        this.board = board;
        this.stream = stream;
    }

    public void sendGameState(GameState gameState) throws IOException {
        stream.reset();
        stream.writeObject(gameState);
        stream.flush();
    }

    @Override
    public void run() {
        System.out.println("unicast iniciado!");
        while (true){
            try{
                sendGameState(new GameState(
                        board.getCells(),
                        board.getSnakes(),
                        board.isFinished()));

            }catch (IOException e){
                try {
                    stream.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                break;
            }
            try{
                Thread.sleep(Board.REMOTE_REFRESH_INTERVAL);
            }catch (InterruptedException e){
                System.out.println("terminating game");
                break; //break while
            }
        } //end while

    }//end run
}
