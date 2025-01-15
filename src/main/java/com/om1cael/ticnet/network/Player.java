package com.om1cael.ticnet.network;

import com.om1cael.ticnet.Main;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Player implements Runnable {
    private final Socket socket;
    private final int id;
    private volatile boolean isRunning;

    private final Logger log = LogManager.getLogger(Player.class);

    public Player(Socket socket, int id) {
        this.socket = socket;
        this.id = id;

        this.isRunning = true;
    }

    @Override
    public void run() {
        log.info("New connection from {}", this.socket.getInetAddress());

        while(this.isRunning) {
            this.readClient();
        }
    }

    private void readClient() {
        try(BufferedReader clientData = new BufferedReader(new InputStreamReader(this.socket.getInputStream()))) {
            if(clientData.read() == -1 || this.socket.isClosed()) {
                this.disconnect();
            }
        } catch (IOException e) {
            log.info("Could not read data from player id {} at {}",
                    this.id,
                    this.socket.getInetAddress()
            );
        }
    }

    private void disconnect() {
        Main.getPlayersManager().deletePlayer(this.id);
        this.isRunning = false;

        log.info("Player id {} disconnected", this.id);
    }

    public int getId() {
        return id;
    }
}
