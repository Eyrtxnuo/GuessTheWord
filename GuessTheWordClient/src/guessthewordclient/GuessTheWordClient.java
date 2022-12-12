package GuessTheWordClient;

import guessthewordclient.Classifica;
import guessthewordclient.GameFrame;
import guessthewordclient.KeyboardKey;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GuessTheWordClient {

    Scanner scan;
    Socket socket = null;
    DataInputStream input = null;
    DataOutputStream output = null;
    InetAddress ip;
    String messaggio="";
    GameFrame frame;

    public GuessTheWordClient() throws UnknownHostException, IOException {
        ip = InetAddress.getLocalHost();
        socket = new Socket(ip, 5763);
        input = new DataInputStream(socket.getInputStream());
        output = new DataOutputStream(socket.getOutputStream());
        frame = new GameFrame(output);
        //scan = new Scanner(System.in);
    }

    public static void main(String[] args) throws IOException {
        GuessTheWordClient client = new GuessTheWordClient();

        client.nomeUtente();
        //thread1 riceve
        client.readMessageThread();

        //thread2 invia
        //client.writeMessageThread();//implementato nella grafica
        
        client.frame.setVisible(true);
    }

    
    private void readMessageThread() {
        Thread readmessage = new Thread(new Runnable() {
            @Override  
            public void run() {
                try {
                    while (true) {
                        String msg = input.readUTF();
                        //System.out.println(msg);
                        char[] msgChar = msg.toCharArray();
                        char[] messChar = frame.getText().toLowerCase().toCharArray();
                        if (msgChar[0] == '#') {
                            System.out.println("Hai indovinato la parola!!");
                            System.out.println("E' il momento di indovinarne un' altra");
                            frame.setGotIt(true);
                            continue;
                        }else if (msgChar[0] == '§') {
                            //Winning transaction
                            frame.setGotIt(true);
                            frame.setPlaying(false);
                            new Classifica(socket);  
                            //output.write(0);
                            socket.close();
                            continue;
                        }else {
                            frame.setGotIt(false);
                            frame.setInputColorMap(msg);
                        }
                        for (int i = 0; i < messChar.length; i++) {
                            switch (msgChar[i]) {
                                case '!' -> {
                                    GameFrame.setCharStatus(messChar[i], KeyboardKey.STATUS.FOUND);
                                    GameFrame.foundChars[i] = messChar[i];
                                    System.out.print("\u001B[32m"+messChar[i]+"\u001B[0m");
                                }
                                case '*' -> {
                                    GameFrame.setCharStatus(messChar[i], KeyboardKey.STATUS.PRESENT);
                                    System.out.print("\u001B[33m"+messChar[i]+"\u001B[0m");
                                }
                                case '?' -> {
                                    GameFrame.setCharStatus(messChar[i], KeyboardKey.STATUS.NOT_PRESENT);
                                    System.out.print("\u001B[31m"+messChar[i]+"\u001B[0m");
                                }
                                default -> {
                                }
                            }
                        }
                        System.out.println();
                    }
                } catch (IOException ex) {
                    try {
                        socket.close();
                    } catch (IOException ex1) {
                        Logger.getLogger(GuessTheWordClient.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                    Logger.getLogger(GuessTheWordClient.class.getName()).log(Level.SEVERE, "Server disconnected");
                }
            }
        });
        readmessage.start();
    }

    private void writeMessageThread() {
        Thread writemessage = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        messaggio = scan.nextLine();
                        while (messaggio.length() != 5) {
                            System.out.println("Inserire una parola di 5 lettere");
                            messaggio = scan.nextLine();
                        }
                        output.writeUTF(messaggio);
                    } catch (IOException ex) {
                        Logger.getLogger(GuessTheWordClient.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
        });
        writemessage.start();
    }
    
    public void writeMessage(String message) throws IOException{
        messaggio =scan.nextLine();
        while(messaggio.length()!=5){
            System.out.println("Inserire una parola di 5 lettere");
            messaggio =scan.nextLine();
        }
        output.writeUTF(messaggio);
    }

    private void nomeUtente() throws IOException {
        System.out.println("Come vuoi essere salvato nella classifica?");
        String nome = scan.nextLine();
        output.writeUTF("(" + nome);
    }

}
