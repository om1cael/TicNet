package com.om1cael.ticnet.response;

import com.google.gson.Gson;
import com.om1cael.ticnet.response.model.BoardResponse;
import com.om1cael.ticnet.response.model.Response;

public class GameResponses {
    private static final Gson gson = new Gson();

    public static final String FAILED_NEW_GAME = gson.toJson(new Response("GR_300", "FAILED NEW GAME"));

    public static final String NEW_GAME = gson.toJson(new Response("GR_400", "NEW GAME"));
    public static final String GAME_SYMBOL_ASSIGNED = gson.toJson(new Response("GR_401", "X GAME SYMBOL ASSIGNED"));

    public static final String GAME_WIN = gson.toJson(new Response("GR_500", "WIN"));
    public static final String GAME_DRAW = gson.toJson(new Response("GR_501", "GAME DRAW"));
    public static final String DISCONNECTION_WIN = gson.toJson(new Response("GR_502", "DISCONNECTION WIN"));
}
