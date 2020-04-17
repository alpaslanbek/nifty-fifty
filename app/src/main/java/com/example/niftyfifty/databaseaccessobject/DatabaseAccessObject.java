package com.example.niftyfifty.databaseaccessobject;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.niftyfifty.player.Player;
import com.example.niftyfifty.util.OnGetDataListener;
import com.example.niftyfifty.util.Util;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

public class DatabaseAccessObject {

    private Context context;
    private FirebaseFirestore db;
    private Util util;
    List<Player> allPlayersData = new ArrayList<Player>();

    public DatabaseAccessObject(Context context) {
        this.context = context;
        util = new Util();
    }

    public DatabaseAccessObject() {
    }

    public void updateDabase(String playerId, Object playerBestScore) {
        Player player = new Player();
        player.setPlayerId(playerId);
        if(playerId.contains("Guest")) {
            player.setName("Guest");
        }
        player.setPlayerBestScore((int)playerBestScore);
        try {
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("players").child(playerId).setValue(player);
            Toast.makeText(context, "Successfully added to Firebase " + " id: " + playerId + " score: " + playerBestScore, Toast.LENGTH_LONG).show();
        } catch(Exception e) {
            Toast.makeText(context, "Error updating data: " + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public List<Player> getAllPlayerData() {
        DatabaseReference mGetReference = FirebaseDatabase.getInstance().getReference();

        mGetReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    HashMap<String, Object> dataMap = (HashMap<String, Object>) dataSnapshot.getValue();

                    allPlayersData = util.getAllPlayerData(dataMap);
                    //Toast.makeText(context, "allPlayerData: " + allPlayerData.size() + " 1st player: " + allPlayerData.get(0).getPlayerBestScore(), Toast.LENGTH_LONG).show();
                    /*for (String key : dataMap.keySet()){
                        Object data = dataMap.get(key);
                        try{
                            HashMap<String, Object> userData = (HashMap<String, Object>) data;
                            Player player = new Player((String) userData.get("playerId"), (String) userData.get("name"), (int) userData.get("playerBestScore"));
                            allPlayersValues.add(player);
                        } catch (ClassCastException cce){
                            //Toast.makeText(context, "Exception occured: " + cce.toString(), Toast.LENGTH_LONG).show();
                        }
                    }*/
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return allPlayersData;
    }

    public List<Player> getDataOnce() {
        final List<Player> allPlayersData = new ArrayList<Player>();
        // Look up all the data: Assumes that no values are NULL
        DatabaseReference mGetReference = FirebaseDatabase.getInstance().getReference().child("players");
        mGetReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //HashMap<String, Object> dataMap = (HashMap<String, Object>) dataSnapshot.getValue();
                    Iterator<DataSnapshot> dataSnapshotIterator = dataSnapshot.getChildren().iterator();
                    //Toast.makeText(context, "dataMap: " + dataSnapshot.getValue().toString(), Toast.LENGTH_LONG).show();
                    //allPlayersData = util.getAllPlayerData(dataMap);
                    while (dataSnapshotIterator.hasNext()){
                        DataSnapshot snap = dataSnapshotIterator.next();
                        String key = snap.getKey();
                        String playerName = (String) snap.child("name").getValue();
                        String playerBestScore = snap.child("playerBestScore").getValue().toString();
                        String playerId = (String) snap.child("playerId").getValue();

                        Player aPlayer = new Player();
                        aPlayer.setName(playerName);
                        aPlayer.setPlayerId(playerId);
                        aPlayer.setPlayerBestScore(Integer.parseInt(playerBestScore));
                        allPlayersData.add(aPlayer);
                        //Toast.makeText(context, "key: " + key + " playerName: " + playerName + " playerBestScore: " + playerBestScore + " playerId: " + playerId, Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return allPlayersData;
    }

    public List<Player> parseDataSnapshot(DataSnapshot dataSnapshot) {
        List<Player> allPlayersData = new ArrayList<Player>();
        if (dataSnapshot.exists()) {
            //HashMap<String, Object> dataMap = (HashMap<String, Object>) dataSnapshot.getValue();
            Iterator<DataSnapshot> dataSnapshotIterator = dataSnapshot.getChildren().iterator();
            //Toast.makeText(context, "dataMap: " + dataSnapshot.getValue().toString(), Toast.LENGTH_LONG).show();
            //allPlayersData = util.getAllPlayerData(dataMap);
            while (dataSnapshotIterator.hasNext()){
                DataSnapshot snap = dataSnapshotIterator.next();
                String key = snap.getKey();
                String playerName = (String) snap.child("name").getValue();
                String playerBestScore = snap.child("playerBestScore").getValue().toString();
                String playerId = (String) snap.child("playerId").getValue();

                Player aPlayer = new Player();
                aPlayer.setName(playerName);
                aPlayer.setPlayerId(playerId);
                aPlayer.setPlayerBestScore(Integer.parseInt(playerBestScore));
                allPlayersData.add(aPlayer);
                //Toast.makeText(context, "key: " + key + " playerName: " + playerName + " playerBestScore: " + playerBestScore + " playerId: " + playerId, Toast.LENGTH_LONG).show();
            }
        }

        return allPlayersData;
    }

    public void mReadDataOnce(String child, final OnGetDataListener listener) {
        listener.onStart();
        FirebaseDatabase.getInstance().getReference().child(child).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onFailed(databaseError);
            }
        });
    }

    public void updateDatabase_NON_USED(Map<String, Object> data) {
        db.collection("users")
            .add(data)
            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Log.d("FIREBASE", "DocumentSnapshot added with ID: " + documentReference.getId());
                    Toast.makeText(context, "Successfully added to Firebase", Toast.LENGTH_LONG).show();
                    String ex = documentReference.getPath();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w("FIREBASE", "Error adding document", e);
                    Toast.makeText(context, "Error adding to Firebase: " + e.toString(), Toast.LENGTH_LONG).show();
                    String ex = e.toString();
                }
            });
    }
}
