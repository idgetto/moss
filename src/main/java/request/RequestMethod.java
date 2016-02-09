package request;

import annotations.Rfc;
import parser.RequestMessageParser;

/**
 * Created by isaac on 2/8/16.
 */
@Rfc("https://tools.ietf.org/html/rfc7231#section-4")
public enum RequestMethod {
    GET,
    HEAD,
    POST,
    PUT,
    DELETE,
    CONNECT,
    OPTIONS,
    TRACE;

    private static final String GET_STR = "GET";
    private static final String HEAD_STR = "HEAD";
    private static final String POST_STR = "POST";
    private static final String PUT_STR = "PUT";
    private static final String DELETE_STR = "DELETE";
    private static final String CONNECT_STR = "CONNECT";
    private static final String OPTIONS_STR = "OPTIONS";
    private static final String TRACE_STR = "TRACE";

    public static RequestMethod fromString(String msg) throws RequestMessageParsingException {
        switch (msg) {
            case GET_STR:
                return GET;
            case HEAD_STR:
                return HEAD;
            case POST_STR:
                return POST;
            case PUT_STR:
                return PUT;
            case DELETE_STR:
                return DELETE;
            case CONNECT_STR:
                return CONNECT;
            case OPTIONS_STR:
                return OPTIONS;
            case TRACE_STR:
                return TRACE;
            default:
                String message = String.format("Could not find a request method for \"%s\".", msg);
                throw new RequestMessageParsingException(message);
        }
    }

}
