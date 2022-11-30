package guessthewordserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;
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
        parola = GetParola();
        System.out.println(parola);
        start();
    }

    @Override
    public void run() {
        try {
            running = true;
            while(running){
                String tentativo = read();
                if(tentativo.equals(parola)){
                    write("Hai indovinato! la parola era: " + parola+" Nuovo round:");
                    parola= GetParola();
                    System.out.println(parola);
                    continue;
                }
                String resp= "";
                var lettereParola = parola.toCharArray();
                var lettereTentativo = tentativo.toCharArray();
                for(int i = 0; i<lettereParola.length && i<lettereTentativo.length; i++){
                    resp += (lettereParola[i]==lettereTentativo[i])?lettereTentativo[i]:"?";
                }
                for(int i = resp.length(); i<lettereParola.length; i++){
                    resp += "?";
                }
                write(resp);
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
    
    public void stopSession() throws IOException{
        closeStream();
    }
    
    public static String GetParola(){
        try {
            URL is = Session.class.getResource("/5_lettere.txt");
            File file = new File(is.toURI());
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            int lines = Math.round(raf.length()/7);//6 should be the line lenght (5 chars + CR + LF)
            
            raf.seek(getRandomNumber(0, lines)*7);
            return raf.readLine();
        } catch (URISyntaxException | IOException ex) {
            Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
