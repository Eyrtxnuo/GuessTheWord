package GuessTheWordClient;

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
    String messaggio = "";

    public GuessTheWordClient() throws UnknownHostException, IOException {
        ip = InetAddress.getLocalHost();
        socket = new Socket(ip, 5763);
        input = new DataInputStream(socket.getInputStream());
        output = new DataOutputStream(socket.getOutputStream());
        scan = new Scanner(System.in);
    }

    public static void main(String[] args) throws IOException {
        GuessTheWordClient client = new GuessTheWordClient();

        //thread1 riceve
        client.readMessageThread();

        //thread2 invia
        client.writeMessageThread();
    }

    private void readMessageThread() {
        Thread readmessage = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        String msg = input.readUTF();
                        char[] msgChar = msg.toCharArray();
                        char[] messChar = messaggio.toCharArray();
                        if (msgChar[0] == '#') {
                            System.out.println("Hai indovinato la parola!!");
                            System.out.println("E' il momento di indovinarne un' altra");
                            continue;
                        } else if (msgChar[0] == '§') {
                            msg=msg.substring(1);
                            System.out.println("Classifica");
                            System.out.println(msg);
                            socket.close();
                            continue;
                        }
                        for (int i = 0; i < messChar.length; i++) {
                            if (msgChar[i] == '!') {
                                System.out.print("\u001B[32m" + messChar[i] + "\u001B[0m");
                            } else if (msgChar[i] == '*') {
                                System.out.print("\u001B[33m" + messChar[i] + "\u001B[0m");
                            } else if (msgChar[i] == '?') {
                                System.out.print("\u001B[31m" + messChar[i] + "\u001B[0m");
                            }
                        }
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

    private void log(String msg) {
        System.out.println(msg);
    }

}
