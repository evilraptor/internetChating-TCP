package Graphic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MyWindow extends JFrame implements ActionListener {
    private JLabel portLabel, ipLabel;
    private JTextField portTextField, ipTextField;
    private JButton serverButton, clientButton;

    private JPanel panel1,panel2,panel3;

    public MyWindow() {
        super("chat application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // установка размеров окна
        setSize(400, 200);

        // создание панелей для размещения компонентов
        panel1 = new JPanel(new FlowLayout());
        panel2 = new JPanel(new FlowLayout());
        panel3 = new JPanel(new BorderLayout());

        // создание кнопок и добавление обработчика событий
        serverButton = new JButton("Server");
        serverButton.addActionListener(this);
        clientButton = new JButton("Client");
        clientButton.addActionListener(this);


        /*portLabel = new JLabel("port:");
        ipLabel = new JLabel("ip:");

        portTextField = new JTextField(15);
        ipTextField = new JTextField(15);*/

        /*// создание кнопок и добавление обработчика событий
        buttonA = new JButton("A");
        buttonA.addActionListener(this);
        buttonB = new JButton("B");
        buttonB.addActionListener(this);

        // создание панелей для размещения компонентов
        JPanel panel1 = new JPanel(new FlowLayout());
        JPanel panel2 = new JPanel(new FlowLayout());
        JPanel panel3 = new JPanel(new BorderLayout());*/

        /*// добавление компонентов на первую панель
        panel1.add(portLabel);
        panel1.add(portTextField);

        // добавление компонентов на вторую панель
        panel2.add(ipLabel);
        panel2.add(ipTextField);*/

        // добавление кнопок на третью панель
        JPanel panelButtons = new JPanel(new FlowLayout());
        panelButtons.add(serverButton);
        panelButtons.add(clientButton);

        // добавление панелей на основную панель
        panel3.add(panel1, BorderLayout.NORTH);
        panel3.add(panel2, BorderLayout.CENTER);
        panel3.add(panelButtons, BorderLayout.SOUTH);

        // добавление основной панели на окно
        setContentPane(panel3);

        // отображение окна
        setVisible(true);
    }

    // обработчик событий для кнопок A и B
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == serverButton) {
            // создание компонентов
            portLabel = new JLabel("port:");
            portTextField = new JTextField(15);

            // добавление компонентов на первую панель
            panel1.add(portLabel);
            panel1.add(portTextField);

            // запуск сервера
            new A(portTextField.getText());
        } else if (e.getSource() == clientButton) {
            // создание компонентов
            portLabel = new JLabel("port:");
            ipLabel = new JLabel("ip:");

            portTextField = new JTextField(15);
            ipTextField = new JTextField(15);

            // добавление компонентов на первую панель
            panel1.add(portLabel);
            panel1.add(portTextField);

            // добавление компонентов на вторую панель
            panel2.add(ipLabel);
            panel2.add(ipTextField);

            // запуск клиента
            new B(portTextField.getText(), ipTextField.getText());
        }
    }
}

class A {
    public A(String text1) {
        System.out.println("Класс A запущен.");
        System.out.println("Текст 1: " + text1);
    }
}

class B {
    public B(String text1, String text2) {
        System.out.println("Класс B запущен.");
        System.out.println("Текст 1: " + text1);
        System.out.println("Текст 2: " + text2);
    }
}
