package com.example.crossfire;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * The Main Menu Activity of the application which controls navigation between the main Activities in the app including
 * the game itself.
 */
public class MainMenuActivity extends AppCompatActivity {
    /**
     * Textview which holds the username of the current user obtained from the server
     */
    TextView username;

    /**
     * Button to navigate to the Achievements Activity
     */
    Button buttonAchievements;
    /**
     * Button to navigate to the Sinlge Player Game Activity
     */
    Button buttonGame;
    /**
     * Button to navigate to the Playerlist Activity
     */
    Button buttonPlayerList;
    /**
     * Button to navigate to the current Player's Stats Activity
     */
    Button buttonPlayerStats;
    /**
     * Button to navigate to the Leaderboard Activity
     */
    Button buttonLeaderboard;
    /**
     * Button to navigate to the Multiplayer Player Game Activity
     */
    Button buttonMultiplayer;
    /**
     * Button to navigate to the Customization Activity
     */
    Button buttonCustomization;

    /**
     * Button to navigate to the Settings Acvtivity
     */
    Button buttonSettings;

    /**
     * God method of the Main Menu Activity. Holds navigation logic for all the buttons on the main menu. When a button is placed,
     * The user is directed to the appropriate Activity.
     * @param savedInstanceState Saved instance state of the app
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        username = (TextView)findViewById(R.id.username);

        //This bundle is sent to every screen so the app can always find the playerID of the user
        final Bundle bundle = getIntent().getExtras();
        username.setText("Username: " + bundle.getString("username") + ", Player ID: " + bundle.getInt("playerId"));

        buttonAchievements = (Button)findViewById(R.id.bn_achievement);
        buttonGame = (Button)findViewById(R.id.bn_game);
        buttonPlayerList = (Button)findViewById(R.id.plyList);
        buttonPlayerStats = (Button)findViewById(R.id.bn_playerstats);
        buttonLeaderboard = (Button)findViewById(R.id.bn_leaderboards);
        buttonMultiplayer = (Button) findViewById(R.id.bn_multi_game);
        buttonCustomization = (Button)findViewById(R.id.bn_customization);
        buttonSettings = (Button)findViewById(R.id.bn_settings);

        buttonAchievements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainMenuActivity.this, AchievementsActivity.class);
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        buttonGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainMenuActivity.this, PregameActivity.class);
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        buttonMultiplayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainMenuActivity.this, GameLobbyActivity.class);
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        buttonPlayerList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainMenuActivity.this, PlayerList.class);
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        buttonPlayerStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainMenuActivity.this, PlayerStatsActivity.class);
                bundle.putBoolean("sameUser", true);
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        buttonLeaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainMenuActivity.this, LeaderboardActivity.class);
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        buttonCustomization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainMenuActivity.this, CustomizationActivity.class);
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainMenuActivity.this, SettingsActivity.class);
                i.putExtras(bundle);
                startActivity(i);
            }
        });
    }
}
