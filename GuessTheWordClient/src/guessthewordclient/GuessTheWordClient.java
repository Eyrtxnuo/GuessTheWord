package GuessTheWordClient;

import guessthewordclient.Classifica;
import guessthewordclient.GameFrame;
import guessthewordclient.KeyboardKey;
import java.awt.event.ActionEvent;
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
        showIpDialog();
        /*ip = InetAddress.getLocalHost();
        socket = new Socket(ip, 5763);
        input = new DataInputStream(socket.getInputStream());
        output = new DataOutputStream(socket.getOutputStream());
        frame = new GameFrame(output);*/
        //scan = new Scanner(System.in);
    }

    public static void main(String[] args) throws IOException {
        GuessTheWordClient client = new GuessTheWordClient();
        /*
        //thread1 riceve
        client.readMessageThread();

        //thread2 invia
        //client.writeMessageThread();//implementato nella grafica
        
        client.frame.setVisible(true);*/
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

    private void log(String msg) {
        System.out.println(msg);
    }

    private void showIpDialog() {
        var jDialog1 =  new javax.swing.JDialog();
        var dialogIPField = new javax.swing.JTextField();
        var dialogOkButton = new javax.swing.JButton();
        var jLabel1 = new javax.swing.JLabel();
        jDialog1.setLocationByPlatform(true);
        jDialog1.getRootPane().setDefaultButton(dialogOkButton);

        dialogIPField.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N

        dialogOkButton.setText("Ok");
        dialogOkButton.setDefaultCapable(false);
        dialogOkButton.setInheritsPopupMenu(true);
        dialogOkButton.setSelected(true);
        dialogOkButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dialogOkButtonActionPerformed(evt);
            }

            private void dialogOkButtonActionPerformed(ActionEvent evt) {
                try {
                    ip = InetAddress.getByName(dialogIPField.getText());
                    socket = new Socket(ip, 5763);
                    input = new DataInputStream(socket.getInputStream());
                    output = new DataOutputStream(socket.getOutputStream());
                    frame = new GameFrame(output);
                    readMessageThread();
                    frame.setVisible(true);
                    jDialog1.setVisible(false);
                } catch (UnknownHostException ex) {
                    Logger.getLogger(GuessTheWordClient.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(GuessTheWordClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel1.setText("Inserisci l'IP");
        
        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDialog1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(dialogIPField)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialog1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(dialogOkButton, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(dialogIPField, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(dialogOkButton, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jDialog1.setResizable(false);
        jDialog1.pack();
        jDialog1.setVisible(true);
    }

}
