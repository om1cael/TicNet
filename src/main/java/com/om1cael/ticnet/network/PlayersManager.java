package com.om1cael.ticnet.network;

import java.util.concurrent.ConcurrentHashMap;

public class PlayersManager {
    private final ConcurrentHashMap<Integer, Player> playerList = new ConcurrentHashMap<>();

    public void createPlayer(Player player) {
        this.playerList.put(player.getId(), player);
    }

    public Player readPlayer(int id) {
        return this.playerList.get(id);
    }

    public void deletePlayer(int id) {
        this.playerList.remove(id);
    }
}
