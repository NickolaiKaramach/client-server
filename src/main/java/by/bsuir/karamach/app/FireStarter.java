package by.bsuir.karamach.app;

import by.bsuir.karamach.client.ClientWindow;
import by.bsuir.karamach.server.Server;
import by.bsuir.karamach.server.ServerException;
import by.bsuir.karamach.util.GUIReader;

import javax.swing.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class FireStarter {

    private static final String README_MESSAGE =
            "Please input a command to execute:\n" +
                    "1. next - to open new client window.\n" +
                    "2. exit - to leave app.";

    private static final String COMMAND_NEXT = "next";
    private static final String COMMAND_EXIT = "exit";
    public static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {

        try {

            serverCreationCycle();

            TimeUnit.SECONDS.sleep(2);

            clientWindowCreationCycle();

        } catch (Throwable e) {

            System.out.println("Sorry, an error occurred:\n" + e.getMessage());
            e.printStackTrace();

        }
    }

    private static void serverCreationCycle() {
        Thread serverThread = new Thread(() -> {

            lock.lock();

            boolean isStartedSuccessfully = false;

            do {

                try {

                    int port = GUIReader.readPort();

                    Server server = new Server(port);

                    server.startup();

                    isStartedSuccessfully = true;

                } catch (ServerException e) {

                    e.printStackTrace();

                    JOptionPane.showMessageDialog(null, e.getMessage() + "\n Try again.");
                }

            } while (!isStartedSuccessfully);

            lock.unlock();
        });

        serverThread.start();

        lock.lock();
    }

    private static void clientWindowCreationCycle() {
        String command;

        //noinspection InfiniteLoopStatement
        while (true) {

            command = JOptionPane.showInputDialog(README_MESSAGE);

            if (command != null) {
                proceedCommand(command);
            }

        }
    }

    private static void proceedCommand(String command) {
        switch (command.toLowerCase()) {

            case COMMAND_NEXT:

                makeNewClientWindow();
                break;

            case COMMAND_EXIT:

                System.exit(0);
                break;

            default:

                JOptionPane.showMessageDialog(null, "Invalid parameter");
                break;

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
