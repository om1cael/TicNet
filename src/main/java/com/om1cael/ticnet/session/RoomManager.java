package com.om1cael.ticnet.session;

import com.om1cael.ticnet.network.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;

public class RoomManager {
    private final ConcurrentHashMap<Player, Player> gameRooms = new ConcurrentHashMap<>();

    private final Logger log = LogManager.getLogger(RoomManager.class);

    public void createRoom(Player host) {
        log.info("Creating new game room with player {} ({}) as host",
                host.getId(),
                host.getSocket().getInetAddress()
        );
    }

    public void joinRoom(Player guest, Player host) {
        log.info("Player {} entering {} game room.",
                guest.getSocket().getInetAddress(),
                host.getSocket().getInetAddress()
        );
    }

    public void deleteRoom(Player host) {
        log.info("Deleting game room that has player {} as host",
                host.getSocket().getInetAddress()
        );
    }
}
