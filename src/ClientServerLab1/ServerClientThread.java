package ClientServerLab1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerClientThread extends Thread {
    private DataOutputStream outputStream = null;
    private DataInputStream inputStream = null;
    private Socket serverClient;
    int clientNumber;

    public ServerClientThread(Socket inSocket, int counter) throws IOException {
        this.serverClient = inSocket;
        this.clientNumber = counter;
    }

    public void run() {
        String clientMessage = "";

        establishConnection();
        processMessage(clientMessage);
        closeConnection();

        System.out.println("Client " + clientNumber + " is disconnected!");
    }


    private void establishConnection() {
        try {
            inputStream = new DataInputStream(serverClient.getInputStream());
            outputStream = new DataOutputStream(serverClient.getOutputStream());
        } catch (IOException e) {
            System.out.printf("Verify port. Session wasn't established: %s%n", e);
        }
    }

    private void processMessage(String clientMessage) {
        while (!clientMessage.equals("FINISH")) {
            try {
                clientMessage = inputStream.readUTF();
                System.out.println("Client " + clientNumber + " message: " + clientMessage);
                String serverMessage = String.format("%s miles is %s km", clientMessage, milesToKm(Double.parseDouble(clientMessage)));
                outputStream.writeUTF(serverMessage);
                outputStream.flush();
            } catch (IOException e) {
                System.out.println("Server is down. Session is terminated!");
            }
        }
    }

    private Double milesToKm(Double miles) {
        var km = 0.0;
        return km = miles * 1.609;
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
