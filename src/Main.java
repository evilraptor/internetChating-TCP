import java.net.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        //ChatClient chatClient = new ChatClient();
        //chatClient.startClient();
        ChatServer chatServer=new ChatServer();
        chatServer.createServer();
    }
}