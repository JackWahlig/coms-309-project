package com.example.crossfire;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * The acitivty the user access prior to entering a single player game. The user uses this screen to select which turret they will control.
 */
public class PregameActivity extends Activity {

    /**
     * Radio button group where the user can select the turret they wish to control
     */
    RadioGroup selectTurret;
    /**
     * Button used to start the game
     */
    Button startGame;
    /**
     * The string which holds the value of the turret the user will be playing as in the game. Default is top
     */
    String turret = "top"; //Top turret is default
    /**
     * URL for server request to get information about the user playing the game
     */
    String player_url = "http://cs309-lr-1.misc.iastate.edu:8080/players/";

    /**
     * God method for the Pregame Activity. Calls the server to get the color of the turret and the bullets of the current
     * user and sends them to the game for the gameapanel to use. Keeps track of which radio button the user selected for determining
     * which turret they will control. Listens for click on start button to take the user to the Game Activity and start the game.
     * @param savedInstanceState Saved instance state of the game
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pregame);

        final Bundle bundle = getIntent().getExtras();
        final Bundle bundleSend = new Bundle();
        bundleSend.putInt("playerId", bundle.getInt("playerId"));
        bundleSend.putString("username", bundle.getString("username"));

        player_url += bundle.getInt("playerId");

        selectTurret = (RadioGroup)findViewById(R.id.radio_group);
        startGame = (Button)findViewById(R.id.bn_startgame);

        //Get user's turret color from server
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, player_url, null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String hexColorStringTurret = response.getString("turretColor");
                            String hexColorStringBullet = response.getString("ballColor");

                            int redTurret = convertHexToDecimal(hexColorStringTurret.substring(1, 3));
                            int greenTurret = convertHexToDecimal(hexColorStringTurret.substring(3, 5));
                            int blueTurret = convertHexToDecimal(hexColorStringTurret.substring(5, 7));

                            int redBullet = convertHexToDecimal(hexColorStringBullet.substring(1, 3));
                            int greenBullet = convertHexToDecimal(hexColorStringBullet.substring(3, 5));
                            int blueBullet = convertHexToDecimal(hexColorStringBullet.substring(5, 7));

                            bundleSend.putInt("redTurret", redTurret);
                            bundleSend.putInt("greenTurret", greenTurret);
                            bundleSend.putInt("blueTurret", blueTurret);

                            bundleSend.putInt("redBullet", redBullet);
                            bundleSend.putInt("greenBullet", greenBullet);
                            bundleSend.putInt("blueBullet", blueBullet);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PregameActivity.this, "Error", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
        MySingleton.getInstance(PregameActivity.this).addToRequestque(jsonObjectRequest);

        selectTurret.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which turret has been selected
                if(checkedId == R.id.top_turret) {
                    turret = "top";
                } else if(checkedId == R.id.right_turret) {
                    turret = "right";
                } else if(checkedId == R.id.bottom_turret){
                    turret = "bottom";
                } else if(checkedId == R.id.left_turret) {
                    turret = "left";
                }
            }
        });

        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundleSend.putString("turret", turret); //Send the string containing which turret is to be used to the game screen

                Intent intent = new Intent(PregameActivity.this, GameActivity.class);
                bundleSend.putBoolean("multiplayer", false);
                intent.putExtras(bundleSend);
                startActivity(intent);
            }
        });
    }

    /**
     * Converts a hex number represented by a string starting with a '#' character to the equivalent decimal value
     * as an integer
     * @param hex The hexadecimal string to convert
     * @return The decimal equivalent to the hex string
     */
    public int convertHexToDecimal(String hex) {
        return Integer.parseInt(hex, 16);
    }
}
