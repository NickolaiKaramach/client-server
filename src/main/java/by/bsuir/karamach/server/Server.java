package by.bsuir.karamach.server;

import by.bsuir.karamach.connection.TcpConnection;
import by.bsuir.karamach.connection.TcpConnectionListener;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Server implements TcpConnectionListener {

    private final List<TcpConnection> connections = new ArrayList<>();

    private int port;

    public Server(int port) {
        this.port = port;
    }

//    public static void main(String[] args) {
//        Server server = new Server();
//        server.startup();
//    }

    public void startup() {
        JOptionPane.showMessageDialog(null, "Server is running...");

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            //noinspection InfiniteLoopStatement
            while (true) {
                new TcpConnection(this, serverSocket.accept());
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public synchronized void onConnectionReady(TcpConnection tcpConnection) {
        connections.add(tcpConnection);
        sendToAllConnections("Client connected: " + tcpConnection);
    }

    @Override
    public synchronized void onReceiveString(TcpConnection tcpConnection, String value) {
        sendToAllConnections(value);
    }

    @Override
    public synchronized void onDisconnect(TcpConnection tcpConnection) {
        connections.remove(tcpConnection);
        sendToAllConnections("Client disconnected: " + tcpConnection);
    }

    @Override
    public synchronized void onException(TcpConnection tcpConnection, Exception e) {
        System.out.println("TcpConnection exception: " + e);

    }

    private void sendToAllConnections(String value) {
        System.out.println(value);
        for (TcpConnection connection : connections) {
            connection.sendString(value);
        }
    }

}
