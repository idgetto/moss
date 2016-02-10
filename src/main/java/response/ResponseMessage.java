package response;

import server.HttpVersion;
import server.MessageHeaders;

/**
 * Created by isaac on 2/8/16.
 */
public class ResponseMessage {
    private static final String SP = " ";
    private static final String CRLF = "\r\n";

    private HttpVersion httpVersion;
    private HttpStatus httpStatus;
    private MessageHeaders messageHeaders;
    private String messageBody;

    public ResponseMessage() {
        httpVersion = HttpVersion.HTTP_V_1_1;
        httpStatus = HttpStatus.OK_200;
        messageHeaders = new MessageHeaders();
        messageBody = new String();
    }

    public HttpVersion getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(HttpVersion httpVersion) {
        this.httpVersion = httpVersion;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public MessageHeaders getMessageHeaders() {
        return messageHeaders;
    }

    public void setMessageHeaders(MessageHeaders messageHeaders) {
        this.messageHeaders = messageHeaders;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // start line
        sb.append(httpVersion);
        sb.append(SP);
        sb.append(httpStatus.getCode());
        sb.append(SP);
        sb.append(httpStatus.getPhrase());
        sb.append(CRLF);

        // headers
        sb.append(messageHeaders);

        // CRLF
        sb.append(CRLF);

        // message body
        sb.append(messageBody);

        return sb.toString();
    }
}
