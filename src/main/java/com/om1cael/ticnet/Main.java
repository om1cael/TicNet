package com.om1cael.ticnet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;

public class Main {
    private final short SERVER_PORT = 1024;

    private final Logger log = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        new Main().start();
    }

    private void start() {
        try(ServerSocket serverSocket = new ServerSocket(this.SERVER_PORT)) {
            log.info("Server started at port {}", this.SERVER_PORT);

            while(true) {
                // Create client thread
            }
        } catch (IOException e) {
            log.fatal("Server could not be started!", e);
        }
    }
}