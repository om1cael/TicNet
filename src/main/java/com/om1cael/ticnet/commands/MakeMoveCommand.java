package com.om1cael.ticnet.commands;

import com.om1cael.ticnet.network.Game;
import com.om1cael.ticnet.network.Player;
import com.om1cael.ticnet.response.CommandResponses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;

public class MakeMoveCommand {
    private final Player player;
    private final Game game;
    private final String clientData;

    private final Logger log = LogManager.getLogger(MakeMoveCommand.class);

    public MakeMoveCommand(Player player, Game game, String clientData) {
        this.player = player;
        this.game = game;
        this.clientData = clientData;
    }

    public void run() {
        if(game == null) {
            log.info("Player {} could not make move because game was not found", this.player.getSocket().getInetAddress());
            this.player.writeClient(CommandResponses.MAKE_MOVE_GAME_NOT_FOUND);
            return;
        }

        String[] splitClientData = this.clientData.split(" ");
        if(splitClientData.length < 3) {
            log.info("Player {} could not make move because it did not include row or column", this.player.getSocket().getInetAddress());
            this.player.writeClient(CommandResponses.MAKE_MOVE_INSUFFICIENT_ARGS);
            return;
        }

        List<Integer> rowAndColumn = this.getRowAndColumn(splitClientData);
        if(rowAndColumn.isEmpty()) {
            log.info("Player {} could not make move because row or column weren't numbers", this.player.getSocket().getInetAddress());
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
