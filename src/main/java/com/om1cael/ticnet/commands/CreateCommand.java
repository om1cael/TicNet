package com.om1cael.ticnet.commands;

import com.om1cael.ticnet.Main;
import com.om1cael.ticnet.network.Player;

public class CreateCommand {
    public void run(Player player) {
        Main.getRoomManager().createRoom(player);
    }
}
