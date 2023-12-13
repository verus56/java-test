import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class client extends Thread {
    
    Socket socket;
    private int nbrC;
    private serveur server;

    public client(Socket socket, int nbrC, serveur server) {
        super();
        this.socket = socket;
        this.nbrC = nbrC;
        this.server = server;
    }

    public void notifivtion(String message) {
        try {
            OutputStream output = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(output, true);
            pw.println(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            InputStream input = socket.getInputStream();
            InputStreamReader ins = new InputStreamReader(input);
            BufferedReader br = new BufferedReader(ins);
            OutputStream output = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(output, true);

            pw.println("Welcome! You are  " + nbrC);
            pw.println("guess the nbr");

            while (true) {
                String guessStr = br.readLine();
                int guess = Integer.parseInt(guessStr);

               

                if (guess < server.getsbrj()) {
                    pw.println("mor hight.");
                } else if (guess > server.getsbrj()) {
                    pw.println("mor low");
                } else {
                    // Correct
                    pw.println("Congratulations!: "+guess);
                    server.announceWinner(socket.getRemoteSocketAddress().toString());

                    server.closeAllConnections();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
