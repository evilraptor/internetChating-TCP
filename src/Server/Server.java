package Server;

import java.io.IOException;

public abstract class Server implements Runnable {
    public static void main(String[] args) throws IOException {
        ChatServer chatServer = new ChatServer();
        chatServer.createServer();
    }
    public Server() {
        ChatServer chatServer = new ChatServer();
        chatServer.createServer();
    }

    public Server(String text1) {
        ChatServer chatServer = new ChatServer(Integer.parseInt(text1));
        chatServer.createServer();
    }
}