package com.om1cael.ticnet.network;

import com.om1cael.ticnet.Main;
import com.om1cael.ticnet.response.GameResponses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetAddress;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class Game implements Runnable {
    private Player host;
    private Optional<Player> guest;
    private char[][] board = new char[3][3];

    private final AtomicBoolean isRunning;
    private final InetAddress hostAddress;

    private final Logger log = LogManager.getLogger(Game.class);
    private Player xPlayer;
    private Player oPlayer;

    public Game(Player host, Optional<Player> guest) {
        this.host = host;
        this.guest = guest;
        this.isRunning = new AtomicBoolean(false);
        this.hostAddress = this.host.getSocket().getInetAddress();
        this.xPlayer = null;
        this.oPlayer = null;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.board[i][j] = ' ';
            }
        }
    }

    private void setup() {
        if (host != null && guest.isPresent()) {
            log.info("A new game with the host {} was started!", this.host.getSocket().getInetAddress());
            this.assignGameSymbol();

            this.isRunning.set(true);
            Main.createGameThread(this::play);

            this.sendMessageToGameMembers(GameResponses.NEW_GAME);
        } else {
            log.error("It was not possible to start a game!");
            this.sendMessageToGameMembers(GameResponses.FAILED_NEW_GAME);
            Main.getRoomManager().deleteRoom(this.host, false);
        }
    }

    private void play() {
        while (this.isRunning.get()) {
        }
    }

    public void validateGameState() {
        if(isBoardFull()) {
            this.sendMessageToGameMembers(GameResponses.GAME_DRAW);
            return;
        }

        char winner = getWinner();
        if (winner != ' ') {
            // Send winner message
            // Send loser message
        }
    }

    private char getWinner() {
        for (int i = 0; i < 3; i++) {
            if (isWinningLine(board[i][0], board[i][1], board[i][2])) return board[i][0]; // Row
            if (isWinningLine(board[0][i], board[1][i], board[2][i])) return board[0][i]; // Column
        }

        if (isWinningLine(board[0][0], board[1][1], board[2][2])) return board[0][0];
        if (isWinningLine(board[0][2], board[1][1], board[2][0])) return board[0][2];

        return ' ';
    }

    private boolean isWinningLine(char a, char b, char c) {
        return a != ' ' && a == b && b == c;
    }

    private boolean isBoardFull() {
        for (char[] row : board) {
            for (char cell : row) {
                if (cell == ' ') return false;
            }
        }
        return true;
    }

    public void stopGame(boolean abruptStop) {
        if(!this.isRunning.get()) return;

        if(abruptStop) {
            log.warn("Abrupt stop on game of {}!",
                    this.hostAddress
            );

            if(this.host == null) {
                this.guest.get().writeClient(GameResponses.DISCONNECTION_WIN);
            } else if(this.guest.isEmpty()) {
                this.host.writeClient(GameResponses.DISCONNECTION_WIN);
            }
        }

        this.isRunning.set(false);
    }

    private void assignGameSymbol() {
        final int randomIndex = (int)((Math.random() * 2));
        if(this.host == null || (this.guest == null || this.guest.isEmpty())) {
            this.stopGame(false);
            return;
        }

        if(randomIndex == 0) {
            this.host.setGameSymbol('x');
            this.host.writeClient(GameResponses.GAME_SYMBOL_ASSIGNED);
            this.xPlayer = this.host;
            this.oPlayer = this.guest.get();
        } else {
            this.guest.get().setGameSymbol('x');
            this.guest.get().writeClient(GameResponses.GAME_SYMBOL_ASSIGNED);
            this.xPlayer = this.guest.get();
            this.oPlayer = this.host;
        }
    }

    public void setHost(Player host) {
        this.host = host;
    }

    public void setGuest(Optional<Player> guest) {
        this.guest = guest;

        if(guest.isPresent()) {
            this.setup();
        }
    }

    private void sendMessageToGameMembers(String message) {
        if(this.host != null) {
            this.host.writeClient(message);
        }

        if(this.guest != null && this.guest.isPresent()) {
            this.guest.get().writeClient(message);
        }
    }

    @Override
    public void run() {

    }
}
