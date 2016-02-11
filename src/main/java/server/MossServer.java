package server;

import parser.RequestMessageParser;
import request.RequestMessage;
import request.RequestMessageParsingException;
import request.RequestTarget;
import response.HttpStatus;
import response.ResponseMessage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.CharBuffer;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by isaac on 2/8/16.
 */
public class MossServer {
    private static final int HTTP_MESSAGE_SIZE = 10000;
    private static final String OK_MSG = "HTTP/1.1 200 OK\r\n\r\n<h1>Hello, World!</h1>";
    private static final String INDEX_PAGE = "/index.html";
    private static final int MB = 1_000_000;
    private static final int MESSAGE_BODY_SIZE = 5 * MB;

    private AtomicBoolean done;

    public MossServer() {
        done = new AtomicBoolean(false);
    }

    public void start(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port);) {
            while (!done.get()) {
                try (
                        Socket clientSocket = serverSocket.accept();
                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        OutputStream out = clientSocket.getOutputStream();
                ) {
                    if (clientSocket.isConnected()) {
                        handleConnection(in, out);
                    } else {
                        System.err.println("Client Socket closed.");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleConnection(Readable in, OutputStream out) {
        String msg = readAll(in);
        RequestMessage requestMessage = null;
        try {
            requestMessage = RequestMessage.fromString(msg);
        } catch (RequestMessageParsingException e) {
            e.printStackTrace();
            try {
                sendBadRequest(out);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return;
        }

        RequestTarget requestTarget = requestMessage.getRequestTarget();
        sendFile(requestTarget.getAbsolutePath(), out);
    }

    public void stop() {
        done.set(true);
    }

    private String readAll(Readable readable) {
        CharBuffer buffer = CharBuffer.allocate(HTTP_MESSAGE_SIZE);
        try {
            readable.read(buffer);
            buffer.flip();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    private void sendOk(OutputStream out) throws IOException {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setHttpStatus(HttpStatus.OK_200);
        responseMessage.write(out);
    }

    private void sendBadRequest(OutputStream out) throws IOException {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setHttpStatus(HttpStatus.BAD_REQUEST_400);
        responseMessage.write(out);
    }

    private void sendNotFound(OutputStream out) throws IOException {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setHttpStatus(HttpStatus.NOT_FOUND_404);
        responseMessage.write(out);
    }

    private void sendFile(String path, OutputStream out) {
        String htmlDir = System.getenv("MOSS_HTML_DIR");

        if (path.equals("/")) {
            path = INDEX_PAGE;
        }

        ResponseMessage responseMessage = new ResponseMessage();
        byte[] messageBody = null;

        path = String.format("%s%s", htmlDir, path);
        byte[] fileContents = new byte[MESSAGE_BODY_SIZE];

        try (InputStream inputStream = new FileInputStream(path)) {
            int len = inputStream.read(fileContents);
            messageBody = new byte[len];
            System.arraycopy(fileContents, 0, messageBody, 0, len);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            try {
                sendNotFound(out);
                return;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        responseMessage.setMessageBody(messageBody);
        responseMessage.setHttpStatus(HttpStatus.OK_200);
        try {
            responseMessage.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
