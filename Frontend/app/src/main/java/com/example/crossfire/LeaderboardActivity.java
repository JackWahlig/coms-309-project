package com.example.crossfire;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
 * The Activity which lists out all the leaderboard of the game including user's names, scores, and ranks
 */
public class LeaderboardActivity extends AppCompatActivity {

    /**
     * ListView which displays the leaderboard
     */
    ListView leaderboardList;
    /**
     * ArrayList of usernames
     */
    ArrayList<String> usernameArray = new ArrayList<String>();
    /**
     * ArrayList of user scores
     */
    ArrayList<Integer> userscoreArray = new ArrayList<Integer>();
    /**
     * ArrayList of player IDs
     */
    ArrayList<Integer> playerIdArray = new ArrayList<Integer>();
    /**
     * ArrayList of player ranks
     */
    ArrayList<Integer> playerRankArray = new ArrayList<Integer>();

    String list_url = "http://cs309-lr-1.misc.iastate.edu:8080/players";

    /**
     * God method for Leaderboard Activity. This initializes the views in the Leaderboard page and constructs the
     * leaderboard list based on values it receives from server calls. It creates an LeaderboardListAdapter which holds the information of
     * every user, their score, and ranks and populates the table. It calls methods to help sort users by score and ranks them. When a player is
     * clicked on, it also navigates the user to that specific user's stats page.
     * @param savedInstanceState Saved instance state of the app
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        //Bundle from main menu (holds user's ID)
        final Bundle bundle = getIntent().getExtras();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, list_url, null,
                new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for(int i = 0; i < response.length(); i++){
                                JSONObject jsonObject = response.getJSONObject(i);
                                String username = jsonObject.getString("username");
                                Integer userscore = jsonObject.getInt("highScore");
                                Integer playerId = jsonObject.getInt("playerId");

                                usernameArray.add(username);
                                userscoreArray.add(userscore);
                                playerIdArray.add(playerId);
                            }

                            sortScores(usernameArray, userscoreArray, playerIdArray, 0, usernameArray.size() - 1);

                            //Set ranks
                            int rank = 1;
                            int consecutiveEqual = 0; //Keeps track of how many consecutive people have the same score
                            playerRankArray.add(1); // Set first person to rank 1
                            for(int i = 1; i < userscoreArray.size(); i++) {
                                //If user has same score as previous, then ranks are the same
                                if(userscoreArray.get(i) == userscoreArray.get(i - 1)) {
                                    playerRankArray.add(playerRankArray.get(i - 1));
                                    consecutiveEqual++;
                                }
                                //Otherwise, the rank = rank of previous player(s) + how many consecutive players have the same score as the previous + 1
                                else {
                                    playerRankArray.add(playerRankArray.get(i - 1) + consecutiveEqual + 1);
                                }
                            }

                            LeaderboardListAdapter adapter = new LeaderboardListAdapter(LeaderboardActivity.this, usernameArray, userscoreArray, playerIdArray, playerRankArray);
                            leaderboardList = (ListView) findViewById(R.id.leaderboardList);
                            leaderboardList.setAdapter(adapter);

                            //When a player is clicked on it takes them to their stats page
                            leaderboardList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent intent = new Intent(LeaderboardActivity.this, PlayerStatsActivity.class);
                                    int playerId = playerIdArray.get(position);
                                    boolean sameUser;

                                    //Put player ID of selected player in bundle to send to player stats page
                                    Bundle bundleSend = new Bundle();
                                    bundleSend.putInt("playerId", playerId);

                                    //Check if the player selected is this user, if not certain stuff is excluded from stats page
                                    if(playerId == bundle.getInt("playerId")) {
                                        sameUser = true;
                                    }
                                    else {
                                        sameUser = false;
                                    }
                                    bundleSend.putBoolean("sameUser", sameUser);
                                    intent.putExtras(bundleSend);
                                    startActivity(intent);
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LeaderboardActivity.this, "Error", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
        MySingleton.getInstance(LeaderboardActivity.this).addToRequestque(jsonArrayRequest);
    }

    /**
     * An implementation of mergesort which takes an array of scores and sorts them while keeping the array of usernames and player IDs
     * parallel to the array of scores.
     * @param usernameArray Array of usernames
     * @param userscoreArray Array of scores to sort
     * @param playerIdArray Array of player IDs
     * @param l Left index of the section of the score array to sort
     * @param r Right index of the section of the score array to sort
     */
    public void sortScores(ArrayList<String> usernameArray, ArrayList<Integer> userscoreArray, ArrayList<Integer> playerIdArray, int l, int r) {
        if (l < r)
        {
            // Find the middle point
            int m = (l + r) / 2;

            // Sort first and second halves
            sortScores(usernameArray, userscoreArray, playerIdArray, l, m);
            sortScores(usernameArray, userscoreArray, playerIdArray, m + 1, r);

            // Merge the sorted halves
            merge(usernameArray, userscoreArray, playerIdArray, l, m, r);
        }
    }

    /**
     * The merge method for the implementation of mergesort using parallel array. Merges two parts of the score array back together in
     * sorted order while keeping the other arrays parallel to the soreted array.
     * @param usernameArray Array of usernames
     * @param userscoreArray Array of scores to be merged
     * @param playerIdArray Array of player IDs
     * @param l Left index of the section of one of the arrays to be merged
     * @param m Middle index between the sections of the arrays to be merged
     * @param r Right index of the section of one of the arrays to be merged
     */
    void merge(ArrayList<String> usernameArray, ArrayList<Integer> userscoreArray, ArrayList<Integer> playerIdArray, int l, int m, int r)
    {
        // Find sizes of two subarrays to be merged
        int n1 = m - l + 1;
        int n2 = r - m;

        /* Create temp arrays */
        int LScores[] = new int[n1];
        String LNames[] = new String[n1];
        int LIds[] = new int[n1];
        int RScores[] = new int[n2];
        String RNames[] = new String[n2];
        int RIds[] = new int[n2];

        /*Copy data to temp arrays*/
        for (int i = 0; i < n1; ++i) {
            LScores[i] = userscoreArray.get(l + i);
            LNames[i] = usernameArray.get(l + i);
            LIds[i] = playerIdArray.get(l + i);
        }
        for (int j = 0; j < n2; ++j) {
            RScores[j] = userscoreArray.get(m + 1 + j);
            RNames[j] = usernameArray.get(m + 1 + j);
            RIds[j] = playerIdArray.get(m + 1 + j);
        }

        /* Merge the temp arrays */
        // Initial indexes of first and second subarrays
        int i = 0, j = 0;

        // Initial index of merged subarry array
        int k = l;
        while (i < n1 && j < n2)
        {
            if (LScores[i] >= RScores[j])
            {
                userscoreArray.set(k, LScores[i]);
                usernameArray.set(k, LNames[i]);
                playerIdArray.set(k, LIds[i]);
                i++;
            }
            else
            {
                userscoreArray.set(k, RScores[j]);
                usernameArray.set(k, RNames[j]);
                playerIdArray.set(k, RIds[j]);
                j++;
            }
            k++;
        }

        /* Copy remaining elements of L[] if any */
        while (i < n1)
        {
            userscoreArray.set(k, LScores[i]);
            usernameArray.set(k, LNames[i]);
            playerIdArray.set(k, LIds[i]);
            i++;
            k++;
        }

        /* Copy remaining elements of R[] if any */
        while (j < n2)
        {
            userscoreArray.set(k, RScores[j]);
            usernameArray.set(k, RNames[j]);
            playerIdArray.set(k, RIds[j]);
            j++;
            k++;
        }
    }
}
