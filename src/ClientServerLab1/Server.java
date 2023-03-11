package ClientServerLab1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private DataInputStream inputStream = null;
    private DataOutputStream outputStream = null;
    private Socket socket = null;
    private ServerSocket serverSocket = null;

    public Server(int port) {
        String clientMessage = "";
        String clientName;

        System.out.println("Server started!");
        System.out.println("Waiting for the client ->");

        try {
            establishConnection(port);
            clientName = inputStream.readUTF();
            System.out.printf("Client '%s' is accepted! %n", clientName);
            if (socket.isConnected()) {
                System.out.println("Connected with client!");
            } else {
                System.out.println("An error occurred! Please verify given details to connect!");
            }

            processMessage(clientMessage);

            System.out.println("Connection closed!");
            closeConnection();

        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }

    private void establishConnection(int port) {
        try {
            serverSocket = new ServerSocket(port);
            socket = serverSocket.accept();
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }

    private void processMessage(String clientMessage) {
        String serverResponse = "Message is retrieved!";
        while (!clientMessage.equals("FINISH")) {
            try {
                System.out.println("Client message:");
                clientMessage = inputStream.readUTF();
                System.out.println(clientMessage);
                outputStream.writeUTF(serverResponse);
            } catch (IOException e) {
                System.out.println("Error: " + e);
                closeConnection();
                break;
            }
        }
    }

    private void closeConnection() {
        try {
            socket.close();
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }

    public static void main(String[] args) {
        Server server = new Server(9999);
    }
}
