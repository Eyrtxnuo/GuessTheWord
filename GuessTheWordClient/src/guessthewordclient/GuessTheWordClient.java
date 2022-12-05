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
                while (true) {
                    try {
                        String msg = input.readUTF();
                        log(msg);
                    } catch (IOException ex) {
                        Logger.getLogger(GuessTheWordClient.class.getName()).log(Level.SEVERE, null, ex);
                    }

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
                        String msg =scan.nextLine();
                        while(msg.length()!=5){
                            System.out.println("Inserire una parola di 5 lettere");
                            msg =scan.nextLine();
                        }
                        output.writeUTF(msg);
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
