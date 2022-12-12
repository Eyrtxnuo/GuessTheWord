package guessthewordserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.net.SocketException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Session extends Thread {

    String parola;
    int tentativi = 0;
    int paroleIndovinate = 0;
    int paroleDaIndovinare = 1;
    Socket conn;
    private final DataInputStream input;
    private final DataOutputStream output;
    String nomeUtente = "";

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
            while (paroleIndovinate < paroleDaIndovinare) {
                String tentativo = read().toLowerCase();
                tentativi++;
                if (tentativo.equals(parola)) {
                    paroleIndovinate++;
                    if (paroleIndovinate == paroleDaIndovinare) {
                        try {
                            File folder = new File(Session.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
                            File file = new File(folder.getAbsolutePath() + "\\classifica.csv");
                            file.createNewFile();
                            
                            write("§");
                            String Username = read();
                            try (FileWriter writer = new FileWriter(file,true)) {
                                writer.write(Username+";"+tentativi+"\n");
                                
                            }
                            try (Scanner myReader = new Scanner(file)) {
                                String data = "";
                                while (myReader.hasNextLine()) {
                                    data += myReader.nextLine() + "\n";
                                }
                                write(data);
                            }
                        } catch (URISyntaxException ex) {
                            Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        write("#");
                        parola = GetParola();
                        System.out.println(parola);
                        continue;
                    }
                    write("#");
                    parola = GetParola();
                    System.out.println(parola);
                    continue;
                }
                char[] resp = new char[parola.length()];
                var lettereParola = parola.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
                var lettereTentativo = tentativo.toCharArray();
                for (int i = 0; i < parola.length() && i < lettereTentativo.length; i++) {
                    if (parola.charAt(i) == lettereTentativo[i]) {
                        resp[i] = '!';
                        lettereParola.remove((Object) parola.charAt(i));
                    }
                }
                for (int i = 0; i < resp.length; i++) {
                    if (resp[i] != 0) {
                        continue;
                    }
                    for (int i = 0; i < resp.length; i++) {
                        if (resp[i] != 0) {
                            continue;
                        }
                        if (i < lettereTentativo.length && lettereParola.remove((Object) lettereTentativo[i])) {
                            resp[i] = '*';
                        } else {
                            resp[i] = '?';
                        }
                    }
                    write(new String(resp));
                }
            }
            closeStream();

        } catch (SocketException ex) {
            Logger.getLogger(Session.class.getName()).log(Level.INFO, "Client Disconnected!");
        } catch (IOException ex) {
            Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
        } catch (URISyntaxException ex) {
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

    public void stopSession() throws IOException {
        closeStream();
    }

    public static String GetParola() {
        try {
            URL is = Session.class.getResource("/5_lettere.txt");
            File file = new File(is.toURI());
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            int lines = Math.round(raf.length() / 7);//6 should be the line lenght (5 chars + CR + LF)

            raf.seek(getRandomNumber(0, lines) * 7);
            return raf.readLine();
        } catch (URISyntaxException | IOException ex) {
            Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static String writeTo() {
        try {
            File folder = new File(Session.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
            File file = new File(folder.getAbsolutePath() + "\\classifica.csv");
            file.createNewFile();
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                System.out.println(data);
            }
            myReader.close();
            return file.getAbsolutePath();
        } catch (URISyntaxException ex) {
            Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
