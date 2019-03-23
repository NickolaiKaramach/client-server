package by.bsuir.karamach.util;

public final class Validator {
    private static final String IP_ADDRESS_PATTERN =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    private Validator() {
    }

    public static boolean isNotValidPortRange(int port) {
        return (port <= 0) || (port > 65535);
    }

    public static boolean isValidPort(String portInput) {
        return !(portInput == null) && !(portInput.isEmpty());
    }

    public static boolean isNotValidIp(String ip) {
        return (ip == null || ip.isEmpty()) || (!ip.matches(IP_ADDRESS_PATTERN));
    }
}
