package minesweeperclient.exceptions;

public class UnexpectedStatusCodeException extends RuntimeException {

    public UnexpectedStatusCodeException(int status, String body) {
        super("Unexpected status code " + status + ". Body: " + body);
    }
}
