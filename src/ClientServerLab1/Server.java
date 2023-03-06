package ClientServerLab1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Server {
    private DataInputStream inputStream = null;
    private DataOutputStream outputStream = null;
    private Socket socket = null;
    private ServerSocket serverSocket = null;

    public Server(int port) {
        String clientMessage = "";
        String serverResponse = "Message is retrieved!";

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
            outputStream = new DataOutputStream(socket.getOutputStream());

            while (!clientMessage.equals("FINISH")) {
                try {
                    System.out.println("Client message:");
                    clientMessage = inputStream.readUTF();
                    System.out.println(clientMessage);
                    outputStream.writeUTF(serverResponse);
                } catch (SocketException e) {
                    System.out.println("Error: " + e);
                }
            }

            socket.close();
            inputStream.close();
            outputStream.close();
            System.out.println("Connection closed!");

        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }

    public static void main(String[] args) {
        Server server = new Server(9999);
    }
}
