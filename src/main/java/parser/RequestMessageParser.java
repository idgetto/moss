package parser;

import request.RequestMessage;
import request.RequestMessageParsingException;
import request.RequestMethod;
import server.HttpVersion;
import server.MessageHeaders;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Struct;
import java.util.*;

/**
 * Created by isaac on 2/8/16.
 */
public class RequestMessageParser {
    private static final String CRLF = "\r\n";
    private static final String HEADER_SEPARATOR = ": ";

    public RequestMessage parseRequestMessage(String msg) throws RequestMessageParsingException {
        StringBuilder sb = new StringBuilder(msg);

        RequestMethod requestMethod = parseRequestMethod(sb);
        URI uri = parseRequestURI(sb);
        HttpVersion httpVersion = parseHttpVersion(sb);
        MessageHeaders messageHeaders = parseMessageHeaders(sb);
        String messageBody = parseMessageBody(sb);

        return new RequestMessage(requestMethod, uri, httpVersion, messageHeaders, messageBody);
    }

    private RequestMethod parseRequestMethod(StringBuilder sb) throws RequestMessageParsingException {
        String methodStr = readUntil(sb, " ");
        expect(sb, " ");
        return RequestMethod.fromString(methodStr);
    }

    private URI parseRequestURI(StringBuilder sb) throws RequestMessageParsingException {
        String uriStr = readUntil(sb, " ");
        expect(sb, " ");
        try {
            return new URI(uriStr);
        } catch (URISyntaxException e) {
            throw new RequestMessageParsingException(e);
        }
    }

    private HttpVersion parseHttpVersion(StringBuilder sb) throws RequestMessageParsingException {
        String httpVersionStr = readUntil(sb, CRLF);
        expect(sb, CRLF);
        return new HttpVersion(httpVersionStr);
    }

    private MessageHeaders parseMessageHeaders(StringBuilder sb) {
        MessageHeaders messageHeaders = new MessageHeaders();

        try {
            // read headers until no more are found
            while (true) {
                String headerName = readUntil(sb, HEADER_SEPARATOR);
                expect(sb, HEADER_SEPARATOR);
                String headerValue = readUntil(sb, CRLF);
                expect(sb, CRLF);
                messageHeaders.addHeader(headerName, headerValue);
            }
        } catch (RequestMessageParsingException e) {
            // done reading headers
        }

        return messageHeaders;
    }

    private String parseMessageBody(StringBuilder sb) throws RequestMessageParsingException {
        expect(sb, CRLF);
        return sb.toString();
    }

    private String readUntil(StringBuilder src, String stop) throws RequestMessageParsingException {
        int stopIndex = src.indexOf(stop);

        if (stopIndex == -1) {
            String message = String.format("String \"%s\" not found in \"%s\".", stop, src.toString());
            throw new RequestMessageParsingException(message);
        }

        String res = src.substring(0, stopIndex);
        src.delete(0, stopIndex);
        return res;
    }

    private void expect(StringBuilder src, String expected) throws RequestMessageParsingException {
        String actual = src.substring(0, expected.length());
        if (!actual.equals(expected)) {
            String message = String.format("Expected \"%s\" next in \"%s\".", expected, src.toString());
            throw new RequestMessageParsingException(message);
        }
        src.delete(0, expected.length());
    }

}
