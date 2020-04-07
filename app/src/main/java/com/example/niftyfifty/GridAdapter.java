package com.example.niftyfifty;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class GridAdapter extends BaseAdapter {

    private Context context;
    List<String> lstSource;
    List<String> last25Source;
    Chronometer chronometer;
    private boolean chronometerStarted = false;
    private int totalNumberPressed = 0;
    String[] gameOrder = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50"};
    //List<String> gameOrderList = new ArrayList<String>();
    List<String> playerPushOrderList = new ArrayList<String>();
    List<String> gameOrderList ;

    public GridAdapter(List<String> lstSource, Context context, List<String> last25Source, Chronometer chronometer){
        this.lstSource = lstSource;
        this.last25Source = last25Source;
        this.chronometer = chronometer;
        this.context = context;
        this.gameOrderList = new LinkedList<>(Arrays.asList(gameOrder));
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
            button.setWidth(60);
            button.setHeight(60);
            button.setLayoutParams(new GridView.LayoutParams(90,90));
            button.setPadding(8,8,8,8);
            button.setText(lstSource.get(position));
            button.setTypeface(button.getTypeface(), Typeface.BOLD);
            button.setTextColor(Color.parseColor("#f0f6f7"));
            button.setBackgroundColor(Color.BLUE);
            button.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    //Toast.makeText(context, "Item value: " + button.getText().toString(), Toast.LENGTH_SHORT).show();
                    totalNumberPressed++;
                    if(!chronometerStarted) {
                        chronometer.setBase(SystemClock.elapsedRealtime());
                        chronometer.start();
                        chronometerStarted = true;
                    }

                    if(totalNumberPressed >= 70) {
                        chronometer.stop();
                        chronometer.setBase(SystemClock.elapsedRealtime());
                        Toast.makeText(context, "Too many clicks!!!", Toast.LENGTH_SHORT).show();
                    }

                    if(gameOrderList.get(0).equalsIgnoreCase(button.getText().toString())) {
                        if(gameOrderList.size() < 26) {
                            button.setBackgroundColor(Color.WHITE);
                        }
                        button.setText(last25Source.get(clickedPosition));
                        gameOrderList.remove(0);
                        if(gameOrderList.size() == 0) {
                            chronometer.stop();
                            chronometer.setBase(SystemClock.elapsedRealtime());
                            Toast.makeText(context, "Game finished successfully", Toast.LENGTH_SHORT).show();
                            Intent gamePlatformActivity = new Intent(context, ResultsActivity.class);
                            context.startActivity(gamePlatformActivity);
                        }
                    }
                    //playerPushOrderList.add(button.getText().toString());
                    //Toast.makeText(context, "Oğuz Şahin'i " + button.getText().toString() + " kere SİKEEER:)",Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
            button = (Button)convertView;
        return button;
    }

    public boolean isGameFinished(List<String> gameOrderList, List<String> playerOrderList) {
        return playerOrderList.equals(gameOrderList);
    }
}
