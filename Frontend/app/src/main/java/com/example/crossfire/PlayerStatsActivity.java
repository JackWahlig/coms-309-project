package com.example.crossfire;

import android.provider.ContactsContract;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Activity containing all the lifetime information of an app user. Includes number of games played and won, highscore, experience
 * earned, level, etc.
 */
public class PlayerStatsActivity extends AppCompatActivity {

    /**
     * Textview holding the user's username
     */
    TextView username;
    /**
     * Textview holding the user's level
     */
    TextView level;
    /**
     * Textview holding the user's number of games played
     */
    TextView gamesPlayed;
    /**
     * Textview holding the user's number of games won
     */
    TextView gamesWon;
    /**
     * Textview holding the user's highscore
     */
    TextView highscore;
    /**
     * Textview holding the user's total number of shots fired
     */
    TextView shotsFired;
    /**
     * Textview holding the user's amount of exp gained
     */
    TextView expText;
    /**
     * Progressbar showing the amount of exp the user needs to reach the next level. Only is visible for a user viewing their own stats
     */
    ContentLoadingProgressBar exp;
    /**
     * Player ID of the user being viewed
     */
    int playerId;
    /**
     * Flag which determines if the user viewing the activity if viewing their own statistics
     */
    boolean sameUser;

    /**
     * URL for the server call to get this playes information
     */
    String player_url = "http://cs309-lr-1.misc.iastate.edu:8080/players/";

    /**
     * God method of the PlayerStats Activity. Sets the UI elements by calling the server and retrieving information on the user
     * being inquired about.
     * @param savedInstanceState Saved instance state of the app
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_stats);

        username = (TextView)findViewById(R.id.username_value);
        level = (TextView)findViewById(R.id.level_number);
        //gamesPlayed = (TextView)findViewById(R.id.games_played_number);
        gamesWon = (TextView)findViewById(R.id.games_won_number);
        highscore = (TextView)findViewById(R.id.highscore_number);
        shotsFired = (TextView)findViewById(R.id.shotsfired_number);
        exp = (ContentLoadingProgressBar)findViewById(R.id.xp_progress);
        expText = (TextView)findViewById(R.id.xp_text);

        final Bundle bundle = getIntent().getExtras();
        playerId = bundle.getInt("playerId");
        sameUser = bundle.getBoolean("sameUser");
        player_url += playerId;

        //Get user info from server
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, player_url, null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            username.setText(response.getString("username"));
                            level.setText(response.getString("accountLevel"));
                            //gamesPlayed.setText(response.getString("gamesPlayed"));
                            gamesWon.setText(response.getString("gamesWon"));
                            highscore.setText(response.getString("highScore"));
                            shotsFired.setText(response.getString("shotCount"));
                            exp.setProgress(response.getInt("experience"));

                            //If this is not the current user's profile, the exp does not need to be shown
                            if (!sameUser) {
                                exp.setVisibility(View.INVISIBLE);
                                expText.setVisibility(View.INVISIBLE);
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PlayerStatsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
        MySingleton.getInstance(PlayerStatsActivity.this).addToRequestque(jsonObjectRequest);
    }
}
