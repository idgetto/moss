package request;

import org.junit.Test;
import server.HttpVersion;
import server.MessageHeaders;

import java.net.URI;

import static org.junit.Assert.*;

/**
 * Created by isaac on 2/8/16.
 */
public class RequestMessageTest {
    private static final String REQ_START_LINE_ONLY = "GET / HTTP/1.1\r\n\r\n";
    private static final String REQ_HEADERS = "GET / HTTP/1.1\r\nHost: localhost:3000\r\nConnection: keep-alive\r\n\r\n";
    private static final String REQ_COOKIES = "GET / HTTP/1.1\r\nHost: localhost:3000\r\nSet-Cookie: uid=5\r\n\r\n";
    private static final String REQ_MESSAGE_BODY = "POST / HTTP/1.1\r\nHost: localhost:3000\r\nContent-Length: 16\r\n\r\nHere's a message";

    @Test
    public void testFromStartLineString() throws Exception {
        RequestMessage reqStartLine = RequestMessage.fromString(REQ_START_LINE_ONLY);
        assertEquals(RequestMethod.GET, reqStartLine.getRequestMethod());
        // assertEquals(new URI("localhost:3000"), reqStartLine.getRequestUri());
        assertEquals(new HttpVersion("HTTP/1.1"), reqStartLine.getHttpVersion());
    }

    @Test
    public void testFromHeadersString() throws Exception {
        RequestMessage reqHeaders = RequestMessage.fromString(REQ_HEADERS);
        assertEquals(RequestMethod.GET, reqHeaders.getRequestMethod());
        // assertEquals(new URI("localhost:3000"), reqHeaders.getRequestUri());
        assertEquals(new HttpVersion("HTTP/1.1"), reqHeaders.getHttpVersion());

        MessageHeaders headers = new MessageHeaders();
        headers.addHeader("Host", "localhost:3000");
        headers.addHeader("Connection", "keep-alive");
        assertEquals(headers, reqHeaders.getRequestHeaders());
    }

    @Test
    public void testFromCookiesString() throws Exception {
        RequestMessage reqHeaders = RequestMessage.fromString(REQ_COOKIES);
        assertEquals(RequestMethod.GET, reqHeaders.getRequestMethod());
        // assertEquals(new URI("localhost:3000"), reqHeaders.getRequestUri());
        assertEquals(new HttpVersion("HTTP/1.1"), reqHeaders.getHttpVersion());

        MessageHeaders headers = new MessageHeaders();
        headers.addHeader("Host", "localhost:3000");
        headers.addCookie("uid=5");

        assertEquals(headers, reqHeaders.getRequestHeaders());
    }

    @Test
    public void testFromMessageBodyString() throws Exception {
        RequestMessage reqMessageBody = RequestMessage.fromString(REQ_MESSAGE_BODY);
        assertEquals(RequestMethod.POST, reqMessageBody.getRequestMethod());
        // assertEquals(new URI("localhost:3000"), reqMessageBody.getRequestUri());
        assertEquals(new HttpVersion("HTTP/1.1"), reqMessageBody.getHttpVersion());
        assertEquals("Here's a message", reqMessageBody.getRequestBody());
    }
}