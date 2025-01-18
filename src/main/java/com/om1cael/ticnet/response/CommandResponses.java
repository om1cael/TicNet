package com.om1cael.ticnet.response;

import com.google.gson.Gson;
import com.om1cael.ticnet.Main;
import com.om1cael.ticnet.response.model.Response;

public class CommandResponses {
    private static Gson gson = Main.getGson();

    public static final String MAKE_MOVE_INSUFFICIENT_ARGS =
            gson.toJson(new Response("CR_800", "MAKE MOVE INSUFFICIENT ARGS"));

    public static final String MAKE_MOVE_GAME_NOT_FOUND =
            gson.toJson(new Response("CR_801", "MAKE MOVE GAME NOT FOUND"));

    public static final String MAKE_MOVE_ARGS_NOT_NUMBER =
            gson.toJson(new Response("CR_802", "MAKE MOVE ARGS NOT NUMBER"));
}
