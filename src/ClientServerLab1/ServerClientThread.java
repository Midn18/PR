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
        while (serverClient.isConnected()) {
            try {
                String clientMessage = inputStream.readUTF();
                if (clientMessage.equals(FINISH)) {
                    MultithreadedSocketServer.clients.remove(serverClient);
                    System.out.printf("Client %s disconnected.%n", clientNumber);
                    closeConnection();
                    break;
                }
                var response = String.format("Client %d: %s", clientNumber, clientMessage);
                System.out.println(response);
                MultithreadedSocketServer.sendMessageToAllClients(response, serverClient);
            } catch (IOException e) {
                System.out.printf("Client %s disconnected. Session is terminated!%n", clientNumber);
                MultithreadedSocketServer.clients.remove(serverClient);
                closeConnection();
                break;
            }
        }
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
