package com.om1cael.ticnet.network;

import com.om1cael.ticnet.Main;
import com.om1cael.ticnet.commands.CreateCommand;
import com.om1cael.ticnet.commands.JoinCommand;
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

    private Game currentGame = null;

    private final Logger log = LogManager.getLogger(Player.class);

    public Player(Socket socket, int id) {
        this.socket = socket;
        this.id = id;

        this.isRunning = true;
    }

    @Override
    public void run() {
        log.info("New connection from {}", this.socket.getInetAddress());

        try(BufferedReader clientData = new BufferedReader(new InputStreamReader(this.socket.getInputStream()))) {
            while(this.isRunning) {
                this.readClient(clientData);
            }
        } catch (IOException e) {
            log.error("Could not read data from player id {} at {}",
                    this.id,
                    this.socket.getInetAddress(),
                    e
            );

            if(e.getMessage().equalsIgnoreCase("Socket is closed")) {
                this.disconnect();
            }
        }
    }

    private void readClient(BufferedReader clientData) throws IOException {
        String input = clientData.readLine();

        if(input == null || this.socket.isClosed()) {
            log.warn("Input is null or socket is closed, starting to disconnect {}! Input: {}",
                    this.socket.getInetAddress(),
                    input
            );

            this.disconnect();
            return;
        }

        this.parseClient(input);
    }

    private void parseClient(String clientData) {
        if(clientData.contains("create-room"))
            new CreateCommand(Main.getPlayersManager().readPlayer(this.id)).run();
        else if(clientData.contains("join-room"))
            new JoinCommand(Main.getPlayersManager().readPlayer(this.id), clientData).run();
        else if(clientData.contains("exit")) {
            this.disconnect();
        }
    }

    private void disconnect() {
        if(this.currentGame != null)
            Main.getRoomManager().deleteRoom(Main.getPlayersManager().readPlayer(this.id));

        Main.getPlayersManager().deletePlayer(this.id);
        this.isRunning = false;

        try {
            this.socket.close();
            log.info("Player id {} disconnected", this.id);
        } catch (IOException e) {
            log.error("An error occurred while closing socket of player id {}", this.id);
        }
    }

    public void setCurrentGame(Game currentGame) {
        this.currentGame = currentGame;
    }

    public Game getCurrentGame() {
        return this.currentGame;
    }

    public Socket getSocket() {
        return this.socket;
    }

    public int getId() {
        return this.id;
    }
}
