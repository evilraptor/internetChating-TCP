package Server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO добавь ник (чек)
//TODO список участников чата
//TODO показывыает все сообщения, которые отправили в чат с момента подключения + некоторое число, отправленных до;
//TODO клиент отображает такие события как: подключение нового человека в чат и уход человека из чата. Сервер должен корректно понимать ситуацию отключения клиента от чата (по таймауту).
//TODO сервер должен логгировать все события, которые происходят на его стороне

public class ChatServer extends Thread {
    // открываемый порт сервера
    private static final int port = 6666;
    private String TEMPL_MSG = "The client '%s' sent me message : \n\t";
    private String TEMPL_CONN = "The client '%s' closed the connection";
    private Map<Integer, UserProfile> clients = new HashMap<>();
    private Socket socket;
    private int num;

    public ChatServer() {
    }

    public void setSocket(int num, Socket socket, Map<Integer, UserProfile> serverClients) {
        // Определение значений
        this.num = num;
        this.socket = socket;
        clients = serverClients;
        // Установка daemon-потока
        setDaemon(true);
        /*
         * Определение стандартного приоритета главного потока
         * int java.lang.Thread.NORM_PRIORITY = 5-the default
         *               priority that is assigned to a thread.
         */
        setPriority(NORM_PRIORITY);
        // Старт потока
        start();
    }

    @Override
    public void run() {
        try {
            // Определяем входной и выходной потоки сокета
            // для обмена данными с клиентом
            InputStream sin = socket.getInputStream();
            OutputStream sout = socket.getOutputStream();

            DataInputStream dis = new DataInputStream(sin);
            DataOutputStream dos = new DataOutputStream(sout);

            String line = null;
            while (true) {
                // Ожидание сообщения от клиента
                line = dis.readUTF();
                System.out.println(
                        String.format(TEMPL_MSG, clients.get(num).getUserName()) + line);
                System.out.println("I'm sending it back...");
                // Отсылаем клиенту обратно эту самую
                // строку текста
                dos.writeUTF("Server.Server receive text: " + line);
                // Завершаем передачу данных
                dos.flush();
                System.out.println();

                //поймали слово выхода
                if (line.equalsIgnoreCase("/quit")) {
                    // завершаем соединение
                    socket.close();
                    System.out.println(
                            String.format(TEMPL_CONN, clients.get(num).getUserName()));
                    clients.remove(num);
                    break;
                }

                if (line.equalsIgnoreCase("/usersList")) {
                    int clientsCount=clients.size();
                    dos.writeUTF("Clients count: " + clientsCount);
                    dos.flush();
                    for (Map.Entry<Integer, UserProfile> entry : clients.entrySet()) {
                        dos.writeUTF("Id: " + entry.getKey() + ", userName: " + entry.getValue());
                        dos.flush();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Exception : " + e);
        }
    }

    //---------------------------------------------------------
    public void createServer() {
        clients = new HashMap<Integer, UserProfile>();
        ServerSocket srvSocket = null;
        try {
            try {
                int i = 0; // Счётчик подключений
                // Подключение сокета к localhost
                InetAddress ia;
                ia = InetAddress.getByName("localhost");
                srvSocket = new ServerSocket(port);

                System.out.println("Server.Server started\n\n");

                while (true) {
                    // ожидание подключения
                    Socket socket = srvSocket.accept();

                    //получаем юсернейм
                    String userName = null;
                    DataInputStream dis = new DataInputStream(socket.getInputStream());
                    userName = dis.readUTF();
                    System.err.println(userName + " accepted");

                    //добавляем человека в лист юзеров
                    UserProfile newUser = new UserProfile();
                    newUser.setUserName(userName);
                    clients.put(i, newUser);

                    // Стартуем обработку клиента
                    // в отдельном потоке
                    new ChatServer().setSocket(i++, socket, clients);
                }
            } catch (Exception e) {
                System.out.println("Exception : " + e);
            }
        } finally {
            try {
                if (srvSocket != null)
                    srvSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.exit(0);
    }
}