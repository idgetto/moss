package server;

import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by isaac on 2/8/16.
 */
public class MessageHeadersTest {

    @Test
    public void testAddHeader() throws Exception {
        MessageHeaders messageHeaders = new MessageHeaders();
        messageHeaders.addHeader("Host", "localhost:3000");
        messageHeaders.addHeader("Connection", "keep-alive");
        messageHeaders.addHeader("Accept-Encoding", "gzip, deflate");

        Map<String, String> headers = messageHeaders.getHeaders();
        assertTrue(headers.containsKey("Host"));
        assertEquals("localhost:3000", headers.get("Host"));
        assertTrue(headers.containsKey("Connection"));
        assertEquals("keep-alive", headers.get("Connection"));
        assertTrue(headers.containsKey("Accept-Encoding"));
        assertEquals("gzip, deflate", headers.get("Accept-Encoding"));
    }


    @Test(expected = MessageHeaders.DuplicateHeaderException.class)
    public void testAddDuplicateHeader() throws Exception {
        MessageHeaders headers = new MessageHeaders();
        headers.addHeader("Host", "localhost:3000");
        headers.addHeader("Host", "example.com");
    }

    public void testAddCookieAsHeader() throws Exception {
        MessageHeaders messageHeaders = new MessageHeaders();
        messageHeaders.addHeader("Set-Cookie", "JSESSIONID=4");

        List<String> cookies = messageHeaders.getCookies();
        assertEquals(1, cookies.size());
        assertEquals("JSESSIONID=4", cookies.get(0));

        Map<String, String> headers = messageHeaders.getHeaders();
        assertEquals(0, headers.size());
    }

    @Test
    public void testAddCookie() throws Exception {
        MessageHeaders messageHeaders = new MessageHeaders();
        messageHeaders.addCookie("JSESSIONID=4");
        messageHeaders.addCookie("uid=12");

        List<String> cookies = messageHeaders.getCookies();
        assertEquals(2, cookies.size());
        assertEquals("JSESSIONID=4", cookies.get(0));
        assertEquals("uid=12", cookies.get(1));
    }

    @Test
    public void testEquals() throws Exception {
        MessageHeaders mh1 = new MessageHeaders();
        MessageHeaders mh2 = new MessageHeaders();
        assertEquals(mh1, mh2);

        // order of headers doesn't matter
        mh1.addHeader("Host", "example.com");
        mh1.addHeader("Content-Encoding", "gzip");
        mh2.addHeader("Content-Encoding", "gzip");
        mh2.addHeader("Host", "example.com");
        assertEquals(mh1, mh2);

        mh1.addCookie("uid=4");
        mh1.addCookie("name=bob");
        mh2.addCookie("uid=4");
        mh2.addCookie("name=bob");
        assertEquals(mh1, mh2);

        // order of cookies matters
        mh1.addCookie("_id=9");
        mh1.addCookie("cat=dog");
        mh2.addCookie("cat=dog");
        mh2.addCookie("_id=9");
        assertNotEquals(mh1, mh2);
    }

}