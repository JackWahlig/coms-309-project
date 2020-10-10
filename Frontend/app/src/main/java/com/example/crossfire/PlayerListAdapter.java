package com.example.crossfire;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * List adapter for the ListView in the PlayerList Acvtivity. Populates each row of the list with the username and user ID of every
 * user in the application
 */
public class PlayerListAdapter extends ArrayAdapter {

    /**
     * Context of the parent application
     */
    private final Activity context;
    /**
     * ArrayList of usernames
     */
    private final ArrayList<String> usernameArray;
    /**
     * ArrayList of user IDs parallel to the usernameArray
     */
    private final ArrayList<Integer> playerIdArray;

    /**
     * Constructs a PlayerListAdapter with a given username ArrayList and playerID ArrayList
     * @param context Context of parent activity
     * @param usernameArray ArrayList of usernames
     * @param playerIdArray ARrayList of player IDs
     */
    public PlayerListAdapter(Activity context, ArrayList<String> usernameArray, ArrayList<Integer> playerIdArray){

        super(context,R.layout.playerlist_row , usernameArray);

        this.context=context;
        this.usernameArray = usernameArray;
        this.playerIdArray = playerIdArray;
    }

    /**
     * Populates each row of the ListView for the PLayerList Activity. Uses the position of the row and the parallel username and
     * player ID arrays to create the row.
     * @param position Position/index of the row being populated in the list
     * @param view The view the row is being added to
     * @param parent The parent viewgroup calling this method
     * @return The row used to populate the calling ListView
     */
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.playerlist_row, null,true);

        TextView usernameTextField = (TextView)rowView.findViewById(R.id.username);
        TextView userscoreTextField = (TextView)rowView.findViewById(R.id.playerId);

        usernameTextField.setText(usernameArray.get(position));
        userscoreTextField.setText("ID: " + playerIdArray.get(position));

        return rowView;
    };
}
