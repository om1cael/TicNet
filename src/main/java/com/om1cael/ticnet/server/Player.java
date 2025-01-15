package com.om1cael.ticnet.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.Socket;

public class Player implements Runnable {
    private final Socket socket;
    private final int id;

    private final Logger log = LogManager.getLogger(Player.class);

    public Player(Socket socket, int id) {
        this.socket = socket;
        this.id = id;
    }

    @Override
    public void run() {
        log.info("New connection from {}", this.socket.getInetAddress());
    }
}
