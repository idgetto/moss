package server;

import annotations.Rfc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;

/**
 * Created by isaac on 2/8/16.
 */

@Rfc("https://tools.ietf.org/html/rfc7230#section-3.2")
public class MessageHeaders {
    private static final String SET_COOKIE_NAME = "Set-Cookie";

    private Map<String, String> headers;
    List<String> cookies;

    public MessageHeaders() {
        headers = new HashMap<>();
        cookies = new ArrayList<>();
    }

    public MessageHeaders(MessageHeaders other) {
        headers = new HashMap<>(other.getHeaders());
        cookies = new ArrayList<>(other.getCookies());
    }

    public void addHeader(String name, String value) {
        if (SET_COOKIE_NAME == name) {
            addCookie(value);
            return;
        }
        if (headers.containsKey(name)) {
            String message = String.format("Header name: \"%s\" is already in use.", name);
            throw new DuplicateHeaderException(message);
        }
        headers.put(name, value);
    }

    public void addCookie(String value) {
        cookies.add(value);
    }

    public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(headers);

    }

    public List<String> getCookies() {
        return Collections.unmodifiableList(cookies);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageHeaders that = (MessageHeaders) o;

        if (headers != null ? !headers.equals(that.headers) : that.headers != null) return false;
        return cookies != null ? cookies.equals(that.cookies) : that.cookies == null;

    }

    @Override
    public int hashCode() {
        int result = headers != null ? headers.hashCode() : 0;
        result = 31 * result + (cookies != null ? cookies.hashCode() : 0);
        return result;
    }

    public class DuplicateHeaderException extends RuntimeException {
        public DuplicateHeaderException() { }

        public DuplicateHeaderException(String message) {
            super(message);
        }

        public DuplicateHeaderException(Throwable throwable) {
            super(throwable);
        }

        public DuplicateHeaderException(String message, Throwable throwable) {
            super(message, throwable);
        }
    }
}
