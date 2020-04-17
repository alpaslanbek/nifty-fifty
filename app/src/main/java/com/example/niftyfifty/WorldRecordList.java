package com.example.niftyfifty;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ListActivity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.niftyfifty.databaseaccessobject.DatabaseAccessObject;
import com.example.niftyfifty.player.Player;
import com.example.niftyfifty.util.OnGetDataListener;
import com.example.niftyfifty.util.Util;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class WorldRecordList extends ListActivity {

    private DatabaseAccessObject dao;
    private ArrayAdapter<String> adapter;
    private List<Player> allPlayerData;
    private List<String> myList;
    private ListView renkList;
    private SharedPreferences pref = null;
    private String playerId;
    private ProgressBar spinner;
    private Util util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_world_record_list);

        spinner = (ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        playerId = pref.getString("PLAYER_ID", null);
        dao = new DatabaseAccessObject(getApplicationContext());
        util = new Util();

        mCheckInforInServer("players");
    }

    private void mCheckInforInServer(String child) {
        new DatabaseAccessObject().mReadDataOnce(child, new OnGetDataListener() {
            @Override
            public void onStart() {
                //DO SOME THING WHEN START GET DATA HERE
            }

            @Override
            public void onSuccess(DataSnapshot data) {
                //DO SOME THING WHEN GET DATA SUCCESS HERE
                allPlayerData = dao.parseDataSnapshot(data);
                myList = new ArrayList<String>();
                //myList.add("" + allPlayerData.size());

                for(Player aPlayer : allPlayerData) {
                    myList.add("Id:" + aPlayer.getPlayerId() + " Score:" + aPlayer.getPlayerBestScore());
                }


                // SORT STRING ARRAY
                Collections.sort(myList, new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        String firstRecord = o1.split("\\s+")[1].split(":")[1];
                        String secondRecord = o2.split("\\s+")[1].split(":")[1];
                        return (firstRecord.compareTo(secondRecord));
                    }
                });

                util.setRanking(myList);
                adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, myList){
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        ViewGroup.LayoutParams params = view.getLayoutParams();
                        params.height = 60;
                        view.setLayoutParams(params);

                        TextView textview = (TextView) view;
                        textview.setTextSize(16);
                        textview.setGravity(Gravity.CENTER_VERTICAL);
                        textview.setTextColor(Color.BLACK);


                        if (myList.get(position).contains(playerId)) {
                            view.setBackgroundResource(R.color.colorAccent);
                            textview.setTypeface(null, Typeface.BOLD);
                        } else {
                            view.setBackgroundResource(R.color.white);
                        }
                        return textview;
                    }
                };
                spinner.setVisibility(View.GONE);
                setListAdapter(adapter);
            }

            @Override
            public void onFailed(DatabaseError databaseError) {
                //DO SOME THING WHEN GET DATA FAILED HERE
            }
        });
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String item = (String) getListAdapter().getItem(position);
        //v.setBackgroundColor(Color.BLUE);
        Toast.makeText(this, "Selected " + playerId + " " + item, Toast.LENGTH_SHORT).show();
    }
}
