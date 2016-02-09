package request;

/**
 * Created by isaac on 2/9/16.
 */
public class RequestMessageParsingException extends Exception {

    public RequestMessageParsingException() {}

    public RequestMessageParsingException(String message) {
        super(message);
    }

    public RequestMessageParsingException(Throwable throwable) {
        super(throwable);
    }

    public RequestMessageParsingException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
