package Server;

import java.io.*;
import java.net.*;
import java.util.List;

//TODO добавь ник
//TODO список участников чата
//TODO показывыает все сообщения, которые отправили в чат с момента подключения + некоторое число, отправленных до;
//TODO клиент отображает такие события как: подключение нового человека в чат и уход человека из чата. Сервер должен корректно понимать ситуацию отключения клиента от чата (по таймауту).
//TODO сервер должен логгировать все события, которые происходят на его стороне

public class ChatServer extends Thread
{
    // открываемый порт сервера
    private static final int port   = 6666;
    private String TEMPL_MSG = "The client '%d' sent me message : \n\t";
    private String TEMPL_CONN = "The client '%d' closed the connection";
    private List<userProfile> clients;
    private  Socket socket;
    private  int    num;

    public ChatServer() {}
    public void setSocket(int num, Socket socket)
    {
        // Определение значений
        this.num    = num;
        this.socket = socket;

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
    public void run() {
        try {
            // Определяем входной и выходной потоки сокета
            // для обмена данными с клиентом
            InputStream  sin  = socket.getInputStream();
            OutputStream sout = socket.getOutputStream();

            DataInputStream  dis = new DataInputStream (sin );
            DataOutputStream dos = new DataOutputStream(sout);

            String line = null;
            while(true) {
                // Ожидание сообщения от клиента
                line = dis.readUTF();
                System.out.println(
                        String.format(TEMPL_MSG, num) + line);
                System.out.println("I'm sending it back...");
                // Отсылаем клиенту обратно эту самую
                // строку текста
                dos.writeUTF("Server.Server receive text : " + line);
                // Завершаем передачу данных
                dos.flush();
                System.out.println();
                if (line.equalsIgnoreCase("quit")) {
                    // завершаем соединение
                    socket.close();
                    System.out.println(
                            String.format(TEMPL_CONN, num));
                    break;
                }
            }
        } catch(Exception e) {
            System.out.println("Exception : " + e);
        }
    }
    //---------------------------------------------------------
    public void createServer(){
        ServerSocket srvSocket = null;
        try {
            try {
                int i = 0; // Счётчик подключений
                // Подключение сокета к localhost
                InetAddress ia;
                ia = InetAddress.getByName("localhost");
                srvSocket = new ServerSocket(port);

                System.out.println("Server.Server started\n\n");

                while(true) {
                    // ожидание подключения
                    Socket socket = srvSocket.accept();
                    System.err.println("Client.Client accepted");
                    // Стартуем обработку клиента
                    // в отдельном потоке
                    new ChatServer().setSocket(i++, socket);
                }
            } catch(Exception e) {
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