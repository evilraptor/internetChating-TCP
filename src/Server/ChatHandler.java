package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
public class ChatHandler extends Thread {
    private Socket clientSocket;
    private Server server;
    private PrintWriter out;

    public ChatHandler(Socket clientSocket, Server server) {
        this.clientSocket = clientSocket;
        this.server = server;
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            out = new PrintWriter(clientSocket.getOutputStream(), true);

            String nickname = in.readLine();
            System.out.println(nickname + " connected to the chat");

            server.broadcast(nickname + " joined the chat", this);

            String message;
            while ((message = in.readLine()) != null) {
                server.broadcast(nickname + ": " + message, this);
            }

            server.removeClient(this);
            System.out.println(nickname + " disconnected from the chat");

            server.broadcast(nickname + " left the chat", null);

        } catch (IOException e) {
            System.out.println("Error handling client: " + e.getMessage());
        }
    }
}
