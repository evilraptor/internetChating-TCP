package Server;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

//TODO добавь ник (чек)
//TODO список участников чата (чек)
//TODO показывыает все сообщения, которые отправили в чат с момента подключения + некоторое число, отправленных до (чек(идет из конфига))
//TODO клиент отображает такие события как: подключение нового человека в чат и уход человека из чата. Сервер должен корректно понимать ситуацию отключения клиента от чата (по таймауту) (чек)
//TODO сервер должен логгировать все события, которые происходят на его стороне (чек)

public class ChatServer extends Thread {
    // открываемый порт сервера
    private int port;
    private String TEMPL_MSG = "The client '%s' sent: ";
    private String TEMPL_CONN = "The client '%s' closed the connection";
    private Map<Integer, UserProfile> clients = new HashMap<>();
    private Socket socket;
    private int num;
    private ChatHistoryManager chatHistoryManager;
    private int max_count_of_giving_messages;
    private boolean saving_log_flag;

    public ChatServer() {
        port = 6666;
        chatHistoryManager = new ChatHistoryManager();
        max_count_of_giving_messages = chatHistoryManager.getMax_count_of_giving_messages();
        saving_log_flag = chatHistoryManager.isSaving_log_flag();
    }

    public ChatServer(int inPort) {
        port = inPort;
        chatHistoryManager = new ChatHistoryManager();
        max_count_of_giving_messages = chatHistoryManager.getMax_count_of_giving_messages();
        saving_log_flag = chatHistoryManager.isSaving_log_flag();
    }

    public void setSocket(int num, Socket socket, Map<Integer, UserProfile> serverClients, ChatHistoryManager inputChatHistoryManager, int input_max_count_of_giving_messages, boolean input_saving_log_flag) {
        // Определение значений
        this.num = num;
        this.socket = socket;
        this.clients = serverClients;
        this.chatHistoryManager = inputChatHistoryManager;
        this.max_count_of_giving_messages = input_max_count_of_giving_messages;
        this.saving_log_flag = input_saving_log_flag;

        // Установка daemon-потока
        setDaemon(true);
        setPriority(NORM_PRIORITY);
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

            clients.get(num).setDis(dis);
            clients.get(num).setDos(dos);
            String userName = clients.get(num).getUserName();
            String line = null;

            while (true) {
                if (dis.available() > 0) {
                    // Ожидание сообщения от клиента
                    line = dis.readUTF();
                    System.out.println(
                            String.format(TEMPL_MSG, clients.get(num).getUserName()) + line);
                    chatHistoryManager.addToHistory(String.format(TEMPL_MSG, clients.get(num).getUserName()) + line);
                    // Отсылаем клиентам эту самую строку текста
                    for (Map.Entry<Integer, UserProfile> entry : clients.entrySet()) {
                        entry.getValue().getDos().writeUTF(userName + ": " + line);
                        dos.flush();
                    }
                    //поймали слово выхода
                    if (line.equals("/quit")) {
                        // завершаем соединение
                        socket.close();
                        String leftUserName = clients.get(num).getUserName();
                        System.out.println(String.format(TEMPL_CONN, leftUserName));
                        clients.remove(num);
                        if (saving_log_flag)
                            chatHistoryManager.addToLog(String.format(TEMPL_CONN, leftUserName) + "\n now several counts of users: " + clients.size() + "\n");
                        for (Map.Entry<Integer, UserProfile> entry : clients.entrySet()) {
                            entry.getValue().getDos().writeUTF(userName + " left the server");
                            dos.flush();
                        }
                        break;
                    } else if (line.equals("/usersList")) {
                        int clientsCount = clients.size();
                        dos.writeUTF("" + clientsCount);
                        dos.flush();
                        if (saving_log_flag)
                            chatHistoryManager.addToLog(userName + "\n asked for /userList\n");
                        for (Map.Entry<Integer, UserProfile> entry : clients.entrySet()) {
                            dos.writeUTF("Id: " + entry.getKey() + ", userName: " + entry.getValue().getUserName());
                            dos.flush();
                        }
                    } else if (line.equals("/showHistory")) {
                        dos.writeUTF(chatHistoryManager.getLastMessages(max_count_of_giving_messages));
                        dos.flush();
                        if (saving_log_flag)
                            chatHistoryManager.addToLog(userName + "\n asked for /showHistory\n");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(this.clients.get(this.num).getUserName() + "send: " + "Exception : " + e);

            System.out.println(String.format(TEMPL_CONN, clients.get(num).getUserName()));
            clients.remove(num);
            for (Map.Entry<Integer, UserProfile> entry : clients.entrySet()) {
                try {
                    entry.getValue().getDos().writeUTF(clients.get(num).getUserName() + " left the server");
                    entry.getValue().getDos().flush();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

            this.clients.remove(this.num);
        }
    }

    //---------------------------------------------------------
    public void createServer() {
        chatHistoryManager.deleteHistory();
        chatHistoryManager.deleteLog();
        chatHistoryManager.addToLog("Server started\n");
        clients = new HashMap<Integer, UserProfile>();
        ServerSocket srvSocket = null;
        int countOfShowingMessages = 5;
        try {
            try {
                int i = 0; // Счётчик подключений
                // Подключение сокета к localhost
                //InetAddress ia;
                //ia = InetAddress.getByName("192.168.1.59");
                srvSocket = new ServerSocket(port);
                System.out.println("Server started " + "\n\n");

                while (true) {
                    // ожидание подключения
                    Socket socket = srvSocket.accept();
                    //получаем юсернейм
                    String userName = null;
                    DataInputStream dis = new DataInputStream(socket.getInputStream());
                    userName = dis.readUTF();
                    System.err.println(userName + " accepted");
                    chatHistoryManager.addToLog(userName + " accepted\n\tuser ID: " + i + "\nnow several counts of users: " + i + "\n");

                    for (Map.Entry<Integer, UserProfile> entry : clients.entrySet()) {
                        try {
                            entry.getValue().getDos().writeUTF(userName + " accepted");
                            entry.getValue().getDos().flush();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }

                    //добавляем человека в лист юзеров
                    UserProfile newUser = new UserProfile();
                    newUser.setUserName(userName);
                    clients.put(i, newUser);

                    // Стартуем обработку клиента
                    // в отдельном потоке
                    new ChatServer().setSocket(i++, socket, clients, chatHistoryManager,max_count_of_giving_messages,saving_log_flag);

                    BufferedReader keyboard;
                    keyboard = new BufferedReader(new InputStreamReader(System.in));
                    String line;
                    if (keyboard.ready()) {
                        line = keyboard.readLine();
                        if (line.equals("/stopServer")) {
                            chatHistoryManager.closeHistory();
                            chatHistoryManager.closeLog();
                            return;
                        }
                    }
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
        chatHistoryManager.addToLog("Server exited 0\n");
        System.exit(0);
    }
}