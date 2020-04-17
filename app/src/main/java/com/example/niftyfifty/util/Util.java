package com.example.niftyfifty.util;

import com.example.niftyfifty.player.Player;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Util {

    public List<Player> getAllPlayerData(HashMap<String, Object> dataMap) {
        List<Player> allPlayerData = new ArrayList<Player>();
        for(Map.Entry<String, Object> mapEntry : dataMap.entrySet()) {
            Object val = mapEntry.getValue();
            List<Map<String, Object>> tmpListMap = (List<Map<String, Object>>)val;
            for(int i = 0; i < tmpListMap.size(); i++) {
                Map<String, Object> map = tmpListMap.get(i);
                if(map != null) {
                    Player aPlayer = new Player();
                    for(Map.Entry<String, Object> innerMapEntry : map.entrySet()) {
                        String innerMapKey = innerMapEntry.getKey();
                        if(innerMapKey.equalsIgnoreCase("name")) {
                            String playerName = innerMapEntry.getValue().toString();
                            aPlayer.setName(playerName);
                        } else if(innerMapKey.equalsIgnoreCase("playerId")) {
                            String playerId = innerMapEntry.getValue().toString();
                            aPlayer.setPlayerId(playerId);
                        } else if(innerMapKey.equalsIgnoreCase("playerBestScore")) {
                            int playerBestScore = Integer.parseInt("" + innerMapEntry.getValue());
                            aPlayer.setPlayerBestScore(playerBestScore);
                        }
                    }
                    allPlayerData.add(aPlayer);
                }
            }
        }
        return allPlayerData;
    }

    public String getTimeInMiliseconds() {
        Date date = new Date();
        //This method returns the time in millis
        long miliseconds = date.getTime();
        return "" + miliseconds;
    }

    public String generatePlayerId() {
        String playerId = null;
        String guestTag = "Guest";
        String currentTimeInMiliseconds = getTimeInMiliseconds();
        playerId = guestTag + currentTimeInMiliseconds;
        return playerId;
    }

    public List<String> setRanking(List<String> playerRecords) {
        int i = 0;
        for(String playerRecord : playerRecords) {
            playerRecords.set(i ,++i + "- " + playerRecord);
        }
        return playerRecords;
    }
}
