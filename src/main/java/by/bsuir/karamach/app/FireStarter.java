package by.bsuir.karamach.app;

import by.bsuir.karamach.client.ClientWindow;
import by.bsuir.karamach.server.Server;
import by.bsuir.karamach.util.GUIReader;

import javax.swing.*;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

public class FireStarter {

    private static final String README_MESSAGE =
            "Please input a command to execute:\n" +
                    "1. next - to open new client window.\n" +
                    "2. exit - to leave app.";

    private static final String COMMAND_NEXT = "next";
    private static final String COMMAND_EXIT = "exit";
    private static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {

        try {

            serverCreationCycle();

            clientWindowCreationCycle();

        } catch (Throwable e) {

            System.out.println("Sorry, an error occurred:\n" + e.getMessage());
            e.printStackTrace();

        }
    }

    private static void serverCreationCycle() {
        Thread serverThread = new Thread(() -> {

            lock.lock();

            Server server = new Server(GUIReader.readPort(), lock);

            server.startup();

            lock.unlock();
        });

        serverThread.start();

        lock.lock();
    }

    private static void clientWindowCreationCycle() {
        String command;
        boolean isActive = true;

        while (isActive) {

            command = JOptionPane.showInputDialog(README_MESSAGE);

            switch (command.toLowerCase()) {

                case COMMAND_NEXT:

                    makeNewClientWindow();
                    break;

                case COMMAND_EXIT:

                    isActive = false;
                    break;

                default:

                    JOptionPane.showMessageDialog(null, "Invalid parameter");
                    break;

            }

        }
    }

    private static void makeNewClientWindow() {
        Thread clientThread = new Thread(() -> SwingUtilities.invokeLater(() -> {
            try {
                new ClientWindow(GUIReader.readIp(), GUIReader.readPort());

            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }));

        clientThread.start();
    }
}
