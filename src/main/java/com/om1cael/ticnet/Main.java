package com.om1cael.ticnet;

import com.om1cael.ticnet.network.Player;
import com.om1cael.ticnet.network.PlayersManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private final short SERVER_PORT;
    private final byte CONNECTION_THREADS;
    private int idCounter;

    private final Logger log = LogManager.getLogger(Main.class);
    private final PlayersManager playersManager;

    public static void main(String[] args) {
        new Main().start();
    }

    public Main() {
        this.SERVER_PORT = 1024;
        this.CONNECTION_THREADS = 4;
        this.idCounter = 0;

        this.playersManager = new PlayersManager();
    }

    private void start() {
        try(ServerSocket serverSocket = new ServerSocket(this.SERVER_PORT);
            ExecutorService executorService = Executors.newFixedThreadPool(this.CONNECTION_THREADS)
        ) {
            log.info("Server started at port {}", this.SERVER_PORT);

            while(true) {
                Player player = new Player(serverSocket.accept(), this.idCounter++);
                this.playersManager.createPlayer(player);

                executorService.execute(player);
            }
        } catch (IOException e) {
            log.fatal("Server could not be started!", e);
        }
    }

    public PlayersManager getPlayersManager() {
        return playersManager;
    }
}