package remote;

import environment.Board;
import environment.GameState;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class SendStateMulticast extends Thread{
    private Board board;
    private ObjectOutputStream out;

    public SendStateMulticast(Board board, ObjectOutputStream out) {
        this.board = board;
        this.out = out;
    }

    @Override
    public void run() {
        System.out.println("Multicast iniciado!");
        while (true){
            try{
                out.writeObject(new GameState(
                        board.getCells(),
                        board.getSnakes(),
                        board.isFinished()));
                out.flush();
                out.reset();
            } catch (IOException e) {
                System.out.println("o.writeObject() throws IOEx");
                try {
                    out.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
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
