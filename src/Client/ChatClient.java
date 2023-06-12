package Client;

import Graphic.TextContainer;

import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class ChatClient {
    String userName;
    private int serverPort;
    private String localhost;
    private boolean graphicFlag;
    private JTextArea textArea;
    //private BufferedReader keyboard;
    private String text;
    private TextContainer textContainer;
    private String line2;

    public void changeText(String inputText) {
        text = inputText;
    }

    public ChatClient() {
        userName = "just a Potato";
        localhost = "192.168.1.59";//
        serverPort = 6666;
        graphicFlag = false;
        textArea = null;
        text = null;
    }

    public ChatClient(String inputUserName, String inputLocalhost, int inputServerPort, JTextArea inputTextArea, boolean inputGraphicFlag) {
        userName = inputUserName;
        localhost = inputLocalhost;
        serverPort = inputServerPort;
        if ((!inputGraphicFlag) || (inputTextArea == null)) {
            graphicFlag = false;
            textArea = null;
        } else {
            graphicFlag = inputGraphicFlag;
            textArea = inputTextArea;
        }
    }

    public ChatClient(String inputUserName, String inputLocalhost, int inputServerPort, JTextArea inputTextArea, boolean inputGraphicFlag, TextContainer inputTextContainer) {
        userName = inputUserName;
        localhost = inputLocalhost;
        serverPort = inputServerPort;
        if ((!inputGraphicFlag) || (inputTextArea == null) || (inputTextContainer == null)) {
            graphicFlag = false;
            textArea = null;
            text = null;
        } else {
            graphicFlag = inputGraphicFlag;
            textArea = inputTextArea;
            textContainer = inputTextContainer;
        }
    }

    public void startClient() {
        Socket socket = null;
        try {
            try {
                System.out.println("It's Client.Client side\nConnecting to the server\n\t (IP address: " +
                        localhost + ", port: " + serverPort + ")");
                InetAddress ipAddress = InetAddress.getByName(localhost);
                socket = new Socket(ipAddress, serverPort);
                System.out.println(
                        "Client.Client was connected.");
                System.out.println(
                        "\tLocalPort = " + socket.getLocalPort() + "\n\tInetAddress.HostAddress = "
                                + socket.getInetAddress().getHostAddress());
                //входной и выходной потоки сокета для обмена сообщениями с сервером
                InputStream inputStream = socket.getInputStream();
                OutputStream outputStream = socket.getOutputStream();

                DataInputStream in = new DataInputStream(inputStream);
                DataOutputStream out = new DataOutputStream(outputStream);
                //поток для чтения с клавиатуры.
                InputStreamReader inputStreamReader;
                inputStreamReader = new InputStreamReader(System.in);

                BufferedReader keyboard = new BufferedReader(inputStreamReader);
                System.out.println("Ready to chat. Type something and press enter... (for example Info)");
                out.writeUTF(userName);
                out.flush();
                appendMessageOnTextArea(userName + "\n");
                Timer timer = new Timer(30000, e -> {
                    line2 = "/quit";
                    try {
                        sendMessageFromString(line2,in,out);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
                timer.setRepeats(false);
                timer.start();
                sendMessageFromString("/showHistory",in,out);

                while (true) {
                    if ((line2 != null) && (line2.endsWith("/quit"))) {
                        break;
                    }

                    if (keyboard.ready() && (!graphicFlag)) {
                        line2 = sendMessage(keyboard, in, out);
                        timer.restart();
                    } else if (in.available() > 0) {
                        readNewMessages(in);
                    } else if ((graphicFlag)) {
                        line2 = sendMessageFromTextContainer(textContainer, in, out);
                        timer.restart();
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            try {
                if (socket != null)
                    socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    String sendMessage(BufferedReader keyboard, DataInputStream in, DataOutputStream out) throws IOException {
        String line = null;
        //ввел строку и нажал Enter
        line = keyboard.readLine();
        // Отсылаем строку серверу
        out.writeUTF(line);
        // Завершаем поток
        out.flush();
        // Ждем ответа от сервера
        line = readNewMessages(in);
        appendMessageOnTextArea(line);
        return line;
    }

    String sendMessageFromTextContainer(TextContainer inTextContainer, DataInputStream in, DataOutputStream out) throws IOException {
        String line = null;
        //ввел строку и нажал Enter
        line = textContainer.getText();
        //line = keyboard.readLine();
        // Отсылаем строку серверу
        if (!line.equals("")) {
            out.writeUTF(line);
            // Завершаем поток
            out.flush();
            // Ждем ответа от сервера
            line = readNewMessages(in);
            appendMessageOnTextArea(line);
            text = "";
            inTextContainer.setText(text);
        }
        return line;
    }

    String sendMessageFromString(String text, DataInputStream in, DataOutputStream out) throws IOException {
        String line = text;
        out.writeUTF(line);
        // Завершаем поток
        out.flush();
        // Ждем ответа от сервера
        line = readNewMessages(in);
        appendMessageOnTextArea(line);
        return line;
    }

    String readNewMessages(DataInputStream in) throws IOException {
        String line = null;
        line = in.readUTF();
        if (line.equals("/quit"))
            return line;
        else if (line.equals("/usersList")) {
            line = in.readUTF();
            int clientsCount = Integer.parseInt(line);
            System.out.println("The server sent clients count:" + clientsCount + "\n\t");
            for (int i = 0; i < clientsCount; i++) {
                line = in.readUTF();
                System.out.println(line);
            }
            return line;
        } else if (line.equals("/showHistory")) {
            line = in.readUTF();
            System.out.println(line);
            return line;
        } else {
            System.out.println(line);
            appendMessageOnTextArea(line);
            //in.reset();
            return line;
        }
    }

    public void appendMessageOnTextArea(String text) {
        if (graphicFlag)
            textArea.append(text + "\n");
    }
}
