package ClientServerLab1;

import java.io.*;
import java.net.Socket;

public class Client {

    private static DataOutputStream outputStream = null;
    private static DataInputStream inputStream = null;
    private static BufferedReader bufferedReader = null;
    private static Socket socket = null;

    public static void main(String[] args) throws IOException {
        String clientMessage = "";

        establishConnection("localhost", 9999);
        System.out.println("Connected with server!");
        System.out.println("To terminate the session write: FINISH");
        sendMessage(clientMessage);

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

    private static void sendMessage(String clientMessage) throws IOException {
        while (!clientMessage.equals("FINISH")) {
            System.out.println("Enter your miles: ");
            clientMessage = bufferedReader.readLine();
            try {
                outputStream.writeUTF(clientMessage);
                outputStream.flush();
                System.out.println("Server: " + inputStream.readUTF());
            } catch (IOException e) {
                System.out.println("Server is down. Session is terminated!");
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
