package com.om1cael.ticnet.network;

import com.om1cael.ticnet.Main;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetAddress;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class Game implements Runnable {
    private Player host;
    private Optional<Player> guest;

    private final AtomicBoolean isRunning;
    private final InetAddress hostAddress;

    private final Logger log = LogManager.getLogger(Game.class);

    public Game(Player host, Optional<Player> guest) {
        this.host = host;
        this.guest = guest;
        this.isRunning = new AtomicBoolean(false);
        this.hostAddress = this.host.getSocket().getInetAddress();
    }

    private void setup() {
        if(host != null && guest.isPresent()) {
            log.info("A new game with the host {} was started!", this.host.getSocket().getInetAddress());
            this.isRunning.set(true);
            Main.createGameThread(this::play);
        } else {
            log.error("It was not possible to start a game!");
            Main.getRoomManager().deleteRoom(this.host, false);
        }
    }

    private void play() {
        while(this.isRunning.get()) {}
    }

    public void stopGame(boolean abruptStop) {
        if(!this.isRunning.get()) return;

        if(abruptStop) {
            log.warn("Abrupt stop on game of {}!",
                    this.hostAddress
            );

            if(this.host == null) {
                log.info("Disconnection origin is host");
            } else if(this.guest.isEmpty()) {
                log.info("Disconnection origin is guest");
            }
        }

        this.isRunning.set(false);
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

    @Override
    public void run() {

    }
}
