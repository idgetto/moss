package server;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by isaac on 2/8/16.
 */
public class HttpVersionTest {

    @Test
    public void testHttpVersion_1_1() throws Exception {
        assertEquals(new HttpVersion("HTTP/1.1"), HttpVersion.HTTP_V_1_1);
        assertEquals(HttpVersion.HTTP_V_1_1, new HttpVersion("HTTP/1.1"));

        assertNotEquals(new HttpVersion("HTTP/3.1"), HttpVersion.HTTP_V_1_1);
        assertNotEquals(HttpVersion.HTTP_V_1_1, new HttpVersion("HTTP/3.1"));
    }

    @Test
    public void testEquals() throws Exception {
        assertEquals(new HttpVersion("HTTP/0.0"), new HttpVersion("HTTP/0.0"));
        assertEquals(new HttpVersion("HTTP/2.0"), new HttpVersion("HTTP/2.0"));
        assertEquals(new HttpVersion("HTTP/4.9"), new HttpVersion("HTTP/4.9"));
    }

    @Test
    public void testToString() throws Exception {
        assertEquals("HTTP/0.0", new HttpVersion("HTTP/0.0").toString());
        assertEquals("HTTP/2.0", new HttpVersion("HTTP/2.0").toString());
        assertEquals("HTTP/4.9", new HttpVersion("HTTP/4.9").toString());
    }
}