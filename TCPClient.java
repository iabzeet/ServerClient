import java.io.*;
import java.net.*;

public class TCPClient {
    public static void main(String[] args) {
        String serverIP = "0.tcp.in.ngrok.io"; // Replace with your ngrok forwarding address
        int serverPort = 11428; // Replace with your ngrok forwarding port

        try (Socket socket = new Socket(serverIP, serverPort);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))) {

            final String[] clientMessage = new String[1];
            final String[] serverMessage = new String[1];

            // Thread to read messages from the server
            Thread readThread = new Thread(() -> {
                try {
                    while ((serverMessage[0] = reader.readLine()) != null) {
                        System.out.println("Server: " + serverMessage[0]);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            readThread.start();

            // Main thread to send messages to the server
            while ((clientMessage[0] = consoleReader.readLine()) != null) {
                writer.println(clientMessage[0]);
            }

        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
        }
    }
}
