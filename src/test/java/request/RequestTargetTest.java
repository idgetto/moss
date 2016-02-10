package request;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by isaac on 2/10/16.
 */
public class RequestTargetTest {

    @Test
    public void testFromString() throws Exception {
        RequestTarget requestTarget = new RequestTarget("/");
        assertEquals("/", requestTarget.getAbsolutePath());
        assertEquals("", requestTarget.getQuery());
        assertEquals("", requestTarget.getHost());
        assertEquals("", requestTarget.getPort());

        requestTarget = new RequestTarget("/foo/bar");
        assertEquals("/foo/bar", requestTarget.getAbsolutePath());
        assertEquals("", requestTarget.getQuery());
        assertEquals("", requestTarget.getHost());
        assertEquals("", requestTarget.getPort());

        requestTarget = new RequestTarget("/foo/bar?name=bob");
        assertEquals("/foo/bar", requestTarget.getAbsolutePath());
        assertEquals("name=bob", requestTarget.getQuery());
        assertEquals("", requestTarget.getHost());
        assertEquals("", requestTarget.getPort());
    }

    @Test(expected = RequestMessageParsingException.class)
    public void testFromInvalidString() throws Exception {
        RequestTarget requestTarget = new RequestTarget("foo/bar?name=bob");
    }
}