package com.om1cael.ticnet.commands;

import com.om1cael.ticnet.network.Game;
import com.om1cael.ticnet.network.Player;
import com.om1cael.ticnet.response.CommandResponses;

import java.util.Collections;
import java.util.List;

public class MakeMoveCommand {
    private final Player player;
    private final Game game;
    private final String clientData;

    public MakeMoveCommand(Player player, Game game, String clientData) {
        this.player = player;
        this.game = game;
        this.clientData = clientData;
    }

    public void run() {
        if(game == null) {
            this.player.writeClient(CommandResponses.MAKE_MOVE_GAME_NOT_FOUND);
            return;
        }

        String[] splitClientData = this.clientData.split(" ");
        if(splitClientData.length < 3) {
            this.player.writeClient(CommandResponses.MAKE_MOVE_INSUFFICIENT_ARGS);
            return;
        }

        List<Integer> rowAndColumn = this.getRowAndColumn(splitClientData);
        if(rowAndColumn.isEmpty()) {
            this.player.writeClient(CommandResponses.MAKE_MOVE_ARGS_NOT_NUMBER);
            return;
        }

        this.game.makeMove(this.player, rowAndColumn.getFirst(), rowAndColumn.getLast());
    }

    public List<Integer> getRowAndColumn(String[] splitClientData) {
        try {
            int row = Integer.parseInt(splitClientData[1]);
            int column = Integer.parseInt(splitClientData[2]);

            return List.of(row, column);
        } catch (NumberFormatException e) {
            return Collections.emptyList();
        }
    }
}
