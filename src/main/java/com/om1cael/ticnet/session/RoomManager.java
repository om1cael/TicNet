package com.om1cael.ticnet.session;

import com.om1cael.ticnet.Responses;
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
            host.writeClient(Responses.ROOM_CREATION_ALREADY_PLAYING);
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

        host.writeClient(Responses.ROOM_CREATION_SUCCESSFUL);
    }

    public void joinRoom(Player guest, Player host) {
        if(guest.getCurrentGame() != null) {
            log.info("Could not join room, because is already in a game {}", guest.getSocket().getInetAddress());
            guest.writeClient(Responses.ROOM_JOINING_ALREADY_PLAYING);
            return;
        }

        Optional<Player> currentGuest = this.gamePlayers.get(host);
        if(currentGuest != null && currentGuest.isPresent()) {
            log.info("Player {} tried to enter a full game room.", guest.getSocket().getInetAddress());
            guest.writeClient(Responses.ROOM_JOINING_FULL_GAME);
            return;
        }

        Game game = this.gameRooms.get(host);
        if(game == null) {
            log.info("Could not join room, as it was not found {}", guest.getSocket().getInetAddress());
            guest.writeClient(Responses.ROOM_NOT_FOUND);
            return;
        }

        game.setGuest(Optional.of(guest));
        this.gamePlayers.put(host, Optional.of(guest));
        guest.setCurrentGame(game);

        log.info("Player {} entering {} game room.",
                guest.getSocket().getInetAddress(),
                host.getSocket().getInetAddress()
        );

        guest.writeClient(Responses.ROOM_JOINING_SUCCESSFUL);
    }

    public void deleteRoom(Player player, boolean abruptStop) {
        Player actualHost = player;

        if(!this.gameRooms.containsKey(player) && player.getCurrentGame() != null) {
            log.info("Player that requested room deletion ({}) was a guest, getting host!", player.getSocket().getInetAddress());
            for(Player host : this.gamePlayers.keySet()) {
                if(this.gamePlayers.get(host).equals(Optional.of(player))) {
                    actualHost = host;
                    break;
                }
            }
        }

        Optional<Player> guest = this.gamePlayers.get(actualHost);
        Game game = this.gameRooms.get(actualHost);
        if(game != null) {
            if(abruptStop) {
                if(actualHost == player) game.setHost(null);
                else game.setGuest(Optional.empty());
            }

            game.stopGame(abruptStop);
        }

        actualHost.setCurrentGame(null);
        actualHost.writeClient(Responses.ROOM_DELETION_SUCCESSFUL);

        guest.ifPresent(client -> {
            client.setCurrentGame(null);
            client.writeClient(Responses.ROOM_DELETION_SUCCESSFUL);
        });

        this.gamePlayers.remove(actualHost);
        this.gameRooms.remove(actualHost);

        log.info("Deleting game room that has player {} as host",
                actualHost.getSocket().getInetAddress()
        );
    }
}
