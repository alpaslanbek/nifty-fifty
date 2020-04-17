package com.example.niftyfifty.player;

public class Player {
    private String playerId;
    private String name;
    private int playerBestScore;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Player() {

    }

    public Player(String playerId, String playerName, int playerBestScore) {
        this.name = playerName;
        this.playerBestScore = playerBestScore;
        this.playerId = playerId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public int getPlayerBestScore() {
        return playerBestScore;
    }

    public void setPlayerBestScore(int playerBestScore) {
        this.playerBestScore = playerBestScore;
    }
}
