import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Repartiteur extends Thread {
    
    Socket socket;
    private int numClient;
    private ServeurMultiClient server;

    public Repartiteur(Socket socket, int numClient, ServeurMultiClient server) {
        super();
        this.socket = socket;
        this.numClient = numClient;
        this.server = server;
    }

    public void run() {
        try {
            InputStream input = socket.getInputStream();
            InputStreamReader ins = new InputStreamReader(input);
            BufferedReader br = new BufferedReader(ins);
            OutputStream output = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(output, true);

            pw.println("Welcome! You are client number: " + numClient);
            pw.println("Guess the secret number.");

            while (true) {
                String guessStr = br.readLine();
                int guess = Integer.parseInt(guessStr);

                // Printing the numver
                System.out.println("Client " + numClient + " guessed: " + guess);

                if (guess < server.getSecretNumber()) {
                    pw.println("The secret number is higher.");
                } else if (guess > server.getSecretNumber()) {
                    pw.println("The secret number is lower.");
                } else {
                    // Correct
                    pw.println("Congratulations! You have won! The secret number is: "+guess);
                    server.announceWinner(socket.getRemoteSocketAddress().toString());

                    server.closeAllConnections();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendAnnouncement(String message) {
        try {
            OutputStream output = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(output, true);
            pw.println(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
