package Client;

import Graphic.TextContainer;
import Server.ChatHistoryManager;

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

    public Client(String text1, String text2, String text3, JTextArea inputTextArea, TextContainer textContainer, ChatHistoryManager ChatHistoryManager) {
        ChatClient chatClient = new ChatClient(text1, text2, Integer.parseInt(text3), inputTextArea, true, textContainer,ChatHistoryManager);
        inputTextArea.append("подключение успешно!\n");
        chatClient.startClient();
    }
}