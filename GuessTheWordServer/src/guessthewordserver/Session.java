package guessthewordserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Session extends Thread{
    String parola;
    int tentativi = 0;
    boolean running;
    Socket conn;
    private DataInputStream input; 
    private DataOutputStream output;
    
    public Session(Socket conn) throws IOException {
        this.conn = conn;
        input = new DataInputStream(conn.getInputStream());
        output = new DataOutputStream(conn.getOutputStream());
        parola = "get random parola";
        start();
    }

    @Override
    public void run() {
        try {
            running = true;
            while(running){
                String tentativo = read();
                
            }
            closeStream();
        } catch (IOException ex) {
            Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void closeStream() throws IOException {
        input.close();
        output.close();
    }

    private void closeSocket() throws IOException {
        conn.close();
    }

    private String read() throws IOException {
        return input.readUTF();
    }

    private void write(String message) throws IOException {
        output.writeUTF(message);
    }
    
    
}
