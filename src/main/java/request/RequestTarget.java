package request;

import annotations.Rfc;

/**
 * This request target only supports the origin-form as described by https://tools.ietf.org/html/rfc7230#section-5.3.1.
 * This form is used for all requests with methods other than CONNECT and OPTIONS.
 * Created by isaac on 2/10/16.
 */
@Rfc("https://tools.ietf.org/html/rfc7230#section-5.3")
public class RequestTarget {
    private static final char COLON = ':';
    private static final char SLASH = '/';
    private static final char QUESTION_MARK = '?';

    private String host = new String();
    private String port = new String();
    private String absolutePath = new String();
    private String query = new String();

    public RequestTarget(String requestTarget) throws RequestMessageParsingException {
        StringBuilder sb = new StringBuilder(requestTarget);

        StringBuilder absolutePathBuilder = new StringBuilder();

        char first = sb.charAt(0);
        if (first != SLASH) {
            String msg = String.format("Invalid request target \"%s\".", requestTarget);
            throw new RequestMessageParsingException(msg);
        }

        try {
            while (sb.length() > 0) {
                String segment = parseSegment(sb);
                absolutePathBuilder.append(segment);
            }
        } catch (RequestMessageParsingException e) {
            // no more segments
        }
        absolutePath = absolutePathBuilder.toString();

        try {
            query = parseQuery(sb);
        } catch (RequestMessageParsingException e) {
            // there was no query
        }
    }

    public RequestTarget() {
        host = new String();
        port = new String();
        absolutePath = new String();
        query = new String();
    }

    private String parseSegment(StringBuilder sb) throws RequestMessageParsingException {
        if (sb.length() == 0) {
            throw new RequestMessageParsingException("input string is blank");
        }

        StringBuilder segment = new StringBuilder();

        char slash = sb.charAt(0);
        if (slash != SLASH) {
            String message = String.format("Invalid request target segment \"%s\".", sb.toString());
            throw new RequestMessageParsingException();
        } else {
            sb.delete(0, 1);
            segment.append(slash);
        }

        try {
            while (sb.length() > 0) {
                String pchar = parsePchar(sb);
                segment.append(pchar);
            }
        } catch (RequestMessageParsingException e) {
            // no more pchars
        }

        return segment.toString();
    }

    private String parseQuery(StringBuilder sb) throws RequestMessageParsingException {
        if (sb.length() == 0) {
            throw new RequestMessageParsingException("input string is blank");
        }

        char questionMark = sb.charAt(0);
        if (questionMark != QUESTION_MARK) {
            String msg = String.format("Could not parase query \"%s\".", sb.toString());
            throw new RequestMessageParsingException(msg);
        } else {
            sb.delete(0, 1);
        }

        StringBuilder queryBuilder = new StringBuilder();
        while (sb.length() > 0) {
            char first = sb.charAt(0);
            if (first == SLASH ||
                first == QUESTION_MARK) {
                sb.delete(0, 1);
                queryBuilder.append(first);
            } else {
                try {
                    String pchar = parsePchar(sb);
                    queryBuilder.append(pchar);
                } catch (RequestMessageParsingException e) {
                    return queryBuilder.toString();
                }
            }
        }
        return queryBuilder.toString();
    }

    private String parsePchar(StringBuilder sb) throws RequestMessageParsingException {
        if (sb.length() == 0) {
            throw new RequestMessageParsingException("input string is blank");
        }

        char first = sb.charAt(0);
        if (isUnreserved(first) ||
            isSubdelims(first) ||
            first == ':' ||
            first == '@') {
            sb.delete(0, 1);
            return Character.toString(first);
        }

        if (sb.length() >= 3) {
            String pct = sb.substring(0, 3);
            if (isPctEncoded(pct)) {
                sb.delete(0, 3);
                return pct;
            }
        }

        String message = String.format("Could not parse pchar \"%s\".", sb.toString());
        throw new RequestMessageParsingException(message);
    }

    private boolean isUnreserved(char c) {
        return Character.isAlphabetic(c) ||
               Character.isDigit(c) ||
               c == '-' ||
               c == '.' ||
               c == '_' ||
               c == '~';
    }

    private boolean isSubdelims(char c) {
        return c == '!' ||
               c == '$' ||
               c == '&' ||
               c == '\'' ||
               c == '(' ||
               c == ')' ||
               c == '*' ||
               c == '+' ||
               c == ',' ||
               c == ';' ||
               c == '=';
    }

    private boolean isPctEncoded(String pct) {
        if (pct.length() != 3) {
            return false;
        }

        return pct.charAt(0) == '%' &&
               isHexDigit(pct.charAt(1)) &&
               isHexDigit(pct.charAt(2));
    }

    private boolean isHexDigit(char c) {
        return Character.isDigit(c) ||
               c == 'A' ||
               c == 'B' ||
               c == 'C' ||
               c == 'D' ||
               c == 'E' ||
               c == 'F';
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) throws RequestMessageParsingException {
        parseHostPort(host);
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    private void parseHostPort(String hostPort) throws RequestMessageParsingException {
        int colonIndex = hostPort.indexOf(COLON);
        if (colonIndex == -1) {
            this.host = hostPort;
        } else {
            try {
                this.host = hostPort.substring(0, colonIndex);
                this.port = hostPort.substring(colonIndex + 1);
            } catch (StringIndexOutOfBoundsException e) {
                String message = String.format("Could not parse host: \"%s\".", hostPort);
                throw new RequestMessageParsingException(message, e);
            }
        }
    }

}
