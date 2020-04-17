package com.example.niftyfifty;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.SystemClock;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.niftyfifty.databaseaccessobject.DatabaseAccessObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GridAdapter extends BaseAdapter {

    private Context context;
    List<String> lstSource;
    List<String> last25Source;
    Chronometer chronometer;
    private boolean chronometerStarted = false;
    private int totalNumberPressed = 0;
    String[] gameOrder = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50"};
    List<String> gameOrderList;
    private SharedPreferences pref = null;
    private static String pref_PLAYER_BEST_SCORE = "PLAYER_BEST_SCORE";
    private DatabaseAccessObject dao;
    private TextView tw_nextBoxNumber;
    private Vibrator vibrator = null;
    private String playerId;


    public GridAdapter(List<String> lstSource, Context context, List<String> last25Source, Chronometer chronometer, TextView tw_nextBoxNumber, String playerId){
        this.lstSource = lstSource;
        this.last25Source = last25Source;
        this.chronometer = chronometer;
        this.gameOrderList = new LinkedList<>(Arrays.asList(gameOrder));
        this.context = context;
        this.dao = new DatabaseAccessObject(this.context);
        this.tw_nextBoxNumber = tw_nextBoxNumber;
        this.playerId = playerId;
        vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
    }
    @Override
    public int getCount() {
        return lstSource.size();
    }

    @Override
    public Object getItem(int position) {
        return lstSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final int clickedPosition = position;
        final Button button;
        if(convertView == null) {
            button = new Button(context);
            //button.setWidth(80);
            //button.setHeight(80);
            //button.setLayoutParams(new GridView.LayoutParams(90,90));
            button.setPadding(8,8,8,8);
            button.setText(lstSource.get(position));
            button.setTypeface(button.getTypeface(), Typeface.BOLD);
            button.setTextColor(Color.parseColor("#f0f6f7"));
            button.setBackgroundColor(Color.BLUE);
            button.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                //Toast.makeText(context, "Item value: " + button.getText().toString(), Toast.LENGTH_SHORT).show();

                    totalNumberPressed++;
                    if(totalNumberPressed >= 60) {
                        chronometer.stop();
                        chronometer.setBase(SystemClock.elapsedRealtime());
                        Toast.makeText(context, "Too many clicks!!!", Toast.LENGTH_SHORT).show();
                    }

                    if(gameOrderList.get(0).equalsIgnoreCase(button.getText().toString())) {
                        if(!chronometerStarted) {
                            chronometer.setBase(SystemClock.elapsedRealtime());
                            chronometer.start();
                            chronometerStarted = true;
                        }

                        vibrate();
                        tw_nextBoxNumber.setText("" + (Integer.parseInt(button.getText().toString()) + 1));

                        if(gameOrderList.size() < 26) {
                            button.setBackgroundColor(Color.WHITE);
                            button.setVisibility(View.GONE);
                        }

                        button.setText(last25Source.get(clickedPosition));

                        gameOrderList.remove(0);
                        if(gameOrderList.size() == 0) {
                            chronometer.stop();
                            tw_nextBoxNumber.setVisibility(View.GONE);
                            //Toast.makeText(context, "Game finished successfully", Toast.LENGTH_SHORT).show();
                            //Intent resultsActivity = new Intent(context, ResultsActivity.class);
                            //resultsActivity.putExtra(actParam_PLAYERLASTSCORE, "" + getPlayerLastScore()); // PASS PLAYERS LAST SCORE TO RESULTSACTIVITY pass val_playerLastScore HERE
                            long playerLastScore = 0;
                            playerLastScore = getPlayerLastScore();
                            chronometer.setText("Your Score: " + formatScoreData(playerLastScore));
                            //tw_nextBoxNumber.setVisibility(View.GONE);

                            Toast.makeText(context, "Your best: " + getPlayerBestScore() + " - Your last: " + playerLastScore, Toast.LENGTH_SHORT).show();
                            if(isBestScore((int)playerLastScore)) { // TD- CHECK PLAYER LASTSCORE WITH HIS BESTSCORE
                                Toast.makeText(context, "This is your best score!!!", Toast.LENGTH_SHORT).show();
                                savePlayerBestScore(playerId, (int)playerLastScore);
                                // TD- IF LASTSCORE IS BETTER THAN BESTSCORE UPDATE FIREBASE, DO NOTHING OTHERWISE
                            }

                            //Toast.makeText(context, "Your Score:  " + (SystemClock.elapsedRealtime() - chronometer.getBase()), Toast.LENGTH_SHORT).show();
                            //chronometer.setBase(SystemClock.elapsedRealtime());
                            //context.startActivity(resultsActivity);
                        }
                    }
                    //playerPushOrderList.add(button.getText().toString());
                }
            });
        }
        else
            button = (Button)convertView;
        return button;
    }

    public void savePlayerBestScore(String playerId, int playerLastScore) {
        // SAVE BEST SCORE LOCALY
        pref = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(pref_PLAYER_BEST_SCORE, "" + playerLastScore);
        editor.commit();
        // SAVE BEST SCORE TO FIREBASE
        updatePlayerBestScore(playerId, playerLastScore);
    }

    public String getPlayerBestScore() {
        String playerBestScore = null;
        pref = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);
        playerBestScore = pref.getString(pref_PLAYER_BEST_SCORE, null);
        if(playerBestScore == null) {
            playerBestScore = "999999";
        }
        return playerBestScore;
    }

    public boolean isBestScore(int playerLastScore) {
        boolean isBestScore = false;
        String playerBestScore = getPlayerBestScore();
        int bestScore = Integer.parseInt(playerBestScore);
        if(playerLastScore < bestScore) {
            return true;
        }
        return isBestScore;
    }

    public long getPlayerLastScore() {
        long unformattedPlayerScore = SystemClock.elapsedRealtime() - chronometer.getBase();
        return unformattedPlayerScore;
    }

    public String formatScoreData(long score) {
        String s_score = "" + score;
        if(s_score != null) {
            if(s_score.length() == 5) {
                s_score = s_score.substring(0, 2) + "." + s_score.substring(2);
            } else if(s_score.length() == 6) {
                s_score = s_score.substring(0, 3) + "." + s_score.substring(3);
            }
        }
        return s_score;
    }

    public void updatePlayerBestScore(String playerId, Object playerBestScore) {
        this.dao.updateDabase(playerId, playerBestScore);
    }

    public void vibrate() {
        vibrator.vibrate(50);
    }
}