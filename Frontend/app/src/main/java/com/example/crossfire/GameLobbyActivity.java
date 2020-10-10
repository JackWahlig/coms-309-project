package com.example.crossfire;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.java_websocket.client.WebSocketClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;


public class GameLobbyActivity extends AppCompatActivity {

    /**
     * The ID of this clients current game.
     */
    private int gameID; //this isnt static so it shouldn't be shared with any other instances of the lobby.

    /**
     * Player ID of Player 1
     */
    private int playerOne;

    /**
     * Player ID of Player 2
     */
    private int playerTwo;

    /**
     * Player ID of Player 3
     */
    private int playerThree;

    /**
     * Player ID of Player 4
     */
    private int playerFour;

    /**
     * Number of players in the current game.
     */
    private int playerCount;

    /**
     * List of player names in the current game.
     */
    private ArrayList<String> playerNames;

    /**
     * List of player IDs in the current game
     */
    private ArrayList<Integer> playerIDs;

    /**
     * Tracks whether or not the current game has been started.
     */
    private boolean gameStarted;

    /**
     * The clients player ID
     */
    private int myID;

    /**
     * Queue for scheduling Volley requests.
     */
    private RequestQueue queue;

    /**
     * URL for Joining a new game.
     */
    private String joinGameURL = "http://cs309-lr-1.misc.iastate.edu:8080/games/join-game";

    /**
     * URL for refreshing the current game information.
     */
    private String refreshURL = "http://cs309-lr-1.misc.iastate.edu:8080/games/";

    /**
     * URL to start game.
     */
    private String startURL = "http://cs309-lr-1.misc.iastate.edu:8080/games/";

    /**
     * URL to get list of all players.
     */
    private String playerURL = "http://cs309-lr-1.misc.iastate.edu:8080/players/";

    /**
     * Displays current game information.
     */
    ListView gameInfoList;

    /**
     * Refreshes the current game information
     */
    Button refresh;

    /**
     * Forces current game to start.
     */
    Button forceStart;

    /**
     * Leaves the current game.
     */
    Button leaveGame;

    /**
     * Player's assigned turret.
     */
    String turret;

    Bundle bundleSend = new Bundle();
    int player1, player2, player3, player4, gameId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_lobby);

        final Bundle bundle = getIntent().getExtras();
        //final Bundle bundleSend = new Bundle();
        bundleSend.putInt("playerId", bundle.getInt("playerId"));
        bundleSend.putString("username", bundle.getString("username"));
        bundleSend.putBoolean("multiplayer", true);
        myID = bundle.getInt("playerId");
        joinGameURL = joinGameURL + "/" + myID;
        playerURL += myID;
        gameInfoList = (ListView) findViewById(R.id.game_info_list);


        playerNames = new ArrayList<String>();
        playerNames.add("Player 1");
        playerNames.add("Player 2");
        playerNames.add("Player 3");
        playerNames.add("Player 4");

        playerIDs = new ArrayList<Integer>();

        /*
        Games path /games/join-game/
        Usage:
        returns a game object.

        games/gameID

        games/start-game/gameID
        Pushes players into a game.

        games/leave-game (DNE Yet)
         */
        queue = Volley.newRequestQueue(this);

        refreshGame(joinGameURL);
        refreshList();

        refresh = (Button) findViewById(R.id.bn_refresh);
        forceStart = (Button) findViewById(R.id.bn_force_start);
        leaveGame = (Button) findViewById(R.id.bn_leave_game);

        leaveGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String leave_url = "http://cs309-lr-1.misc.iastate.edu:8080/games/"+gameID+"/"+myID;
                JsonObjectRequest leave_game = new JsonObjectRequest(Request.Method.POST, leave_url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Do Nothing
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshGame(refreshURL);
                refreshList();
            }
        });

        forceStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame(startURL);
            }
        });

        //While game start conditions are not met refresh the game object every 2 seconds.
//        while(gameStarted==false && playerCount<2){
//
//            refreshGame(refreshURL + gameID);
//
//            refreshList();
//
//            //hold the thread for 2 seconds before trying again.
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//
//            }
//
//        }

        //Get user's turret color from server
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, playerURL, null,
                new Response.Listener<JSONObject>() {
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
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(GameLobbyActivity.this, "Error", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
        MySingleton.getInstance(GameLobbyActivity.this).addToRequestque(jsonObjectRequest);

    }

    /**
     * This method simply updates the visual list of players currently connected to the game that has been joined.
     */
    private void refreshList() {

        PlayerListAdapter adapter = new PlayerListAdapter(this, playerNames, playerIDs);

    }

    /**
     * This method refreshes the current game information, including the list of joined players, and starts the game if it detects a true flag on the gameStarted boolean.
     */
    private void refreshGame(String url) {
        JSONObject game = new JSONObject();
        JsonObjectRequest gameRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //try to pull as much info as possible.
                //GameID
                playerCount = 0;
                playerIDs.clear();
                try {
                    gameID = response.getInt("gameId");
                    System.out.println(gameID);
                    if (gameID < 0) {
                        gameID = -1;//This shouldn't happen.
                    }
                } catch (JSONException e) { //if we fail to get this, then we've got a problem skipper.
                    e.printStackTrace();
                }
                //Player 1
                try {
                    playerOne = response.getInt("player1");
                    System.out.println(playerOne);
                    if (!playerIDs.contains(playerOne)) {
                        playerIDs.add(playerOne);
                        playerCount++;
                    }
                } catch (JSONException e) { //Not all the players have to be initialized.
                    playerOne = -1;
                }
                //Player 2
                try {
                    playerTwo = response.getInt("player2");
                    System.out.println(playerTwo);
                    if (!playerIDs.contains(playerTwo)) {
                        playerIDs.add(playerTwo);
                        playerCount++;
                    }
                } catch (JSONException e) {
                    playerTwo = -1;
                }
                //Player 3
                try {
                    playerThree = response.getInt("player3");
                    System.out.println(playerThree);
                    if (!playerIDs.contains(playerThree)) {
                        playerIDs.add(playerThree);
                        playerCount++;
                    }
                } catch (JSONException e) {
                    playerThree = -1;
                }
                //Player 4
                try {
                    playerFour = response.getInt("player4");
                    System.out.println(playerFour);
                    if (!playerIDs.contains(playerFour)) {
                        playerIDs.add(playerFour);
                        playerCount++;
                    }
                } catch (JSONException e) {
                    playerFour = -1;
                }
                try {
                    gameStarted = response.getBoolean("gameStarted");
                } catch (JSONException e) { //if we fail to get this then we also have a problem skipper.
                    e.printStackTrace();
                }
                refreshURL = "http://cs309-lr-1.misc.iastate.edu:8080/games/" + gameID;
                startURL = "http://cs309-lr-1.misc.iastate.edu:8080/games/start-game/" + gameID;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error when joining game.");
                error.printStackTrace();
            }
        });
        gameRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(gameRequest);

        if (gameStarted || playerCount == 4) {
            startGame(startURL);
        }

    }

    /**
     * Starts the game with the current GameID.
     *
     * @param url URL to access to flip the server sided value of the gameStarted boolean.
     */
    private void startGame(String url) {
        //send start game to server
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (!response.get("player1").equals(null)) {
                                player1 = response.getInt("player1");
                                bundleSend.putInt("player1", player1);
                            }
                            if (!response.get("player2").equals(null)) {
                                player2 = response.getInt("player2");
                                bundleSend.putInt("player2", player2);
                            }
                            if (!response.get("player3").equals(null)) {
                                player3 = response.getInt("player3");
                                bundleSend.putInt("player3", player3);
                            }
                            if (!response.get("player4").equals(null)) {
                                player4 = response.getInt("player4");
                                bundleSend.putInt("player4", player4);
                            }
                            gameId = response.getInt("gameId");
                            bundleSend.putInt("gameId", gameId);
                            triggerStart();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(GameLobbyActivity.this, "Error", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
        MySingleton.getInstance(GameLobbyActivity.this).addToRequestque(jsonObjectRequest);
    }

    public void triggerStart() {
        if (playerIDs.contains(myID) && playerIDs.indexOf(myID) == 0) {//index 0 is top turret.
            turret = "top";
        } else if (playerIDs.contains(myID) && playerIDs.indexOf(myID) == 1) {
            turret = "right";
        } else if (playerIDs.contains(myID) && playerIDs.indexOf(myID) == 2) {
            turret = "bottom";
        } else {
            turret = "left";
        }
        bundleSend.putString("turret", turret);
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtras(bundleSend);
        startActivity(intent);
    }

    /**
     * Takes in the string of the hex number for the rgb values and converts them to integers
     * @param hex String encoded hexidecimal value to be converted to integer.
     */
    public int convertHexToDecimal(String hex) {
        return Integer.parseInt(hex, 16);
    }
}
