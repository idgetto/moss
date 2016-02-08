package runner;

import server.MossServer;

/**
 * Created by isaac on 2/8/16.
 */
public class MossRunner {
    public static void main(String[] args) {
        MossServer server = new MossServer();
        server.start(3000);
    }
}
