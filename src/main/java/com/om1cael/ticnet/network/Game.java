package com.om1cael.ticnet.network;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class Game {
    private Player host;
    private Optional<Player> guest;

    private AtomicBoolean isRunning;

    public Game(Player host, Optional<Player> guest) {
        this.host = host;
        this.guest = guest;
        this.isRunning = new AtomicBoolean(false);
    }

    public void stopGame(boolean abruptStop) {
        this.isRunning.set(false);

        if(abruptStop) {
            // Send message regarding abrupt stop
            return;
        }

        // Send message regarding stop
        // Anything else that needs to be done
    }

    public void setGuest(Optional<Player> guest) {
        this.guest = guest;
    }
}
