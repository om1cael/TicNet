package com.om1cael.ticnet.response;

import com.google.gson.Gson;
import com.om1cael.ticnet.response.model.GameResponse;

public class GameResponses {
    private static final Gson gson = new Gson();

    public static final String FAILED_NEW_GAME = gson.toJson(new GameResponse("GR_300", "FAILED NEW GAME", "BOTH"));

    public static final String NEW_GAME = gson.toJson(new GameResponse("GR_400", "NEW GAME", "BOTH"));
    public static final String GAME_SYMBOL_ASSIGNED(String player) {
        return gson.toJson(new GameResponse(
                "GR_401",
                "X GAME SYMBOL ASSIGNED",
                player
            )
        );
    }
}
