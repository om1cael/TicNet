package com.om1cael.ticnet.session;

import com.om1cael.ticnet.network.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class RoomManager {
    private final ConcurrentHashMap<Player, Optional<Player>> gameRooms = new ConcurrentHashMap<>();

    private final Logger log = LogManager.getLogger(RoomManager.class);

    public void createRoom(Player host) {
        if(gameRooms.containsKey(host)) {
            log.info("Player {} tried to create a new room, but is already playing.", host.getSocket().getInetAddress());
            return;
        }

        gameRooms.put(host, Optional.empty());

        log.info("Creating new game room with player {} ({}) as host",
                host.getId(),
                host.getSocket().getInetAddress()
        );
    }

    public void joinRoom(Player guest, Player host) {
        Optional<Player> currentGuest = gameRooms.get(host);

        if(currentGuest.isPresent()) {
            log.info("Player {} tried to enter a full game room.", host.getSocket().getInetAddress());
            return;
        }

        gameRooms.put(host, Optional.of(guest));

        log.info("Player {} entering {} game room.",
                guest.getSocket().getInetAddress(),
                host.getSocket().getInetAddress()
        );
    }

    public void deleteRoom(Player host) {
        gameRooms.remove(host);

        log.info("Deleting game room that has player {} as host",
                host.getSocket().getInetAddress()
        );
    }
}
