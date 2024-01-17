package remote;

import environment.Board;
import environment.GameState;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class SendStateMulticast extends Thread {
    private List<ObjectOutputStream> outs = new ArrayList<>();
    private Board board;
    public SendStateMulticast(Board board) {
        this.board = board;
    }

    public synchronized void sendGameState(GameState gameState) {
        List<ObjectOutputStream> outsCopy = outs;
        for (ObjectOutputStream stream: outsCopy) {
            try{
                stream.reset();
                stream.writeObject(gameState);
                stream.flush();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public synchronized void addStreamToMulticast(ObjectOutputStream s){
        outs.add(s);
    }

    @Override
    public void run() {
        System.out.println("Multicast iniciado!");
        while(!board.isFinished()){
            sendGameState(new GameState(
                    board.getCells(),
                    board.getSnakes(),
                    board.isFinished()));
            try{
                Thread.sleep(Board.REMOTE_REFRESH_INTERVAL);
            }catch (InterruptedException e){
                System.out.println("terminating game");
                break;
            }
        }
    }
}
