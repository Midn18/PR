package ClientServerLab1;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private DataInputStream inputStream = null;
    private Socket socket = null;
    private ServerSocket serverSocket = null;

    public Server(int port) {
        String clientMessage = "";

        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started!");
            System.out.println("Waiting for the client->");
            socket = serverSocket.accept();

            if (socket.isConnected()) {
                System.out.println("Connected with client!");
            } else {
                System.out.println("An error occurred! Please verify given details to connect!");
            }
            inputStream = new DataInputStream(socket.getInputStream());

            while (!clientMessage.equals("FINISH")) {
                try {
                    clientMessage = inputStream.readUTF();
                    System.out.println(clientMessage);
                } catch (IOException e) {
                    System.out.println("Error: " + e);
                }
            }
            socket.close();
            inputStream.close();
            System.out.println("Connection closed!");
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }

    public static void sendMessageToClient(String message) {

    }

    public static void main(String[] args) {
        Server server = new Server(9999);
    }
}
