package Client;

import javax.swing.*;
import java.io.*;

public class Client {
    public static void main(String[] args) throws IOException {
        ChatClient chatClient = new ChatClient();
        chatClient.startClient();
    }

    public Client() {
        ChatClient chatClient = new ChatClient();
        chatClient.startClient();
    }

    public Client(String text1, String text2, String text3) {
        ChatClient chatClient = new ChatClient(text1, text2, Integer.parseInt(text3),null,false);
        chatClient.startClient();
    }
    public Client(String text1, String text2, String text3, JTextArea inputTextArea) {
        ChatClient chatClient = new ChatClient(text1, text2, Integer.parseInt(text3),inputTextArea,true);
        chatClient.startClient();
    }
}