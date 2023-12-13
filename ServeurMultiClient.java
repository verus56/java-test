import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ServeurMultiClient extends Thread {
    private int numClient = 0;
    private int secretNumber;
    private List<Repartiteur> clients = new ArrayList<>();

    public ServeurMultiClient() {
        // Generating a secret number
        secretNumber = (new Random().nextInt(100)) * 100;
        System.out.println("Server has chosen a secret number: " + secretNumber);
    }

    public void run() {
        try {
            ServerSocket s_server = new ServerSocket(1234);
            while (true) {
                Socket s = s_server.accept();
                ++numClient;
                Repartiteur repartiteur = new Repartiteur(s, numClient, this);
                clients.add(repartiteur);
                repartiteur.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getSecretNumber() {
        return secretNumber;
    }

    public void announceWinner(String winnerIp) {
        for (Repartiteur repartiteur : clients) {
            repartiteur.sendAnnouncement("Winner's IP: " + winnerIp);
        }
    }

    public void closeAllConnections() {
        for (Repartiteur repartiteur : clients) {
            repartiteur.sendAnnouncement("Game has ended! Server is closing. Goodbye!");
            try {
                //repartiteur.join();
                repartiteur.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.exit(0);
    }


    public static void main(String[] args) {
        new ServeurMultiClient().start();
    }
}
