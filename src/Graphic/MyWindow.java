package Graphic;

import Client.Client;
import Server.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.ref.Cleaner;

public class MyWindow extends JFrame {
    private JButton serverButton, clientButton;
    private JTextField textField1, textField2, textField3;
    private JTextArea textArea;
    JScrollPane scrollPane;
    public MyWindow() {
        super("the chat server/client window");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Создание кнопки
        serverButton = new JButton("start the server!");
        clientButton = new JButton("join a server!");

        textArea = new JTextArea();
        textArea.setEditable(false);

        scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        serverButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //clientButton.setVisible(false);//FIXME здесь кнопка вторая про другой режим удаляется слишком рано

                // Создание панели для ввода текста
                JPanel panel = new JPanel();
                panel.add(new JLabel("write the server's port"));
                textField1 = new JTextField(10);
                panel.add(textField1);

                // Создание диалогового окна для ввода текста
                int result = JOptionPane.showConfirmDialog(MyWindow.this, panel, "creating the server", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    // Создание нового экземпляра класса А и вызов его метода
                    //A a = new A();
                    //a.printText(textField1.getText());

                    //new Server(textField1.getText());
                }
            }
        });
        clientButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Создание панели для ввода текста
                JPanel panel = new JPanel();
                panel.add(new JLabel("userName"));
                textField1 = new JTextField(10);
                panel.add(textField1);
                panel.add(new JLabel("server's ip"));
                textField2 = new JTextField(10);
                panel.add(textField2);
                panel.add(new JLabel("server's port"));
                textField3 = new JTextField(10);
                panel.add(textField3);

                // Создание диалогового окна для ввода текста
                int result = JOptionPane.showConfirmDialog(MyWindow.this, panel, "joining the server", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    // Создание нового экземпляра класса А и вызов его метода
                    //B b = new B();
                    //b.printText(textField1.getText(), textField2.getText());
                    //new Client(textField1.getText(), textField2.getText(),textField3.getText());
                    Thread daemonThread = new Thread(new Runnable() {
                        public void run() {
                            Client client = new Client(textField1.getText(), textField2.getText(),textField3.getText(),textArea);
                        }
                    });
                    daemonThread.setDaemon(true);
                    daemonThread.start();
                }
            }
        });

        // Добавление компонентов на панель
        JPanel panel = new JPanel();
        panel.add(serverButton);
        panel.add(clientButton);
        textArea.append("окно чата:\n");
        textArea.setPreferredSize(new Dimension(200, 100));
        panel.add(scrollPane);

        // Добавление панели на окно
        getContentPane().add(panel);

        // Настройка размеров окна и его видимости
        setSize(300, 400);
        setVisible(true);
    }

    public static void main(String[] args) {
        new MyWindow();
    }

    public void setNewTextToTextArea(String text){

    }
}