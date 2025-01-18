package com.om1cael.ticnet;

import com.google.gson.Gson;
import com.om1cael.ticnet.network.Player;
import com.om1cael.ticnet.network.PlayersManager;
import com.om1cael.ticnet.session.RoomManager;
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
    private static final Gson gson = new Gson();

    private static final PlayersManager playersManager = new PlayersManager();
    private static final RoomManager roomManager = new RoomManager();

    public static void main(String[] args) {
        new Main().start();
    }

    public Main() {
        this.SERVER_PORT = 1024;
        this.CONNECTION_THREADS = 4;
        this.idCounter = 0;
    }

    private void start() {
        try(ServerSocket serverSocket = new ServerSocket(this.SERVER_PORT);
            ExecutorService executorService = Executors.newFixedThreadPool(this.CONNECTION_THREADS)
        ) {
            log.info("Server started at port {}", this.SERVER_PORT);

            while(true) {
                Player player = new Player(serverSocket.accept(), this.idCounter++);
                playersManager.createPlayer(player);

                executorService.execute(player);
            }
        } catch (IOException e) {
            log.fatal("Server could not be started!", e);
        }
    }

    public static PlayersManager getPlayersManager() {
        return playersManager;
    }

    public static RoomManager getRoomManager() {
        return roomManager;
    }

    public static Gson getGson() {
        return gson;
    }
}