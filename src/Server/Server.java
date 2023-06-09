package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private int port;
    private List<ChatHandler> clients;

    public Server(int port) {
        this.port = port;
        this.clients = new ArrayList();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                ChatHandler handler = new ChatHandler(clientSocket, this);
                clients.add(handler);
                handler.start();
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }

    public void broadcast(String message, ChatHandler excludeHandler) {
        for (ChatHandler handler : clients) {
            if (handler != excludeHandler) {
                handler.sendMessage(message);
            }
        }
    }

    public void removeClient(ChatHandler handler) {
        clients.remove(handler);
    }

    public static void main(String[] args) {
        Server server = new Server(1234);
        server.start();
    }
}
