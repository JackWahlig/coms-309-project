package com.example.crossfire;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * A List adapter used to construct the leaderboard list in the Leaderboard Activity
 */
public class LeaderboardListAdapter extends ArrayAdapter {

    /**
     * Context of the parent activity
     */
    private final Activity context;
    /**
     * ArrayList of usernames
     */
    private final ArrayList<String> usernameArray;
    /**
     * ArrayList of user scores
     */
    private final ArrayList<Integer> userscoreArray;
    /**
     * ArrayList of player IDs
     */
    private final ArrayList<Integer> playerIdArray;
    /**
     * ArrayList of player ranks
     */
    private final ArrayList<Integer> playerRankArray;

    /**
     * Constructs a LeaderboardListAdapter. This is used to generate the rows of the leaderboard list and as such requires the names of each user,
     * their highscores, their database ids, and thier global rank.
     * @param context The activity context of the adapter
     * @param usernameArray Array containing the names of every user
     * @param userscoreArray A parallel array to the usernameArray containing every user's highscore
     * @param playerIdArray A parallel array to the usernameArray containing every user's ID
     * @param playerRankArray A parallel array to the usernameArray containing the rank of every user
     */
    public LeaderboardListAdapter(Activity context, ArrayList<String> usernameArray, ArrayList<Integer> userscoreArray,
                                  ArrayList<Integer> playerIdArray, ArrayList<Integer> playerRankArray){

        super(context,R.layout.leaderboardlist_row , usernameArray);

        this.context=context;
        this.usernameArray = usernameArray;
        this.userscoreArray = userscoreArray;
        this.playerIdArray = playerIdArray;
        this.playerRankArray = playerRankArray;
    }

    /**
     * For a given position in the leaderboard table, this sets that row's values and text based on the appropriate values in the parallel
     * leaderboard arrays for this adapter.
     * @param position The position/index of the row in the leaderboard list
     * @param view The view this list is in
     * @param parent The parent of this view
     * @return The leaderboard table row view
     */
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.leaderboardlist_row, null,true);

        TextView username = (TextView)rowView.findViewById(R.id.username);
        TextView userscore = (TextView)rowView.findViewById(R.id.user_score);
        TextView playerRank = (TextView)rowView.findViewById(R.id.rank);

        username.setText(usernameArray.get(position));
        userscore.setText("Score: " + userscoreArray.get(position));
        playerRank.setText(playerRankArray.get(position) + ".");

        return rowView;
    };
}
