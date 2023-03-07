package ClientServerLab1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private DataOutputStream outputStream = null;
    private DataInputStream inputStream = null;
    private Scanner scanner = null;
    private Socket socket = null;

    public Client(String address, int port) {
        String userInput = "";
        String serverMessage = "";

        try {
            socket = new Socket(address, port);
            System.out.println("To terminate the session write: FINISH");
            scanner = new Scanner(System.in);
            outputStream = new DataOutputStream(socket.getOutputStream());
            inputStream = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("Error: " + e.getCause());
        }

        while (!userInput.equals("FINISH")) {
            userInput = scanner.nextLine();
            try {
                outputStream.writeUTF(userInput);
                serverMessage = inputStream.readUTF();
                System.out.println(serverMessage);
            } catch (IOException e) {
                System.out.println("Server is down. Please insert FINISH to terminate connection!");
            }
        }

        System.out.println("Connection finished!");
        try {
            outputStream.close();
            inputStream.close();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error: " + e.getCause());
        }
    }

    public static void main(String[] args) {
        Client client = new Client("localhost", 9999);
    }
}
