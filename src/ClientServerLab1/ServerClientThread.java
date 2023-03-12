package ClientServerLab1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerClientThread extends Thread {
    private DataOutputStream outputStream = null;
    private DataInputStream inputStream = null;
    private Socket serverClient;
    private static final String FINISH = "FINISH";
    int clientNumber;

    public ServerClientThread(Socket inSocket, int counter) throws IOException {
        this.serverClient = inSocket;
        this.clientNumber = counter;
    }

    public void run() {
        establishConnection();
        processMessage();
        closeConnection();
    }

    private void establishConnection() {
        try {
            inputStream = new DataInputStream(serverClient.getInputStream());
            outputStream = new DataOutputStream(serverClient.getOutputStream());
        } catch (IOException e) {
            System.out.printf("Verify port. Session wasn't established: %s%n", e);
        }
    }

    private void processMessage() {
        while (true) {
            try {
                String clientMessage = inputStream.readUTF();
                if (clientMessage.equals(FINISH)) {
                    closeConnection();
                    break;
                }
                System.out.printf("Client %d message: %s%n", clientNumber, clientMessage);
                String serverMessage = String.format("%s miles is %s km", clientMessage, milesToKm(Double.parseDouble(clientMessage)));
                outputStream.writeUTF(serverMessage);
                outputStream.flush();
            } catch (IOException e) {
                System.out.printf("Client %s disconnected. Session is terminated!%n", clientNumber);
                closeConnection();
                break;
            }
        }
    }

    private Double milesToKm(Double miles) {
        return miles * 1.609;
    }

    private void closeConnection() {
        try {
            outputStream.close();
            inputStream.close();
            serverClient.close();
        } catch (IOException e) {
            throw new RuntimeException("Error: " + e.getCause());
        }
    }
}
