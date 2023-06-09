package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private String host;
    private int port;
    private String nickname;

    public Client(String host, int port, String nickname) {
        this.host = host;
        this.port = port;
        this.nickname = nickname;
    }

    public void start() {
        try (Socket socket = new Socket(host, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            System.out.println("Connected to server");

            out.println(nickname);

            new Thread(new UserInputHandler(out)).start();

            String message;
            while ((message = in.readLine()) != null) {
                System.out.println(message);
            }

        } catch (IOException e) {
            System.out.println("Client error: " + e.getMessage());
        }
    }

    private static class UserInputHandler implements Runnable {
        private PrintWriter out;

        public UserInputHandler(PrintWriter out) {
            this.out = out;
        }

        @Override
        public void run() {
            try (BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {
                String input;
                while ((input = userInput.readLine()) != null) {
                    out.println(input);
                }
            } catch (IOException e) {
                System.out.println("User input error: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        Client client = new Client("localhost", 1234, "John");
        client.start();
    }
}
