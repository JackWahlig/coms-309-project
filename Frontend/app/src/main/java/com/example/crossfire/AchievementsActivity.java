package com.example.crossfire;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * The Activity which lists out all the achievements of the game, their descriptions, and whether or not the current user has unlocked them
 */
public class AchievementsActivity extends AppCompatActivity {

    /**
     * ListView which will display each achievement
     */
    ListView achievementsList;
    /**
     * ArrayList of achievement names
     */
    ArrayList<String> nameArray = new ArrayList<String>();
    /**
     * ArrayList of achievement descriptions
     */
    ArrayList<String> descArray = new ArrayList<String>();
    /**
     * ArrayList of achievement Ids
     */
    ArrayList<Integer> idArray = new ArrayList<Integer>();
    /**
     * ArrayList of integers signalling if each achievement has been unlocked
     */
    ArrayList<Integer> unlocked = new ArrayList<Integer>();

    String list_url = "http://cs309-lr-1.misc.iastate.edu:8080/achievements";
    String user_url = "http://cs309-lr-1.misc.iastate.edu:8080/players/";

    /**
     * God method for Achievement Activity. This initializes the views in the Achievements page and constructs the
     * achievement list based on values it receives from server calls. It creates an AchievementListAdapter which holds the information of
     * every achievement and whether the current user has unlocked each one and populates the table.
     * @param savedInstanceState The saved instance state of the app
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        final Bundle bundle = getIntent().getExtras();
        user_url += bundle.getInt("playerId");
        user_url += "/achievements";

        //Get the achievements this player has unlocked
        JsonArrayRequest jsonArrayRequest1 = new JsonArrayRequest(Request.Method.GET, user_url, null,
                new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for(int i = 0; i < response.length(); i++){
                                JSONObject jsonObject = response.getJSONObject(i);
                                Integer id = jsonObject.getInt("achievementId");

                                unlocked.add(id);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AchievementsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });

        JsonArrayRequest jsonArrayRequest2 = new JsonArrayRequest(Request.Method.GET, list_url, null,
                new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for(int i = 0; i < response.length(); i++){
                                JSONObject jsonObject = response.getJSONObject(i);
                                String name = jsonObject.getString("achievementName");
                                String desc = jsonObject.getString("description");
                                Integer id = jsonObject.getInt("achievementId");

                                nameArray.add(name);
                                descArray.add(desc);
                                idArray.add(id);
                            }

                            AchievementListAdapter adapter = new AchievementListAdapter(AchievementsActivity.this, nameArray, descArray, idArray, unlocked);
                            achievementsList = (ListView) findViewById(R.id.achievementsList);
                            achievementsList.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AchievementsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
                }
        });
        MySingleton.getInstance(AchievementsActivity.this).addToRequestque(jsonArrayRequest1);
        MySingleton.getInstance(AchievementsActivity.this).addToRequestque(jsonArrayRequest2);
    }
}
