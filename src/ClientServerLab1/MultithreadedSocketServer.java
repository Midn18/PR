package ClientServerLab1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MultithreadedSocketServer {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(9999);
            int counter = 0;
            System.out.println("Server started!");
            while (true) {
                counter++;
                Socket serverClient = serverSocket.accept();
                System.out.printf("Client %d is accepted! %n", counter);
                ServerClientThread sct = new ServerClientThread(serverClient, counter);
                sct.start();
            }
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }
}
