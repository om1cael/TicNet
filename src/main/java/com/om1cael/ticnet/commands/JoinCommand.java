package com.om1cael.ticnet.commands;

import com.om1cael.ticnet.Main;
import com.om1cael.ticnet.response.RoomResponses;
import com.om1cael.ticnet.network.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JoinCommand {
    private Player guest;
    private String clientCommand;

    private final Logger log = LogManager.getLogger(JoinCommand.class);

    public JoinCommand(Player guest, String clientCommand) {
        this.clientCommand = clientCommand;
        this.guest = guest;
    }

    public void run() {
        String[] splitCommand = this.clientCommand.split(" ");
        if(splitCommand.length <= 1) {
            log.info("Could not join room, because host ID was not specified {}", this.guest.getSocket().getInetAddress());
            this.guest.writeClient(RoomResponses.ROOM_ID_NOT_SPECIFIED);
            return;
        }

        int hostID = Integer.parseInt(splitCommand[1]);
        if(hostID == this.guest.getId()) {
            log.info("Could not join room, because host ID was the same as the guest ID {}", this.guest.getSocket().getInetAddress());
            this.guest.writeClient(RoomResponses.ROOM_ID_HOST_EQUALS_GUEST);
            return;
        }

        Player host = Main.getPlayersManager().readPlayer(hostID);
        if(host == null) {
            log.info("Could not join room, because host was not found.");
            this.guest.writeClient(RoomResponses.ROOM_HOST_NOT_FOUND);
            return;
        }

        Main.getRoomManager().joinRoom(this.guest, host);
    }
}
