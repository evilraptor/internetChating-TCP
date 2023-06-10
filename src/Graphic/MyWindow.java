package Graphic;

import javax.swing.*;
import java.awt.event.*;

public class MyWindow extends JFrame {
    private JButton serverButton, clientButton;
    private JTextField textField1, textField2;
    private JTextArea textArea;

    public MyWindow() {
        super("the chat server/client window");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Создание кнопки
        serverButton = new JButton("start the server!");
        clientButton = new JButton("join a server!");
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
                    A a = new A();
                    a.printText(textField1.getText());
                }
            }
        });
        clientButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Создание панели для ввода текста
                JPanel panel = new JPanel();
                panel.add(new JLabel("write the server's ip"));
                textField1 = new JTextField(10);
                panel.add(textField1);
                panel.add(new JLabel("write the server's port"));
                textField2 = new JTextField(10);
                panel.add(textField2);

                // Создание диалогового окна для ввода текста
                int result = JOptionPane.showConfirmDialog(MyWindow.this, panel, "joining the server", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    // Создание нового экземпляра класса А и вызов его метода
                    B b = new B();
                    b.printText(textField1.getText(), textField2.getText());
                }
            }
        });

        // Добавление компонентов на панель
        JPanel panel = new JPanel();
        panel.add(serverButton);
        panel.add(clientButton);

        // Добавление панели на окно
        getContentPane().add(panel);

        // Настройка размеров окна и его видимости
        setSize(300, 400);
        setVisible(true);
    }

    public static void main(String[] args) {
        new MyWindow();
    }
}

class A {
    public void printText(String text1) {
        System.out.println(text1);
    }
}

class B {
    public void printText(String text1, String text2) {
        System.out.println(text1 + " " + text2);
    }
}