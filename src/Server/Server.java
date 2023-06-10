package Server;

import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        System.out.println("Chat server started on port 1234");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress());

            Thread clientThread = new Thread(new ClientHandler(clientSocket));
            clientThread.start();
        }
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;

    public ClientHandler(Socket socket) throws IOException {
        this.clientSocket = socket;
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream(), true);
    }

    public void run() {
        try {
            while (true) {
                String inputLine = in.readLine();
                if (inputLine == null) {
                    break;
                }
                System.out.println("Message received from " + clientSocket.getInetAddress().getHostAddress() + ": " + inputLine);
                out.println("You said: " + inputLine);
            }
        } catch (IOException e) {
            System.out.println("Error handling client: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Error closing client socket: " + e.getMessage());
            }
            System.out.println("Client disconnected: " + clientSocket.getInetAddress().getHostAddress());
        }
    }
}
