package Server;

import java.io.PrintWriter;
import java.net.Socket;

public class UserProfile {
    private String userName;
    private Socket clientSocket;
    //private Server server;
    //private PrintWriter out;


    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }
}
