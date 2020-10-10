package com.example.crossfire;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * The activity which runs the actual game. Uses a gamepanel to control the game logic and visuals. Takes user input through the two joysticks which
 * control the directions that the user's turret and shield face.
 */
public class GameActivity extends Activity implements JoystickView.JoystickListener {

    /**
     * Left joystick which controls the user's shield
     */
    JoystickView joystickLeft;
    /**
     * Right joystick which controls the user's turret angle
     */
    JoystickView joystickRight;
    /**
     * Gamepanel which the game is drawn on
     */
    GamePanel gamePanel;
    /**
     * The user's health bar
     */
    ProgressBar healthBar;
    /**
     * The user's current score
     */
    TextView scoreView;
    /**
     * Player ID of the current user
     */
    public int playerId;
    /**
     * Username of the current user
     */
    public String username;
    /**
     * Turret which the current user is controlling
     */
    public String turret;

    /**
     * God method for Game Activity. Initializes the gamepanl object, joystick views, health bar, and score textview for the user.
     * Sets the turret the user controls, their player ID, their username, and their turret and bullet colors in the gamepanel. Listens for
     * changes in the user's health and score and updates their health bar and score textview.
     * @param savedInstanceState Saved instance state of the app
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_game);

        joystickLeft = (JoystickView)findViewById(R.id.joystick_left);
        joystickRight = (JoystickView)findViewById(R.id.joystick_right);
        gamePanel = (GamePanel)findViewById(R.id.game_panel);
        healthBar = (ProgressBar)findViewById(R.id.health_bar);
        scoreView = (TextView)findViewById(R.id.score_num);

        final Bundle bundle = getIntent().getExtras();

        gamePanel.setTurret(bundle.getString("turret"));
        gamePanel.setPlayerId(bundle.getInt("playerId"));
        gamePanel.setUsername(bundle.getString("username"));
        gamePanel.setTurretColor(bundle.getInt("redTurret"), bundle.getInt("greenTurret"), bundle.getInt("blueTurret"));
        gamePanel.setBulletColor(bundle.getInt("redBullet"), bundle.getInt("greenBullet"), bundle.getInt("blueBullet"));

        gamePanel.setPlayer1(bundle.getInt("player1"));
        gamePanel.setPlayer2(bundle.getInt("player2"));
        gamePanel.setPlayer3(bundle.getInt("player3"));
        gamePanel.setPlayer4(bundle.getInt("player4"));
        gamePanel.setMultiplayer(bundle.getBoolean("multiplayer"));
        gamePanel.setGameId(bundle.getInt("gameId"));

        //Updates HP bar via gamePanel
        gamePanel.setOnHPChangeListener(new GamePanel.OnHPChangeListener() {
            public void onHPChanged(int HP) {
                healthBar.setProgress(HP);
            }
        });

        gamePanel.setmOnScoreChangeListener(new GamePanel.OnScoreChangeListener() {
            public void onScoreChanged(int score) {
                scoreView.setText(score + "");
            }
        });
    }

    /**
     * Sends information to the gamepanel based on how the user is moving the shield or turret joysticks. Specifically, it sends the
     * angle in degrees that either joystick is being pointed as well as whether or not either joystick is currently in its resting position
     * (has no angle of direction)
     * @param xDisplacement The displacement in the X direction from the joystick's resting position
     * @param yDisplacement The displacement in the Y direction from the joystick's resting position
     * @param baseRadius The radius of the base circle of the joystick (for trigonomtric calculations)
     * @param id The id of the joystick being moved
     */
    public void onJoystickMoved(float xDisplacement, float yDisplacement, float baseRadius, int id) {
        //Calculate direction of turret of shield
        int gunDirection = 0;
        int shieldDirection = 0;
        boolean centeredGun = false; //This is used to determine if the gun joystick is not being touched/in the center
        boolean centeredShield = false; //This is used to determine if the shield joystick is not being touched/in the center

        switch (id) {
            case R.id.joystick_left:
                if(yDisplacement <= 0 ) {
                    shieldDirection = (int) ((Math.acos(xDisplacement / baseRadius)) * (180 / Math.PI));
                }
                else {
                    shieldDirection = (int) ((Math.acos(xDisplacement / baseRadius)) * (-180 / Math.PI));
                }
                centeredGun = true;
                if(xDisplacement == 0 && yDisplacement == 0) {
                    centeredShield = true;
                }
                else {
                    centeredShield = false;
                }
                break;
            case R.id.joystick_right:
                if(yDisplacement <= 0) {
                    gunDirection = (int) ((Math.acos(xDisplacement / baseRadius)) * (180 / Math.PI));
                }
                else {
                    gunDirection = (int) ((Math.acos(xDisplacement / baseRadius)) * (-180 / Math.PI));
                }
                centeredShield = true;
                if(xDisplacement == 0 && yDisplacement == 0) {
                    centeredGun = true;
                }
                else {
                    centeredGun = false;
                }
                break;
        }
        gamePanel.update(gunDirection, shieldDirection, centeredGun, centeredShield);
    }
}
