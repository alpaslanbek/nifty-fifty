package com.example.niftyfifty;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.niftyfifty.fdao.FileSystemDataAccessObject;

import java.io.File;

public class ResultsActivity extends AppCompatActivity {

    private TextView tw_playerLastScore;
    private TextView tw_playerBestScore;
    private static final String actParam_PLAYERLASTSCORE = "PLAYER_LAST_SCORE";
    FileSystemDataAccessObject fileSystemDataAccessObject;
    private String projectName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        projectName = getApplicationContext().getApplicationInfo().loadLabel(getApplicationContext().getPackageManager()).toString();
        fileSystemDataAccessObject = new FileSystemDataAccessObject(getApplicationContext());

        String playerLastScore = getPlayerLastScore();
        updateBestScore(playerLastScore);
        tw_playerLastScore = (TextView) findViewById(R.id.tw_playerLastScore);
        tw_playerLastScore.setText(tw_playerLastScore.getText() + playerLastScore);
        tw_playerLastScore.setTypeface(tw_playerLastScore.getTypeface(), Typeface.BOLD);

        tw_playerBestScore = (TextView) findViewById(R.id.tw_playerBestScore);
        //tw_playerBestScore.setText(tw_playerBestScore.getText() + "0.0");
        tw_playerBestScore.setTypeface(tw_playerBestScore.getTypeface(), Typeface.BOLD);

        File directory = fileSystemDataAccessObject.getDirectory("niftyfifty.txt");
        if(directory != null) {
            Toast.makeText(getApplicationContext(), "Directory is not null: " + directory.getPath(), Toast.LENGTH_LONG).show();
        }
        File[] files = fileSystemDataAccessObject.getInternalFileData("niftyfifty.txt");
        fileSystemDataAccessObject.writeDataToFile("niftyfifty.txt", "Oguz");
        //Toast.makeText(getApplicationContext(), "File write finished: " + files.length, Toast.LENGTH_LONG).show();
        /*File[] fileArray = fileSystemDataAccessObject.getInternalFileData("niftyfifty.txt");
        if(fileArray != null) {
            if(fileArray.length > 0) {
                tw_playerBestScore.setText(fileArray[0].toString());
            } else {
                tw_playerBestScore.setText("LENGTH 0");
            }
        } else {
            tw_playerBestScore.setText("FILE ARRAY NULL");
        }*/
    }

    public String getPlayerLastScore() {
        String playerLastScore = null;
        if(getIntent().getStringExtra(actParam_PLAYERLASTSCORE) != null) {
            playerLastScore = getIntent().getStringExtra(actParam_PLAYERLASTSCORE);
        } else {
            Toast.makeText(getApplicationContext(), "Player Last Score is EMPTY!", Toast.LENGTH_SHORT).show();
        }
        return formatScoreData(playerLastScore);
    }

    public String formatScoreData(String score) {
        if(score != null) {
            if(score.length() == 5) {
                score = score.substring(0, 2) + "." + score.substring(2);
            } else if(score.length() == 6) {
                score = score.substring(0, 3) + "." + score.substring(3);
            }
        }
        return score;
    }

    public void updateBestScore(String playerLastScore) {
        String playerBestScore = null; // FETCH PLAYER BEST SCORE FROM FILE SYSTEM
        // TD- CONVERT SCORES TO NUMBERS
        // TD- COMPARE LAST AND BEST SCORES TO UPDATE BEST SCORE
        // TD- WRITE PLAYER LAST SCORE AS BEST SCORE TO FILE AND SAVE IT
    }
}