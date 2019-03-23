package by.bsuir.karamach.connection;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

public class TcpConnection {

    private static final String DEFAULT_CHARSET = "UTF-8";
    private final Socket socket;
    private final Thread rxThread;
    private final TcpConnectionListener eventListener;
    private final BufferedReader in;
    private final BufferedWriter out;

    public TcpConnection(TcpConnectionListener eventListener, String ipAddress, int port) throws IOException {
        this(eventListener, new Socket(ipAddress, port));
    }

    public TcpConnection(TcpConnectionListener eventListener, Socket socket) throws IOException {
        this.socket = socket;
        this.eventListener = eventListener;

        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName(DEFAULT_CHARSET)));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName(DEFAULT_CHARSET)));

        rxThread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    eventListener.onConnectionReady(TcpConnection.this);
                    while (!rxThread.isInterrupted()) {
                        String msg = in.readLine();
                        eventListener.onReceiveString(TcpConnection.this, msg);
                    }

                } catch (IOException e) {
                    eventListener.onException(TcpConnection.this, e);
                } finally {
                    eventListener.onDisconnect(TcpConnection.this);
                }

            }
        });

        rxThread.start();
    }

    public synchronized void sendString(String value) {
        try {

            out.write(value + "\r\n"); // записалось в буффер,
            out.flush(); // передать строку по сети, очистив буффер

        } catch (IOException e) {

            eventListener.onException(TcpConnection.this, e);
            disconnect();

        }
    }

    public synchronized void disconnect() {
        rxThread.interrupt();

        try {

            socket.close();

        } catch (Exception e) {

            eventListener.onException(TcpConnection.this, e);

        }

    }

    @Override
    public String toString() {
        return "TcpConnection " + socket.getInetAddress() + ": " + socket.getPort();
    }
}
