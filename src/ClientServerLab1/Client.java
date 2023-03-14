package ClientServerLab1;

import java.io.*;
import java.net.Socket;

public class Client {

    private static DataOutputStream outputStream = null;
    private static DataInputStream inputStream = null;
    private static BufferedReader bufferedReader = null;
    static Thread sendMessage;
    static Thread receiveMessage;
    private static Socket socket = null;
    private static final String FINISH = "FINISH";

    public static void main(String[] args) {
        establishConnection("localhost", 9999);
        System.out.println("Connected with server!");
        System.out.println("To terminate the session write: FINISH");
        sendMessage = new Thread(Client::sendMessage);
        receiveMessage = new Thread(Client::receiveMessage);

        while (socket.isConnected()) {
            if (sendMessage.isAlive() && receiveMessage.isAlive())
                continue;
            sendMessage.start();
            receiveMessage.start();
            break;
        }
        try {
            sendMessage.join();
            receiveMessage.join();
        } catch (InterruptedException e) {
            System.out.println("Closing error: " + e);
        }

        System.out.println("Connection closed!");
        closeConnection();
    }

    private static void establishConnection(String address, int port) {
        try {
            socket = new Socket(address, port);
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
            bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        } catch (IOException e) {
            System.out.printf("Verify port. Session wasn't established: %s%n", e);
        }
    }

    private static void sendMessage() {
        while (socket.isConnected()) {
            try {
                System.out.println("-> ");
                String clientMessage = bufferedReader.readLine();

                if (clientMessage.equals(FINISH)) {
                    outputStream.writeUTF(clientMessage);
                    outputStream.flush();
                    closeConnection();
                    break;
                }
                outputStream.writeUTF(clientMessage);
                outputStream.flush();
            } catch (IOException e) {
                System.out.println("Server is down. Session is terminated!");
                closeConnection();
                break;
            }
        }
    }

    private static void receiveMessage() {
        while (socket.isConnected()) {
            try {
                String serverMessage = inputStream.readUTF();
                System.out.println(serverMessage);
            } catch (IOException e) {
                System.out.println("Session is terminated!");
                closeConnection();
                break;
            }
        }
    }

    private static void closeConnection() {
        try {
            outputStream.close();
            inputStream.close();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error: " + e.getCause());
        }
    }
}
