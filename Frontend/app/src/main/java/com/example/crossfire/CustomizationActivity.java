package com.example.crossfire;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
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
 * The activity where the user can customize the color of their turret and ball to be used in the game
 */
public class CustomizationActivity extends Activity implements SeekBar.OnSeekBarChangeListener {

    /**
     * Red value seekbar for the Turret
     */
    SeekBar redValueTurret;
    /**
     * Green value seekbar for the Turret
     */
    SeekBar greenValueTurret;
    /**
     * Blue value seekbar for the Turret
     */
    SeekBar blueValueTurret;
    /**
     * Red value seekbar for the Bullets
     */
    SeekBar redValueBullet;
    /**
     * Green value seekbar for the Bullets
     */
    SeekBar greenValueBullet;
    /**
     * Blue value seekbar for the Bullets
     */
    SeekBar blueValueBullet;

    /**
     * Colorbox for the turret
     */
    TextView colorboxTurret;
    /**
     * Colorbox for the bullets
     */
    TextView colorboxBullet;

    /**
     * Button used to save the colors to the server
     */
    Button save;

    /**
     * Server URL used to retrieve and save color customization data
     */
    String base_url = "http://cs309-lr-1.misc.iastate.edu:8080/players/";
    /**
     * Player ID of the current user
     */
    int playerId;

    /**
     * God method for Customization activity. This sets the views in the activity and initializes them with values
     * retrieved by a call to the server. Sliders and color boxes are set based on the current user's current RBG values
     * for their turret and bullets. When the save button is pressed, the values that are currently in each slider are
     * sent and saved in the server database of those user's new turret and bullet colors.
     * @param savedInstanceState Saved instance stat of the app
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customization);

        redValueTurret = (SeekBar)findViewById(R.id.red_value_turret);
        greenValueTurret = (SeekBar)findViewById(R.id.green_value_turret);
        blueValueTurret = (SeekBar)findViewById(R.id.blue_value_turret);

        redValueBullet = (SeekBar)findViewById(R.id.red_value_bullet);
        greenValueBullet = (SeekBar)findViewById(R.id.green_value_bullet);
        blueValueBullet = (SeekBar)findViewById(R.id.blue_value_bullet);

        colorboxTurret = (TextView)findViewById(R.id.colorbox_turret);
        colorboxBullet = (TextView)findViewById(R.id.colorbox_bullet);

        save = (Button)findViewById(R.id.save_customization);

        redValueTurret.setOnSeekBarChangeListener(this);
        greenValueTurret.setOnSeekBarChangeListener(this);
        blueValueTurret.setOnSeekBarChangeListener(this);

        redValueBullet.setOnSeekBarChangeListener(this);
        greenValueBullet.setOnSeekBarChangeListener(this);
        blueValueBullet.setOnSeekBarChangeListener(this);

        final Bundle bundle = getIntent().getExtras();
        playerId = bundle.getInt("playerId");
        String player_url = base_url + playerId;

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

                            //Set the seekbars to these value from the server
                            redValueTurret.setProgress(redTurret);
                            greenValueTurret.setProgress(greenTurret);
                            blueValueTurret.setProgress(blueTurret);

                            redValueBullet.setProgress(redBullet);
                            greenValueBullet.setProgress(greenBullet);
                            blueValueBullet.setProgress(blueBullet);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CustomizationActivity.this, "Error", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
        MySingleton.getInstance(CustomizationActivity.this).addToRequestque(jsonObjectRequest);

        //When save button is pressed, the current rbg values of the turret seek bars need to be sent to server
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> params = new HashMap<>();
                params.put("playerId", "" + playerId);
                params.put("turretColor", "" + convertDecimalToHex(redValueTurret.getProgress(), greenValueTurret.getProgress(), blueValueTurret.getProgress()));
                params.put("ballColor", "" + convertDecimalToHex(redValueBullet.getProgress(), greenValueBullet.getProgress(), blueValueBullet.getProgress()));

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH, base_url, new JSONObject(params),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                //Do nothing
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CustomizationActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                });
                MySingleton.getInstance(CustomizationActivity.this).addToRequestque(jsonObjectRequest);

                //After saved, go back to home screen
                Intent i = new Intent(CustomizationActivity.this, MainMenuActivity.class);
                i.putExtras(bundle);
                startActivity(i);
            }
        });
    }

    /**
     * Triggers whenever a seek bar on screen has been changed. It first colledts the current value of each seek bar on
     * screen, then checks to see which bar was changed and changes the appropriate value. It then updates the colors
     * od the color box textview which show the user the new color they have generated.
     * @param seekBar The seek bar that has been altered
     * @param progress The new progress the seek bar has been set to
     * @param fromTouch True if the seek bar was changed via a user interaction, false otherwise
     */
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
        //get current RGB values
        int redTurret = redValueTurret.getProgress();
        int greenTurret = greenValueTurret.getProgress();
        int blueTurret = blueValueTurret.getProgress();

        int redBullet = redValueBullet.getProgress();
        int greenBullet = greenValueBullet.getProgress();
        int blueBullet = blueValueBullet.getProgress();

        //Reference the value changing
        int id = seekBar.getId();

        //Get the changed value
        if(id == R.id.red_value_turret)
            redTurret = progress;
        else if(id == R.id.green_value_turret)
            greenTurret = progress;
        else if(id == R.id.blue_value_turret)
            blueTurret = progress;
        else if(id == R.id.red_value_bullet)
            redBullet = progress;
        else if(id == R.id.green_value_bullet)
            greenBullet = progress;
        else if(id == R.id.blue_value_bullet)
            blueBullet = progress;

        //Build and show the new color
        colorboxTurret.setBackgroundColor(Color.rgb(redTurret, greenTurret, blueTurret));
        colorboxBullet.setBackgroundColor(Color.rgb(redBullet, greenBullet, blueBullet));

    }

    /**
     * Necessary method for seek bar tracking. Not implemented
     * @param seekBar Seek bar being tracked
     */
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    /**
     * Necessary method for seek bar tracking. Not implemented
     * @param seekBar Seek bar being tracked
     */
    public void onStopTrackingTouch(SeekBar seekBar) {
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

    /**
     * Specifically takes three integer values representing red, blue, and green color values, and converts them to a single
     * hexadecimal representation of the color.
     * @param red Integer value of the red color
     * @param green Integer value of the green color
     * @param blue Integer value of the blue color
     * @return The hexadecimal string representation of the RBG color with a '#' at the beginning (will always be 6 digits in length)
     */
    public String convertDecimalToHex(int red, int green, int blue) {
        String redString, greenString, blueString;
        if(red <= 15) {
            redString = "0" + Integer.toHexString(red);
        }
        else {
            redString = Integer.toHexString(red);
        }
        if(green <= 15) {
            greenString = "0" + Integer.toHexString(green);
        }
        else {
            greenString = Integer.toHexString(green);
        }
        if(blue <= 15) {
            blueString = "0" + Integer.toHexString(blue);
        }
        else {
            blueString = Integer.toHexString(blue);
        }
        return "#" + redString + greenString + blueString;
    }
}

