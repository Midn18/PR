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

        try {
            establishConnection(address, port);
            insertName(scanner);
            System.out.println("To terminate the session write: FINISH");
        } catch (IOException e) {
            throw new RuntimeException("Error: " + e.getCause());
        }

        sendMessage(userInput);

        System.out.println("Connection finished!");
        closeConnection();
    }

    private void establishConnection(String address, int port) {
        try {
            socket = new Socket(address, port);
            scanner = new Scanner(System.in);
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.printf("Verify port. Session wasn't established: %s%n", e);
        }
    }

    private void insertName(Scanner scanner) throws IOException {
        String name;
        System.out.println("Insert your name: ");
        name = scanner.nextLine();
        outputStream.writeUTF(name);
    }

    private void closeConnection() {
        try {
            outputStream.close();
            inputStream.close();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error: " + e.getCause());
        }
    }

    private void sendMessage(String userInput) {
        while (!userInput.equals("FINISH")) {
            System.out.println("Insert message: ");
            userInput = scanner.nextLine();
            try {
                outputStream.writeUTF(userInput);
                String serverMessage = inputStream.readUTF();
                System.out.println(serverMessage);
            } catch (IOException e) {
                System.out.println("Server is down. Please insert FINISH to terminate connection!");
            }
        }
    }

    public static void main(String[] args) {
        Client client = new Client("localhost", 9999);
    }
}
