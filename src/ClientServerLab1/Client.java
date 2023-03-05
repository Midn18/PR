package ClientServerLab1;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private DataOutputStream outputStream = null;
    private Scanner scanner = null;
    private Socket socket = null;

    public Client(String address, int port) {
        String userInput = "";
        try {
            socket = new Socket(address, port);
            System.out.println("To terminate the session write: FINISH");
            scanner = new Scanner(System.in);
            outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }

        while (!userInput.equals("FINISH")) {
            var input = scanner.nextLine();
            try {
                outputStream.writeUTF(input);
            } catch (IOException e) {
                System.out.println("Error: " + e);
            }
        }
        System.out.println("Connection finished!");

        try {
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }

    public static void main(String[] args) {
        Client client = new Client("localhost", 9999);
    }
}
