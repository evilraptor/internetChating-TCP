import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Predicate;

class ChatClient extends Thread {
    private Socket socket = null;
    private ChatServer server = null;
    private String name;
    public ChatClient(Socket s, ChatServer srv) {
        socket = s;
        server = srv;
    }

    public Socket getSocket() {return socket; }
    public void sendMessage(String message) {
        try {
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println(message);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void run() {
        try {
            Scanner s = new Scanner(socket.getInputStream());
            String handshake = s.nextLine();
            if (!handshake.startsWith("name:")) {
                socket.close();
                return;
            }
            name = handshake.substring(5);
            while (true) {
                try {
                    server.SendMessage(this, name + " sent: " + s.nextLine());
                } catch (Exception e) {
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }
};

class ChatServer {
    private ServerSocket serverSocket;
    private ArrayList<ChatClient> clients = new ArrayList<>();
    public void Run() throws IOException {
        serverSocket = new ServerSocket(27015);
        System.out.println(serverSocket.getInetAddress().getHostAddress());

        while (true) {
            Socket socket = serverSocket.accept();
            ChatClient client = new ChatClient(socket, this);
            clients.add(client);
            client.start();
        }

    }
    public synchronized void SendMessage(ChatClient from ,String s) {
        System.out.println(s);
        clients.forEach(new Consumer<ChatClient>() {
            @Override
            public void accept(ChatClient chatClient) {
                if (chatClient != from) {
                    chatClient.sendMessage(s);
                }
            }
        });
    }
};


public class Main {
    public static void main(String[] args) throws Exception {
        ChatServer chatServer = new ChatServer();
        chatServer.Run();
    }
}