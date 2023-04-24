package lab1;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MultithreadedSocketServer {
    static ArrayList<Socket> clients = new ArrayList<>();
    static int counter = 0;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(9999);
            System.out.println("Server started!");
            while (!serverSocket.isClosed()) {
                counter++;
                Socket serverClient = serverSocket.accept();
                System.out.printf("Client %d is accepted! %n", counter);
                clients.add(serverClient);
                ServerClientThread sct = new ServerClientThread(serverClient, counter);
                sct.start();
            }
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }

    public static void sendMessageToAllClients(String message, Socket socket) {
        for (Socket client : clients) {
            if (client != socket) {
                try {
                    DataOutputStream outputStream = new DataOutputStream(client.getOutputStream());
                    outputStream.writeUTF(message);
                    outputStream.flush();
                } catch (IOException e) {
                    System.out.println("Error: " + e);
                }
            }
        }
    }
}
