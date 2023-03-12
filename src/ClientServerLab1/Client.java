package ClientServerLab1;

import java.io.*;
import java.net.Socket;

public class Client {

    private static DataOutputStream outputStream = null;
    private static DataInputStream inputStream = null;
    private static BufferedReader bufferedReader = null;
    private static Socket socket = null;
    private static final String FINISH = "FINISH";

    public static void main(String[] args) throws IOException {
        establishConnection("localhost", 9999);
        System.out.println("Connected with server!");
        System.out.println("To terminate the session write: FINISH");
        
        sendMessage();

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

    private static void sendMessage() throws IOException {
        while (true) {
            System.out.println("Enter your miles: ");
            String clientMessage = bufferedReader.readLine();

            if (clientMessage.equals(FINISH)) {
                outputStream.writeUTF(clientMessage);
                outputStream.flush();
                closeConnection();
                break;
            }

            try {
                outputStream.writeUTF(clientMessage);
                outputStream.flush();
                System.out.println("Server: " + inputStream.readUTF());
            } catch (IOException e) {
                System.out.println("Server is down. Session is terminated!");
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
