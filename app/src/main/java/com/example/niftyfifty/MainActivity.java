package com.example.niftyfifty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.niftyfifty.util.Util;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    CallbackManager callbackManager = null;
    AccessToken accessToken = null;
    Button btn_guest = null;
    Util util;
    private static String pref_PLAYER_ID = "PLAYER_ID";
    private static String act_PLAYER_ID = "PLAYER_ID";
    private SharedPreferences pref = null;
    private String playerId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        util = new Util();

        btn_guest = (Button) findViewById(R.id.btn_guest);
        btn_guest.setOnClickListener(this);
        btn_guest.setText("Continue as Guest");

        callbackManager = CallbackManager.Factory.create();

        final LoginButton loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //Toast.makeText(getApplicationContext(), "SUCCESSFUL LOGIN OGUZ", Toast.LENGTH_SHORT).show();
                //Map<String, String> facebookUserInfo = getFacebookLoginInformation(loginResult.getAccessToken());
                final Intent gamePlatformActivity = new Intent(MainActivity.this, GamePlatformActivity.class);
                accessToken = loginResult.getAccessToken();
                //String name = facebookUserInfo.get("name").toString();
                //gamePlatformActivity.putExtra("name", name);
                //startActivity(gamePlatformActivity);
                // App code
                final String name = "";
                GraphRequest request = GraphRequest.newMeRequest(
                        accessToken,
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                // Application code
                                try {
                                    gamePlatformActivity.putExtra("name", response.getJSONObject().getString("name"));
                                    //gamePlatformActivity.putExtra("birthday", response.getJSONObject().getString("birthday"));
                                        //startActivity(gamePlatformActivity);
                                    Toast.makeText(getApplicationContext(), "SUCCESSFUL LOGIN " + response.getJSONObject().getString("name"), Toast.LENGTH_LONG).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name");
                request.setParameters(parameters);
                request.executeAsync();
                //startActivity(gamePlatformActivity);

            }
            @Override
            public void onCancel() {
                // App code
                Toast.makeText(getApplicationContext(), "CANCELLED LOGIN", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(getApplicationContext(), "ERRORED LOGIN", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Map<String, String> getFacebookLoginInformation(AccessToken accessToken) {
        final Map<String, String> values = new HashMap<String, String>();
        /**
         Creating the GraphRequest to fetch user details
         1st Param - AccessToken
         2nd Param - Callback (which will be invoked once the request is successful)
         **/
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            //OnCompleted is invoked once the GraphRequest is successful
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String name = object.getString("name");
                    values.put("name", object.getString("name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return values;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        if(v == btn_guest) {
            Intent gamePlatformActivity = new Intent(MainActivity.this, GamePlatformActivity.class);
            if(!isPlayerIdGenerated()) {
                String playerId = util.generatePlayerId();
                savePlayerId(playerId);
                gamePlatformActivity.putExtra(act_PLAYER_ID, playerId);
                //Toast.makeText(getApplicationContext(), "PlayerId Generated: " + playerId, Toast.LENGTH_SHORT).show();
            } else {
                playerId = getPlayerId();
                gamePlatformActivity.putExtra(act_PLAYER_ID, playerId);
                //Toast.makeText(getApplicationContext(), "PlayerId Saved: " + playerId, Toast.LENGTH_SHORT).show();
            }
            startActivity(gamePlatformActivity);
        }
    }

    public String getPlayerId() {
        String playerId;
        pref = this.getSharedPreferences("MyPref", this.MODE_PRIVATE);
        playerId = pref.getString(pref_PLAYER_ID, null);
        return playerId;
    }

    public boolean isPlayerIdGenerated() {
        boolean playerIdGenerated = true;
        pref = this.getSharedPreferences("MyPref", this.MODE_PRIVATE);
        String playerID = pref.getString(pref_PLAYER_ID, null);
        if(playerID != null) {
            return playerIdGenerated;
        }
        return false;
    }

    public void savePlayerId(String playerId) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(pref_PLAYER_ID, "" + playerId);
        editor.commit();
    }
}
