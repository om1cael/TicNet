package com.om1cael.ticnet.network;

import com.om1cael.ticnet.Main;
import com.om1cael.ticnet.commands.CreateCommand;
import com.om1cael.ticnet.commands.JoinCommand;
import com.om1cael.ticnet.commands.MakeMoveCommand;
import com.om1cael.ticnet.response.model.PlayerJoinResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;

public class Player implements Runnable {
    private final Socket socket;
    private final int id;
    private volatile boolean isRunning;

    private Game currentGame = null;
    private char gameSymbol = 'o';

    private final Logger log = LogManager.getLogger(Player.class);
    private PrintWriter printWriter;

    public Player(Socket socket, int id) {
        this.socket = socket;
        this.id = id;

        this.isRunning = true;
        try {
            this.printWriter = new PrintWriter(this.socket.getOutputStream(), true);
        } catch (IOException e) {
            log.fatal("An error occurred while setting player data output stream up");
        }
    }

    @Override
    public void run() {
        log.info("New connection from {}", this.socket.getInetAddress());
        this.writeClient(Main.getGson().toJson(new PlayerJoinResponse("PR_0", this.id)));

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

    public void writeClient(String message) {
        this.printWriter.println(message);
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
        else if(clientData.contains("make-move"))
            new MakeMoveCommand(Main.getPlayersManager().readPlayer(this.id), this.currentGame, clientData).run();
        else if(clientData.contains("exit")) {
            this.disconnect();
        }
    }

    private void disconnect() {
        if(this.currentGame != null)
            Main.getRoomManager().deleteRoom(Main.getPlayersManager().readPlayer(this.id), true);

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

    public void setGameSymbol(char gameSymbol) {
        this.gameSymbol = gameSymbol;
    }

    public char getGameSymbol() {
        return this.gameSymbol;
    }

    public Socket getSocket() {
        return this.socket;
    }

    public int getId() {
        return this.id;
    }
}
