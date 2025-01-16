package com.om1cael.ticnet.network;

import java.util.Optional;

public class Game {
    private Player host;
    private Optional<Player> guest;

    public Game(Player host, Optional<Player> guest) {
        System.out.println("Game created");
        this.host = host;
        this.guest = guest;
    }

    public void setGuest(Optional<Player> guest) {
        System.out.println("Guest set");
        this.guest = guest;
    }
}
