package request;

import annotations.Rfc;
import parser.RequestMessageParser;
import response.ResponseMessage;
import server.HttpVersion;
import server.MessageHeaders;

import java.net.URI;

/**
 * Created by isaac on 2/8/16.
 */
@Rfc("https://tools.ietf.org/html/rfc7230#section-3")
public class RequestMessage {
    private final RequestMethod requestMethod;
    private final RequestTarget requestTarget;
    private final HttpVersion httpVersion;
    private final MessageHeaders requestHeaders;
    private final String requestBody;

    public RequestMessage(RequestMethod requestMethod,
                           RequestTarget requestTarget,
                           HttpVersion httpVersion,
                           MessageHeaders requestHeaders,
                           String requestBody) {
        this.requestMethod = requestMethod;
        this.requestTarget = requestTarget;
        this.requestHeaders = requestHeaders;
        this.httpVersion = httpVersion;
        this.requestBody = requestBody;
    }

    public static RequestMessage fromString(String msg) throws RequestMessageParsingException {
        RequestMessageParser parser = new RequestMessageParser();
        return parser.parseRequestMessage(msg);
    }

    public RequestMethod getRequestMethod() {
        return requestMethod;
    }

    public RequestTarget getRequestTarget() {
        return requestTarget;
    }

    public HttpVersion getHttpVersion() {
        return httpVersion;
    }

    public MessageHeaders getRequestHeaders() {
        return new MessageHeaders(requestHeaders);
    }

    public String getRequestBody() {
        return requestBody;
    }
}
