package server;

import parser.RequestMessageParser;
import request.RequestMessage;
import request.RequestMessageParsingException;
import request.RequestTarget;
import response.HttpStatus;
import response.ResponseMessage;

import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.CharBuffer;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by isaac on 2/8/16.
 */
public class MossServer {
    private static final int HTTP_MESSAGE_SIZE = 10000;
    private static final String OK_MSG = "HTTP/1.1 200 OK\r\n\r\n<h1>Hello, World!</h1>";
    private static final String INDEX_PAGE = "/index.html";

    private AtomicBoolean done;

    public MossServer() {
        done = new AtomicBoolean(false);
    }

    public void start(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port);) {
            while (!done.get()) {
                try (
                        Socket clientSocket = serverSocket.accept();
                        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                ) {
                    String msg = readAll(in);
                    System.out.println(msg);

                    try {
                        RequestMessage requestMessage = RequestMessage.fromString(msg);
                        RequestTarget requestTarget = requestMessage.getRequestTarget();
                        sendFile(requestTarget.getAbsolutePath(), out);

                    } catch (RequestMessageParsingException e) {
                        e.printStackTrace();
                        sendBadRequest(out);
                        continue;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private void sendOk(PrintWriter out) {
        out.println(OK_MSG);
    }

    private void sendBadRequest(PrintWriter out) {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setHttpStatus(HttpStatus.BAD_REQUEST_400);
        out.println(responseMessage);
    }

    private void sendNotFound(PrintWriter out) {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setHttpStatus(HttpStatus.NOT_FOUND_404);
        out.println(responseMessage);
    }

    private void sendFile(String path, PrintWriter out) {
        String htmlDir = System.getenv("MOSS_HTML_DIR");

        if (path.equals("/")) {
            path = INDEX_PAGE;
        }

        ResponseMessage responseMessage = new ResponseMessage();
        StringBuilder messageBody = new StringBuilder();

        path = String.format("%s%s", htmlDir, path);
        File file = new File(path);
        if (file.exists() && !file.isDirectory()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(path));
                String line;
                while ((line = reader.readLine()) != null) {
                    messageBody.append(line);
                    messageBody.append("\n");
                }
                responseMessage.setMessageBody(messageBody.toString());
                responseMessage.setHttpStatus(HttpStatus.OK_200);
                out.println(responseMessage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            sendNotFound(out);
        }
    }

}
