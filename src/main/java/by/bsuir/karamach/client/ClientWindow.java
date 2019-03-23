package by.bsuir.karamach.client;

import by.bsuir.karamach.connection.TcpConnection;
import by.bsuir.karamach.connection.TcpConnectionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ClientWindow extends JFrame implements ActionListener, TcpConnectionListener {

    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;

//    public static void main(String[] args) {
//        // swing работает из потока edt
//        // выполняет эту строку в потоке edt
//
//    }

    private final JTextArea textArea = new JTextArea();
    private final JTextField userNameField = new JTextField("User");
    private final JTextField messageField = new JTextField("Hello world!");

    private TcpConnection connection;

    private String ip;
    private int port;

    public ClientWindow(String ip, int port) throws IOException {
        setUpGUI();

        this.ip = ip;
        this.port = port;
        connection = new TcpConnection(this, ip, port);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String msg = messageField.getText();

        if (msg.equals("")) {
            return;
        }

        messageField.setText(null);
        connection.sendString(userNameField.getText() + ": " + msg);
    }

    @Override
    public void onConnectionReady(TcpConnection tcpConnection) {
        printMsg("Connection ready...");
    }

    @Override
    public void onReceiveString(TcpConnection tcpConnection, String value) {
        printMsg(value);
    }

    @Override
    public void onDisconnect(TcpConnection tcpConnection) {
        printMsg("Connection close...");
    }

    @Override
    public void onException(TcpConnection tcpConnection, Exception e) {
        printMsg("Connection exception: " + e);
    }

    private void setUpGUI() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);

        textArea.setEditable(false);
        textArea.setLineWrap(true);
        add(textArea, BorderLayout.CENTER);

        messageField.addActionListener(this);
        add(messageField, BorderLayout.SOUTH);
        add(userNameField, BorderLayout.NORTH);

        setVisible(true);
    }

    private synchronized void printMsg(String msg) {
        SwingUtilities.invokeLater(() -> {
            textArea.append(msg + "\n");
            textArea.setCaretPosition(textArea.getDocument().getLength());
        });
    }

    private synchronized void printError(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }
}
