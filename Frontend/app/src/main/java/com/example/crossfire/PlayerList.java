package com.example.crossfire;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Activity which displays the entire list of users registered in the app. Calls the server to obtain the list of users and
 * lists them in a ListView object.
 */
public class PlayerList extends AppCompatActivity {

    /**
     * ListView which will contain the list of all users of the game
     */
    ListView playerList;
    /**
     * ArrayList of usernames
     */
    ArrayList<String> usernameArray = new ArrayList<String>();
    /**
     * ArrayList player IDs parallel to the usernameArray
     */
    ArrayList<Integer> playerIdArray = new ArrayList<Integer>();
    /**
     * Volley server request queue
     */
    private RequestQueue queue;
    /**
     * URL for the server request to get the entire list of users
     */
    String list_url = "http://cs309-lr-1.misc.iastate.edu:8080/players";

    /**
     * God method of the PlayerList Activity. Makes a server request to obtain the list of users, their usernames, and their player IDs.
     * Then uses the information to populate a ListView. If a user is clicked on, the current user will be navigated to that specific user's stats page.
     * @param savedInstanceState Saved instance state of the application
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_list);
        final Bundle bundle = getIntent().getExtras();

        queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, list_url, null,
                new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for(int i=0; i<response.length(); i++){
                                JSONObject jsonObject = response.getJSONObject(i);
                                String username = jsonObject.getString("username");
                                Integer playerId = jsonObject.getInt("playerId");

                                usernameArray.add(username);
                                playerIdArray.add(playerId);

                                PlayerListAdapter adapter = new PlayerListAdapter(PlayerList.this, usernameArray, playerIdArray);
                                playerList = (ListView) findViewById(R.id.playerList);
                                playerList.setAdapter(adapter);

                                //When a player is clicked on it takes them to their stats page
                                playerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Intent intent = new Intent(PlayerList.this, PlayerStatsActivity.class);
                                        int playerId = playerIdArray.get(position);
                                        boolean sameUser = true;  //This is always true since an Admin should see all details of a user's stats

                                        //Put player ID of selected player in bundle to send to player stats page
                                        Bundle bundleSend = new Bundle();
                                        bundleSend.putInt("playerId", playerId);
                                        bundleSend.putBoolean("sameUser", sameUser);
                                        intent.putExtras(bundleSend);
                                        startActivity(intent);
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PlayerList.this, "Error", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
        queue.add(jsonArrayRequest);
    }
}
