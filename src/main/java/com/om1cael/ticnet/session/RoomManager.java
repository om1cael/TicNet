package com.om1cael.ticnet.session;

import com.om1cael.ticnet.network.Game;
import com.om1cael.ticnet.network.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class RoomManager {
    private final ConcurrentHashMap<Player, Optional<Player>> gamePlayers = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Player, Game> gameRooms = new ConcurrentHashMap<>();

    private final Logger log = LogManager.getLogger(RoomManager.class);

    public void createRoom(Player host) {
        if(this.gamePlayers.containsKey(host) || host.getCurrentGame() != null) {
            log.info("Player {} tried to create a new room, but is already playing.", host.getSocket().getInetAddress());
            return;
        }

        Game game = new Game(host, Optional.empty());
        this.gamePlayers.put(host, Optional.empty());
        this.gameRooms.put(host, game);

        host.setCurrentGame(game);

        log.info("Creating new game room with player {} ({}) as host",
                host.getId(),
                host.getSocket().getInetAddress()
        );
    }

    public void joinRoom(Player guest, Player host) {
        if(guest.getCurrentGame() != null) {
            log.info("Could not join room, because is already in a game {}", guest.getSocket().getInetAddress());
            return;
        }

        Optional<Player> currentGuest = this.gamePlayers.get(host);
        if(currentGuest != null && currentGuest.isPresent()) {
            log.info("Player {} tried to enter a full game room.", host.getSocket().getInetAddress());
            return;
        }

        Game game = gameRooms.get(host);
        if(game == null) {
            log.info("Could not join room, as it was not found {}", guest.getSocket().getInetAddress());
            return;
        }

        game.setGuest(Optional.of(guest));
        this.gamePlayers.put(host, Optional.of(guest));
        guest.setCurrentGame(game);

        log.info("Player {} entering {} game room.",
                guest.getSocket().getInetAddress(),
                host.getSocket().getInetAddress()
        );
    }

    public void deleteRoom(Player player) {
        if(!this.gameRooms.containsKey(player) && player.getCurrentGame() != null) {
            log.info("Player that requested room deletion ({}) was a guest, getting host!", player.getSocket().getInetAddress());
            for(Player host : this.gamePlayers.keySet()) {
                if(this.gamePlayers.get(host).equals(Optional.of(player))) {
                    player = host;
                    break;
                }
            }
        }

        Optional<Player> guest = gamePlayers.get(player);

        player.setCurrentGame(null);
        guest.ifPresent(client -> client.setCurrentGame(null));

        gamePlayers.remove(player);
        gameRooms.remove(player);

        log.info("Deleting game room that has player {} as host",
                player.getSocket().getInetAddress()
        );
    }
}
