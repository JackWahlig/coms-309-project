package com.example.crossfire;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {

    Switch sound, music, hints, colorblind;
    Button save;
    String base_url = "http://cs309-lr-1.misc.iastate.edu:8080/players";
    String user_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final Bundle bundle = getIntent().getExtras();
        final int playerId = bundle.getInt("playerId");
        final String username = bundle.getString("username");

        user_url = base_url + "/" + playerId;

        sound = (Switch) findViewById(R.id.sound_switch);
        music = (Switch) findViewById(R.id.music_switch);
        hints = (Switch) findViewById(R.id.hints_switch);
        colorblind = (Switch) findViewById(R.id.colorblind_switch);

        save = (Button)findViewById(R.id.save_settings);

        getSettings();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings(playerId, username);
            }
        });
    }

    public void getSettings() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, user_url, null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            sound.setChecked(response.getBoolean("soundStatus"));
                            music.setChecked(response.getBoolean("musicStatus"));
                            hints.setChecked(response.getBoolean("tipStatus"));
                            colorblind.setChecked(response.getBoolean("colorBlindStatus"));
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SettingsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
        MySingleton.getInstance(SettingsActivity.this).addToRequestque(jsonObjectRequest);
    }

    public void saveSettings(int playerId, String username) {
        HashMap<String, String> params = new HashMap<>();
        params.put("playerId", "" + playerId);
        params.put("soundStatus", String.valueOf(sound.isChecked()));
        params.put("musicStatus", String.valueOf(music.isChecked()));
        params.put("tipStatus", String.valueOf(hints.isChecked()));
        params.put("colorBlindStatus", String.valueOf(colorblind.isChecked()));

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
                Toast.makeText(SettingsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
        MySingleton.getInstance(SettingsActivity.this).addToRequestque(jsonObjectRequest1);

        Intent i = new Intent(SettingsActivity.this, MainMenuActivity.class);
        Bundle bundleSend = new Bundle();
        bundleSend.putInt("playerId", playerId);
        bundleSend.putString("username", username);
        i.putExtras(bundleSend);
        startActivity(i);
    }
}
