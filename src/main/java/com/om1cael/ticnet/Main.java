package com.om1cael.ticnet;

import com.om1cael.ticnet.network.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private final short SERVER_PORT = 1024;
    private final byte CONNECTION_THREADS = 4;

    private final Logger log = LogManager.getLogger(Main.class);
    private int idCounter = 0;

    public static void main(String[] args) {
        new Main().start();
    }

    private void start() {
        try(ServerSocket serverSocket = new ServerSocket(this.SERVER_PORT);
            ExecutorService executorService = Executors.newFixedThreadPool(this.CONNECTION_THREADS)
        ) {
            log.info("Server started at port {}", this.SERVER_PORT);

            while(true) {
                Player player = new Player(serverSocket.accept(), this.idCounter++);
                executorService.execute(player);
            }
        } catch (IOException e) {
            log.fatal("Server could not be started!", e);
        }
    }
}