package com.om1cael.ticnet;

import com.google.gson.Gson;
import com.om1cael.ticnet.model.Response;

public class Responses {
    private static final Gson gson = new Gson();

    public static final String ROOM_ID_NOT_SPECIFIED = gson.toJson(new Response("R_100", "ROOM ID NOT SPECIFIED"));
    public static final String ROOM_ID_HOST_EQUALS_GUEST = gson.toJson(new Response("R_101", "ROOM ID HOST EQUALS GUEST"));
    public static final String ROOM_HOST_NOT_FOUND = gson.toJson(new Response("R_102", "ROOM HOST NOT FOUND"));
}

