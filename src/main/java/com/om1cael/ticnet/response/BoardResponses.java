package com.om1cael.ticnet.response;

import com.google.gson.Gson;
import com.om1cael.ticnet.response.model.BoardResponse;

public class BoardResponses {
    private static Gson gson = new Gson();

    public static String boardMove(char gameSymbol, int row, int column) {
        return gson.toJson(new BoardResponse("BR_600", gameSymbol, row, column));
    }
}
