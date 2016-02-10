package server;

import annotations.Rfc;

import java.util.*;

/**
 * Created by isaac on 2/8/16.
 */

@Rfc("https://tools.ietf.org/html/rfc7230#section-3.2")
public class MessageHeaders {
    private static final String SET_COOKIE_NAME = "Set-Cookie";
    private static final String HEADER_SEPARATOR = ": ";
    private static final String CRLF = "\r\n";

    private Map<String, String> headers;
    List<String> cookies;

    public MessageHeaders() {
        // maintain header insertion order using LinkedHashMap
        headers = new LinkedHashMap<>();
        cookies = new ArrayList<>();
    }

    public MessageHeaders(MessageHeaders other) {
        headers = new HashMap<>(other.getHeaders());
        cookies = new ArrayList<>(other.getCookies());
    }

    public void addHeader(String name, String value) {
        if (SET_COOKIE_NAME.equals(name)) {
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
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry entry : headers.entrySet()) {
            sb.append(entry.getKey());
            sb.append(HEADER_SEPARATOR);
            sb.append(entry.getValue());
            sb.append(CRLF);
        }

        for (String cookie : cookies) {
            sb.append(SET_COOKIE_NAME);
            sb.append(HEADER_SEPARATOR);
            sb.append(cookie);
            sb.append(CRLF);
        }
        return sb.toString();
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
