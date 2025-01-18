package com.om1cael.ticnet.response.model;

public record BoardResponse(String code,
                            char symbol,
                            int row,
                            int column) { }
