package com.om1cael.ticnet;

import com.google.gson.Gson;
import com.om1cael.ticnet.model.Response;

public class Responses {
    private static final Gson gson = new Gson();

    public static final String ROOM_ID_NOT_SPECIFIED = gson.toJson(new Response("R_100", "ROOM ID NOT SPECIFIED"));
    public static final String ROOM_ID_HOST_EQUALS_GUEST = gson.toJson(new Response("R_101", "ROOM ID HOST EQUALS GUEST"));
    public static final String ROOM_HOST_NOT_FOUND = gson.toJson(new Response("R_102", "ROOM HOST NOT FOUND"));
    public static final String ROOM_CREATION_ALREADY_PLAYING = gson.toJson(new Response("R_103", "ROOM CREATION ALREADY PLAYING"));
    public static final String ROOM_JOINING_ALREADY_PLAYING = gson.toJson(new Response("R_104", "ROOM JOINING ALREADY PLAYING"));
    public static final String ROOM_JOINING_FULL_GAME = gson.toJson(new Response("R_105", "ROOM JOINING FULL ROOM"));
    public static final String ROOM_NOT_FOUND = gson.toJson(new Response("R_106", "ROOM NOT FOUND"));

    public static final String ROOM_CREATION_SUCCESSFUL = gson.toJson(new Response("R_200", "ROOM CREATION SUCCESSFUL"));
    public static final String ROOM_JOINING_SUCCESSFUL = gson.toJson(new Response("R_201", "ROOM JOINING SUCCESSFUL"));
    public static final String ROOM_DELETION_SUCCESSFUL = gson.toJson(new Response("R_202", "ROOM DELETION SUCCESSFUL"));
}

