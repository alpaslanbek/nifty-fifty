package com.example.niftyfifty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.GridView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class GamePlatformActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tw_nextBoxNumber;
    private GridView gridView;
    private GridAdapter adapter;
    private List<String> lstGridSource = new ArrayList<>();
    private Button btn_restart;
    private Button btn_worldRecords;
    private List<String> last25NumberList = new ArrayList<String>();
    private Chronometer chronometer;
    private String playerBestScore;
    private SharedPreferences pref = null;
    private String playerId;
    private static String act_PLAYER_ID = "PLAYER_ID";

    String[] array_first25Numbers = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25"};
    String[] array_last25Numbers = {"26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_platform);

        playerId = getIntent().getStringExtra(act_PLAYER_ID);

        chronometer = findViewById(R.id.chronometer);

        tw_nextBoxNumber = (TextView) findViewById(R.id.tw_nextBoxNumber);

        btn_restart = (Button)findViewById(R.id.btn_restart);
        btn_restart.setOnClickListener(this);

        btn_worldRecords = (Button)findViewById(R.id.btn_worldRecords);
        btn_worldRecords.setOnClickListener(this);

        lstGridSource = Arrays.asList(array_first25Numbers);
        last25NumberList = Arrays.asList(array_last25Numbers);

        Collections.shuffle(lstGridSource);
        Collections.shuffle(last25NumberList);
        gridView = (GridView) findViewById(R.id.gridView);
        //é a classe gridadapter
        adapter = new GridAdapter(lstGridSource,GamePlatformActivity.this, last25NumberList, chronometer, tw_nextBoxNumber, playerId);
        gridView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if(v == btn_restart) {
            Collections.shuffle(lstGridSource);
            gridView = (GridView) findViewById(R.id.gridView);
            //é a classe gridadapter
            chronometer.stop();
            chronometer.setBase(SystemClock.elapsedRealtime());
            tw_nextBoxNumber.setText("1");
            adapter = new GridAdapter(lstGridSource,GamePlatformActivity.this, last25NumberList, chronometer, tw_nextBoxNumber, playerId);
            tw_nextBoxNumber.setText("1");
            gridView.setAdapter(adapter);
        } else if(v == btn_worldRecords) {
            Intent archive = new Intent(GamePlatformActivity.this, WorldRecordList.class);
            GamePlatformActivity.this.startActivity(archive);
        }
    }
}

