package model;

import chess.ChessGame;

public record GameRecord(int gameID, String wUsername, String bUsername, String gameName, ChessGame game) {}