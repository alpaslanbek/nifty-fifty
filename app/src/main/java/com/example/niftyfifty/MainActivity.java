package com.example.niftyfifty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            startActivity(gamePlatformActivity);
        }
    }
}
