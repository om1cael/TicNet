package com.om1cael.ticnet.session;

import com.om1cael.ticnet.network.Game;
import com.om1cael.ticnet.network.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class RoomManager {
    private final ConcurrentHashMap<Player, Optional<Player>> gamePlayers = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Map<Player, Optional<Player>>, Game> gameRooms = new ConcurrentHashMap<>();

    private final Logger log = LogManager.getLogger(RoomManager.class);

    public void createRoom(Player host) {
        if(gamePlayers.containsKey(host)) {
            log.info("Player {} tried to create a new room, but is already playing.", host.getSocket().getInetAddress());
            return;
        }

        Game game = new Game(host, Optional.empty());
        gamePlayers.put(host, Optional.empty());
        gameRooms.put(gamePlayers, game);

        log.info("Creating new game room with player {} ({}) as host",
                host.getId(),
                host.getSocket().getInetAddress()
        );
    }

    public void joinRoom(Player guest, Player host) {
        Optional<Player> currentGuest = gamePlayers.get(host);

        if(currentGuest.isPresent()) {
            log.info("Player {} tried to enter a full game room.", host.getSocket().getInetAddress());
            return;
        }

        Game game = gameRooms.get(host);
        game.setGuest(Optional.of(guest));

        gamePlayers.put(host, Optional.of(guest));
        gameRooms.put(gamePlayers, game);

        log.info("Player {} entering {} game room.",
                guest.getSocket().getInetAddress(),
                host.getSocket().getInetAddress()
        );
    }

    public void deleteRoom(Player host) {
        gamePlayers.remove(host);

        log.info("Deleting game room that has player {} as host",
                host.getSocket().getInetAddress()
        );
    }
}
