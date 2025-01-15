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
            String input = clientData.readLine();
            if(input == null || this.socket.isClosed()) {
                this.disconnect();
                return;
            }

            this.parseClient(clientData.readLine());
        } catch (IOException e) {
            log.info("Could not read data from player id {} at {}",
                    this.id,
                    this.socket.getInetAddress(),
                    e
            );
        }
    }

    private void parseClient(String clientData) {
        // parse commands
    }

    private void disconnect() {
        Main.getPlayersManager().deletePlayer(this.id);
        this.isRunning = false;

        try {
            this.socket.close();
            log.info("Player id {} disconnected", this.id);
        } catch (IOException e) {
            log.error("An error occurred while closing socket of player id {}", this.id);
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public int getId() {
        return id;
    }
}
