package com.om1cael.ticnet.network;

import java.util.Optional;

public class Game {
    private Player host;
    private Optional<Player> guest;

    public Game(Player host, Optional<Player> guest) {
        this.host = host;
        this.guest = guest;
    }

    public void setGuest(Optional<Player> guest) {
        this.guest = guest;
    }
}
