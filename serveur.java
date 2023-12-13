import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class serveur extends Thread {
    private int nbrC = 0;
    private int sbrj;
    private List<client> clients = new ArrayList<>();

  

    public void run() {
        try {
            ServerSocket s_server = new ServerSocket(1234);
            while (true) {
                Socket s = s_server.accept();
                ++nbrC;
                client client = new client(s, nbrC, this);
                clients.add(client);
                client.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getsbrj() {
        return sbrj;
    }

    public void announceWinner(String winnerIp) {
        for (client client : clients) {
            client.notifivtion("the winer' is : " + winnerIp);
        }
    }

    public void closeAllConnections() {
        for (client client : clients) {
            client.notifivtion("Goodbye!");
            try {
                
                client.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.exit(0);
    }

    public serveur() {
        // Generating a secret number
        sbrj = (new Random().nextInt(100)) * 100;
        System.out.println("we choise numbre a secret number: " + sbrj);
    }


    public static void main(String[] args) {
        new serveur().start();
    }
}
