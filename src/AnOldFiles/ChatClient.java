package AnOldFiles;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class ChatClient {
    String userName = "just a Potato";
    private static final int serverPort = 6666;
    private static final String localhost = "192.168.1.59";

    public void startClient() {
        Socket socket = null;
        try {
            try {
                System.out.println("It's AnOldFiles.Client side\nConnecting to the server\n\t (IP address: " +
                        localhost + ", port: " + serverPort + ")");
                InetAddress ipAddress = InetAddress.getByName(localhost);
                socket = new Socket(ipAddress, serverPort);
                System.out.println(
                        "AnOldFiles.Client was connected.");
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
                BufferedReader keyboard;
                keyboard = new BufferedReader(inputStreamReader);
                String line = null;
                System.out.println("Ready to chat. Type something and press enter... (for example Info)");
                while (true) {
                    line=sendMessage(keyboard, in, out);
                    //System.out.println("sda");
                    //readNewMessages(in);
                    //break;
                    if ((line != null) && (line.endsWith("quit"))) {
                        //if (out.equals("quit"))
                        break;
                    }
                }

                /*while (true) {
                    //ввел строку и нажал Enter
                    line = keyboard.readLine();
                    // Отсылаем строку серверу
                    out.writeUTF(line);
                    // Завершаем поток
                    out.flush();
                    // Ждем ответа от сервера
                    line = in.readUTF();
                    if (line.endsWith("quit"))
                        break;
                    else {
                        System.out.println("The server sent:\n\t" + line);
                    }
                }*/
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
        return line;
    }

    String readNewMessages(DataInputStream in) throws IOException {
        String line = null;
        line = in.readUTF();
        if (line.endsWith("quit"))
            return line;
        else {
            System.out.println("The server sent:\n\t" + line);
            return line;
        }
    }
}
