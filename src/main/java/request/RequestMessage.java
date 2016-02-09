package request;

import annotations.Rfc;
import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;
import parser.RequestMessageParser;
import server.HttpVersion;
import server.MessageHeaders;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by isaac on 2/8/16.
 */
@Rfc("https://tools.ietf.org/html/rfc7230#section-3")
public class RequestMessage {
    private final RequestMethod requestMethod;
    private final URI requestUri;
    private final HttpVersion httpVersion;
    private final MessageHeaders requestHeaders;
    private final String requestBody;

    public RequestMessage(RequestMethod requestMethod,
                           URI requestUri,
                           HttpVersion httpVersion,
                           MessageHeaders requestHeaders,
                           String requestBody) {
        this.requestMethod = requestMethod;
        this.requestUri = requestUri;
        this.requestHeaders = requestHeaders;
        this.httpVersion = httpVersion;
        this.requestBody = requestBody;
    }

    public static RequestMessage fromString(String msg) throws RequestMessageParser.RequestMessageParsingException {
        RequestMessageParser parser = new RequestMessageParser();
        return parser.parseRequestMessage(msg);
    }

    public RequestMethod getRequestMethod() {
        return requestMethod;
    }

    public URI getRequestUri() {
        return requestUri;
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
