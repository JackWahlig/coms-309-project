package com.example.crossfire;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A List adapter used to construct the achievements list in the Achievement Activity
 */
public class AchievementListAdapter extends ArrayAdapter {

    /**
     * Conetext of the parent activity
     */
    private final Activity context;
    /**
     * ArrayList of achievement IDs
     */
    private final ArrayList<Integer> idArray;
    /**
     * ArrayList of achievements names
     */
    private final ArrayList<String> nameArray;
    /**
     * ArrayList of achievement descriptions
     */
    private final ArrayList<String> descArray;
    /**
     * ArrayList of integers signalling if each achievement has been unlocked
     */
    private final ArrayList<Integer> unlocked;

    /**
     * Constructs an AchievementListAdapter. This is used to generate the rows of the achievements list and as such requires the names of each achievement,
     * their descriptions, their database ids, and whether or not they've been unlocked by the user
     * @param context The activity context of the adapter
     * @param nameArray Array of achievement names
     * @param descArray Array of achievement descriptions
     * @param idArray Array of achievement IDs
     * @param unlocked Array of integers signalling if each achievement is locked or unlocked
     */
    public AchievementListAdapter(Activity context, ArrayList<String> nameArray, ArrayList<String> descArray,
                                  ArrayList<Integer> idArray, ArrayList<Integer> unlocked){

        super(context,R.layout.achievementlist_row , nameArray);

        this.context=context;
        this.idArray = idArray;
        this.nameArray = nameArray;
        this.descArray = descArray;
        this.unlocked = unlocked;
    }

    /**
     * For a given position in the achievement table, this sets that row's values and text based on the appropriate values in the parallel
     * achievement arrays for this adapter.
     * @param position The position/index of the row in the achievement list
     * @param view The view this list is in
     * @param parent The parent of this view
     * @return The achievement table row view
     */
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.achievementlist_row, null,true);

        TextView nameTextField = (TextView)rowView.findViewById(R.id.achievementName);
        TextView infoTextField = (TextView)rowView.findViewById(R.id.achievementDesc);

        nameTextField.setText(nameArray.get(position));
        infoTextField.setText(descArray.get(position));

        CheckBox unlock = (CheckBox)rowView.findViewById(R.id.checkbox);
        if(unlocked.contains(idArray.get(position))) {
            unlock.setChecked(true);
        }

        return rowView;
    };
}
