package request;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by isaac on 2/9/16.
 */
public class RequestMethodTest {

    @Test
    public void testFromString() throws Exception {
        assertEquals(RequestMethod.GET, RequestMethod.fromString("GET"));
        assertEquals(RequestMethod.HEAD, RequestMethod.fromString("HEAD"));
        assertEquals(RequestMethod.POST, RequestMethod.fromString("POST"));
        assertEquals(RequestMethod.PUT, RequestMethod.fromString("PUT"));
        assertEquals(RequestMethod.DELETE, RequestMethod.fromString("DELETE"));
        assertEquals(RequestMethod.CONNECT, RequestMethod.fromString("CONNECT"));
        assertEquals(RequestMethod.OPTIONS, RequestMethod.fromString("OPTIONS"));
        assertEquals(RequestMethod.TRACE, RequestMethod.fromString("TRACE"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIncorrectCase() throws Exception {
        RequestMethod.fromString("Delete");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIncorrectName() throws Exception {
        RequestMethod.fromString("FOO");
    }
}