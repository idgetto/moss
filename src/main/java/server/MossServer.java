package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.CharBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by isaac on 2/8/16.
 */
public class MossServer {
    private static final int HTTP_MESSAGE_SIZE = 10000;
    private AtomicBoolean done;

    public MossServer() {
        done = new AtomicBoolean(false);
    }

    public void start(int port) {
        while (!done.get()) {
            try (
                    ServerSocket serverSocket = new ServerSocket(port);
                    Socket clientSocket = serverSocket.accept();
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            ) {
                String msg = readAll(in);
                System.out.println(msg);
                sendOk(out);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
        out.println("HTTP/1.1 200 OK\r\n\r\n<h1>Hello, World!</h1>");
    }

}
