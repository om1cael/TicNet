package com.om1cael.ticnet.network;

import com.om1cael.ticnet.Main;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class Game {
    private Player host;
    private Optional<Player> guest;

    private final AtomicBoolean isRunning;

    private final Logger log = LogManager.getLogger(Game.class);

    public Game(Player host, Optional<Player> guest) {
        this.host = host;
        this.guest = guest;
        this.isRunning = new AtomicBoolean(false);
    }

    private void setup() {
        if(host != null && guest.isPresent()) {
            log.info("A new game with the host {} was started!", this.host.getSocket().getInetAddress());
            this.isRunning.set(true);
            this.play();
        } else {
            log.error("It was not possible to start a game!");
            Main.getRoomManager().deleteRoom(this.host, false);
        }
    }

    private void play() {
    }

    public void stopGame(boolean abruptStop) {
        if(!this.isRunning.get()) return;

        if(abruptStop) {
            log.warn("Abrupt stop on game of {}!",
                    this.host.getSocket().getInetAddress()
            );
        }

        this.isRunning.set(false);
    }

    public void setGuest(Optional<Player> guest) {
        this.guest = guest;
        this.setup();
    }
}
