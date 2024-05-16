import java.io.*;
import java.net.*;

public class TCPServer {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(9999)) {
            System.out.println("Server is listening on port 9999");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");

                new ServerHandler(socket).start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

class ServerHandler extends Thread {
    private Socket socket;

    public ServerHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))) {

            final String[] clientMessage = new String[1];
            final String[] serverMessage = new String[1];

            // Thread to read messages from the client
            Thread readThread = new Thread(() -> {
                try {
                    while ((clientMessage[0] = reader.readLine()) != null) {
                        System.out.println("Client: " + clientMessage[0]);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            readThread.start();

            // Main thread to send messages to the client
            while ((serverMessage[0] = consoleReader.readLine()) != null) {
                writer.println(serverMessage[0]);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
