package com.om1cael.ticnet.commands;

import com.om1cael.ticnet.Main;
import com.om1cael.ticnet.network.Player;

public class CreateCommand {
    Player player;

    public CreateCommand(Player player) {
        this.player = player;
    }

    public void run() {
        Main.getRoomManager().createRoom(this.player);
    }
}
