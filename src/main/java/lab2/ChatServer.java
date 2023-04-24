package lab2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;

public class ChatServer {
    private static final int PORT = 9001;
    // Creates names
    private static HashSet<String> names = new HashSet<String>();
    // Creates writers
    private static HashMap<String, PrintWriter> writers = new HashMap<>();
    // Main method, which just listens on a port and spawns handler threads.
    public static void main(String[] args) throws Exception {
        System.out.println("The Chatty server is running.");
        ServerSocket listener = new ServerSocket(PORT);
        try {
            while (true) {
            new Handler(listener.accept()).start();
        }
    } finally {
        listener.close();
    }
}

public static class Handler extends Thread {
    // Port that the server listens on.
    private String name;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public Handler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
// Create character streams for the socket.
                in = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                String keyForEachUser = socket.getInetAddress().toString();
                // Request a name from this client.  Keep requesting until
                // a name is submitted that is not already used.  Note that
                // checking for the existence of a name and adding the name
                // must be done while locking the set of names.
                while (true) {
                    out.println("SUBMITNAME");
                    name = in.readLine();
                    if (name == null) {
                        return;
                    }
                    synchronized (names) {
                        if (!names.contains(name)) {
                            names.add(name);
                            break;
                        }
                    }
                }
                // Now that a successful name has been chosen, add the
                // socket's print writer to the set of all writers so
                // this client can receive broadcast messages.
                out.println("NAMEACCEPTED");
                writers.put(keyForEachUser, out);
                // Accept messages from this client and broadcast them.
                // Ignore other clients that cannot be broadcasted to.
                while (true) {
                    String input = in.readLine();
                    if (input == null) {
                        return;
                    }
// Where to add the section that prevents sending to original sender?
                    for (String key : writers.keySet()) {
                        if (key.equalsIgnoreCase(keyForEachUser)) {
                            //original user founf not sending the data
                        } else {
                            PrintWriter writer = writers.get(key); //getting the correct output stream
                            writer.println("MESSAGE " + name + ": " + input);
                            System.out.println(writer);
                            System.out.println("MESSAGE " + name + ": " + input);
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println(e);
            } finally {
                // This client is going down!  Remove its name and its print
                // writer from the sets, and close its socket.
                if (name != null) {
                    names.remove(name);
                }
                if (out != null) {
                    writers.remove(out);
                }
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        }
    }
}
