package by.bsuir.karamach.util;

import javax.swing.*;

import static by.bsuir.karamach.util.Validator.isNotValidIp;

public class GUIReader {
    public synchronized static String readIp() {
        String ip;

        boolean isValidData;

        do {

            isValidData = true;

            ip = JOptionPane.showInputDialog("Input IP Address: ");

            if (isNotValidIp(ip)) {
                JOptionPane.showMessageDialog(null, "Invalid ip address");
                isValidData = false;
            }


        } while (!isValidData);
        return ip;
    }

    public synchronized static int readPort() {
        int port = 0;
        boolean isValidData;

        do {

            try {

                isValidData = true;

                String inputPort = JOptionPane.showInputDialog("Input PORT: ");

                if (Validator.isValidPort(inputPort)) {

                    port = Integer.valueOf(inputPort);

                    if (Validator.isNotValidPortRange(port)) {
                        JOptionPane.showMessageDialog(null, "Invalid port range");
                        isValidData = false;
                    }
                } else {
                    isValidData = false;
                    JOptionPane.showMessageDialog(null, "Invalid port");
                }

            } catch (NumberFormatException e) {

                isValidData = false;
                JOptionPane.showMessageDialog(null,
                        "Number format exception " + e.getMessage());
            }

        } while (!isValidData);

        return port;
    }
}
