package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class UserProfile {
    private String userName;
    private Socket clientSocket;
    private DataInputStream dis;
    private DataOutputStream dos;

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

    public void setDis(DataInputStream dis) {
        this.dis = dis;
    }

    public DataInputStream getDis() {
        return dis;
    }

    public void setDos(DataOutputStream dos) {
        this.dos = dos;
    }

    public DataOutputStream getDos() {
        return dos;
    }
}
