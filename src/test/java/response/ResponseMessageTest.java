package response;

import org.junit.Test;
import server.HttpVersion;
import server.MessageHeaders;

import static org.junit.Assert.*;

/**
 * Created by isaac on 2/9/16.
 */
public class ResponseMessageTest {

    @Test
    public void testToString() throws Exception {
        ResponseMessage responseMessage = new ResponseMessage();
        assertEquals("HTTP/1.1 200 OK\r\n\r\n", responseMessage.toString());

        responseMessage.setHttpVersion(new HttpVersion("HTTP/2.0"));
        responseMessage.setHttpStatus(HttpStatus.BAD_REQUEST_400);
        assertEquals("HTTP/2.0 400 Bad Request\r\n\r\n", responseMessage.toString());

        MessageHeaders messageHeaders = new MessageHeaders();
        messageHeaders.addHeader("Name", "Bob");
        messageHeaders.addHeader("Foo", "Bar");
        messageHeaders.addCookie("id=4");
        responseMessage.setMessageHeaders(messageHeaders);
        assertEquals("HTTP/2.0 400 Bad Request\r\nName: Bob\r\nFoo: Bar\r\nSet-Cookie: id=4\r\n\r\n", responseMessage.toString());


        responseMessage.setMessageBody("<h1>Hello</h1>");
        assertEquals("HTTP/2.0 400 Bad Request\r\nName: Bob\r\nFoo: Bar\r\nSet-Cookie: id=4\r\n\r\n<h1>Hello</h1>", responseMessage.toString());
    }
}