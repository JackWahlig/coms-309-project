package com.example.crossfire;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * The activity that is displayed at the end of a game. Displays postgame statistics for the user including score, shots fired, time survived
 * and overall game place. Updates statistics for the current user in the server.
 */
public class PostgameActivity extends Activity {

    /**
     * Button that takes the user back to the Main Menu Activity
     */
    Button home;
    /**
     * TextView which displays the current user's place in the game played
     */
    TextView place;
    /**
     * TextView which displays the current user's score in the game played
     */
    TextView score;
    /**
     * TextView which displays the current user's highscore
     */
    TextView highscore;
    /**
     * TextView which displays the current user's number of shots fired in the game played
     */
    TextView shotsFired;
    /**
     * TextView which displays the current user's total number of shots fired
     */
    TextView totalShotsFired;
    /**
     * TextView which displays the current user's time survived in the game played
     */
    TextView time;
    /**
     * TextView which displays the current user's experience points gained in the game played
     */
    TextView expGained;
    /**
     * TextView which displays the current user's level
     */
    TextView level;
    /**
     * Value of the user's score in the game played
     */
    int scoreNum;
    /**
     * Value of the user's experience points gained in the game played
     */
    int expNum;
    /**
     * Value of the user's highscore
     */
    int userHighscore;
    /**
     * Value of the user's total shots fired
     */
    int userTotalShotsFired;
    /**
     * Value of the user's total experience points earned
     */
    int userExp;
    /**
     * Value of the user's level
     */
    int userLevel;
    /**
     * Value of the user's number of games won
     */
    int userGamesWon;
    /**
     * Progress bar which displays how much more exp the user needs to reach the next level
     */
    int gameId, playerNum;
    ContentLoadingProgressBar exp;
    /**
     * URL for the server call to retrieve stats on the user and send new stats back to update the server database
     */
    String base_url = "http://cs309-lr-1.misc.iastate.edu:8080/players";
    String game_url = "http://cs309-lr-1.misc.iastate.edu:8080/games";

    /**
     * God method of the Postgame Activity. Sets the UI elements including player place, score, and exp earned by calling the
     * server to request the information. Adds information sent from the game just completed and adds these to any necessary statistics such as total
     * shots fired. Sends back new user statistics to the server to update the datatbase
     * @param savedInstanceState Saved instance state of the app
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postgame);

        final Bundle bundle = getIntent().getExtras();
        final int placeNum = bundle.getInt("place");
        long timeNum = bundle.getLong("timeAlive");
        final int shotsFiredNum = bundle.getInt("bulletsFired");
        final int playerId = bundle.getInt("playerId");
        final String username = bundle.getString("username");
        gameId = bundle.getInt("gameId");
        playerNum = bundle.getInt("playerNum");
        scoreNum = bundle.getInt("score");

        place = (TextView)findViewById(R.id.place_text);
        score = (TextView)findViewById(R.id.score_text);
        highscore = (TextView)findViewById(R.id.highscore_text);
        shotsFired = (TextView)findViewById(R.id.shotsfired_text);
        totalShotsFired = (TextView)findViewById(R.id.totalshotsfired_text);
        time = (TextView)findViewById(R.id.time_text);
        expGained = (TextView)findViewById(R.id.xpgained_text);
        exp = (ContentLoadingProgressBar) findViewById(R.id.xp_progress);
        level = (TextView)findViewById(R.id.level_text);

        home = (Button)findViewById(R.id.bn_home);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PostgameActivity.this, MainMenuActivity.class);
                Bundle bundleSend = new Bundle();
                bundleSend.putInt("playerId", playerId);
                bundleSend.putString("username", username);
                i.putExtras(bundleSend);
                startActivity(i);
            }
        });

        //Print the place of this player
        if(placeNum == 1) {
            place.setText("You Placed: First");
            scoreNum += 1000;
        }
        else if(placeNum == 2) {
            place.setText("You Placed: Second");
            scoreNum += 500;
        }
        else if(placeNum == 3) {
            place.setText("You Placed: Third");
            scoreNum += 250;
        }
        else if(placeNum == 4) {
            place.setText("You Placed: Fourth");
            scoreNum += 100;
        }
        expNum = scoreNum / 100;

        //Print game stats
        score.setText("Score: " + scoreNum);

        shotsFired.setText("Shots Fired: " + shotsFiredNum);

        time.setText("Time lasted: " + timeNum / 1000);

        expGained.setText("EXP Gained: " + expNum);

        String player_url = base_url + "/" + playerId; //Set URL for this player

        //Obtain info for this player from server and update it accordingly
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, player_url, null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Replace highscore if necessary
                            userHighscore = response.getInt("highScore");
                            if(scoreNum > userHighscore) {
                                userHighscore = scoreNum;
                            }
                            highscore.setText("Highscore: " + userHighscore);

                            //Update total shots fired
                            userTotalShotsFired = response.getInt("shotCount");
                            userTotalShotsFired += shotsFiredNum;
                            totalShotsFired.setText("Total Shots Fired: " + userTotalShotsFired);

                            //Add exp, if exp > 100, level up
                            userExp = response.getInt("experience");
                            userLevel = response.getInt("accountLevel");
                            userExp += expNum;
                            if(userExp >= 100) {
                                userLevel++;
                                userExp -= 100;
                            }
                            exp.setProgress(userExp);
                            level.setText("Level: " + userLevel);

                            userGamesWon = response.getInt("gamesWon");
                            if(placeNum == 1) {
                                userGamesWon++;
                            }

                            HashMap<String, String> params = new HashMap<>();
                            params.put("playerId", "" + playerId);
                            params.put("highScore", "" + userHighscore);
                            params.put("shotCount", "" + userTotalShotsFired);
                            params.put("experience", "" + userExp);
                            params.put("accountLevel", "" + userLevel);
                            params.put("gamesWon", "" + userGamesWon);

                            //Update the player's stats in the server
                            JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.PATCH, base_url, new JSONObject(params),
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            //Do nothing
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(PostgameActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                    error.printStackTrace();
                                }
                            });
                            MySingleton.getInstance(PostgameActivity.this).addToRequestque(jsonObjectRequest1);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PostgameActivity.this, "Error", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
        MySingleton.getInstance(PostgameActivity.this).addToRequestque(jsonObjectRequest);

        if(gameId != 0) {
            //Update the game object data in the database
            HashMap<String, String> params = new HashMap<>();
            params.put("gameId", "" + gameId);
            if (playerNum == 1) {
                params.put("player1Score", "" + scoreNum);
            } else if (playerNum == 2) {
                params.put("player2Score", "" + scoreNum);
            } else if (playerNum == 3) {
                params.put("player3Score", "" + scoreNum);
            } else if (playerNum == 4) {
                params.put("player4Score", "" + scoreNum);
            }
            if (placeNum == 1) {
                params.put("winner", "" + playerNum);
            }
            JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.PATCH, game_url, new JSONObject(params),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //If winner, send signal to end game
                            if (placeNum == 1) {
                                game_url = game_url + "/" + "end-game" + "/" + gameId;
                                JsonObjectRequest jsonObjectRequest3 = new JsonObjectRequest(Request.Method.POST, game_url, null,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                //Do nothing
                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(PostgameActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                        error.printStackTrace();
                                    }
                                });
                                MySingleton.getInstance(PostgameActivity.this).addToRequestque(jsonObjectRequest3);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(PostgameActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }
            });
            MySingleton.getInstance(PostgameActivity.this).addToRequestque(jsonObjectRequest2);
        }
    }
}
