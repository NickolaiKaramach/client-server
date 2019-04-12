package by.bsuir.karamach.server;

public class ServerException extends Exception {
    private static final long serialVersionUID = -9184438944277312518L;


    public ServerException() {
        super();
    }

    public ServerException(String message) {
        super(message);
    }

    public ServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServerException(Throwable cause) {
        super(cause);
    }


}
