package Server;

import java.io.IOException;
import java.util.List;

public class Server {
    public static void main(String[] args) throws IOException {
        ChatServer chatServer = new ChatServer();
        chatServer.createServer();
    }
}