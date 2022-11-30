package guessthewordserver.net.TCP;

import guessthewordserver.Session;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author matti
 */
public class Server {
    Socket socket;
    ServerSocket serverSocket;
    private AtomicBoolean listening=new AtomicBoolean(false);


    public Server() throws IOException {
        startListening();
    }
    
   
    
    public void startListening() throws IOException{
        this.serverSocket = new ServerSocket(5763);
        listening.set(true);
        new Thread(()->{
            while(listening.get()){
                try {
                    Socket newSocket = serverSocket.accept();
                    //create new Session
                    Session s = new Session(newSocket);
                    
                    
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
    }
    
    public void stopListening(){
        try {
            listening.set(false);
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
