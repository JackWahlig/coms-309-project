package com.example.crossfire;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;

/**
 * The surface on in the game activity screen where the game is drawn. This includes the turrets, their bullets, and powerups that appear. Also
 * controls the logic of the game.
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    /**
     * Default value of the maximum number of powerups that can appear on screen
     */
    private static final int MAX_POWERUPS = 3;
    /**
     * URI of the websocket to send and receive game data in a multiplayer game
     */
    private String websocketURL = "ws://cs309-lr-1.misc.iastate.edu:8080/websocket";

    private boolean isMultiplayer;

    /**
     * Main game thread that the game uses to run, update, and draw
     */
    private MainThread thread;
    /**
     * Websocket used for sending and receiving multiplayer data
     */
    private WebSocketClient myWebSocket;
    private WebSocketClient topWebSocket, rightWebSocket, bottomWebSocket, leftWebSocket;
    /**
     * Turret on the bottom of the gamepanel
     */
    private Turret bottomTurret;
    /**
     * Turret on the left side of the gamepanel
     */
    private Turret leftTurret;
    /**
     * Turret on the top of the gamepanel
     */
    private Turret topTurret;
    /**
     * Turret on the right of the gamepanel
     */
    private Turret rightTurret;
    /**
     * The value of the turret position that this user will control
     */
    String turret;

    int player1, player2, player3, player4, gameId;
    /**
     * The turret that this current user will control
     */
    private Turret myTurret; //This is the turret that the player will control

    /**
     * The list of turrets that this user will not control (3 of them)
     */
    private ArrayList<Turret> otherTurrets;
    /**
     * The number of turrets still alive in the game
     */
    private int numAlive;
    /**
     * The ID of the current user playing the game
     */
    private int playerId;
    /**
     * The current score of the user currently playing the game
     */
    private int score;
    /**
     * Red value of the turret color
     */
    private int redTurret;
    /**
     * Blue value of the turret color
     */
    private int blueTurret;
    /**
     * Green value of the turret color
     */
    private int greenTurret;
    /**
     * Red value of the bullet color
     */
    private int redBullet;
    /**
     * Blue value of the bullet color
     */
    private int blueBullet;
    /**
     * Green value of the bullet color
     */
    private int greenBullet;
    /**
     * Username of the current user
     */
    private String username;
    /**
     * ArrayList of powerups on screen
     */
    private static ArrayList<PowerUp> activePowerups; //Active powerups.

    /**
     * Timer of powerups
     */
    private Timer powerUpTimer;

    /**
     * Contructs a Gamepanel. Starts the
     * @param context The contet of the parent activity
     */
    public GamePanel(Context context) {
        super(context);

        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);
        //initializeGame();

        setFocusable(true);
    }

    /**
     * Contructs a Gamepanel. Starts the
     * @param context The contet of the parent activity
     */
    public GamePanel(Context context, AttributeSet attributes) {
        super(context, attributes);

        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);
        //initializeGame();

        setFocusable(true);
    }

    /**
     * Contructs a Gamepanel. Starts the
     * @param context The contet of the parent activity
     * @param attributes Attribute set
     * @param style Style
     */
    public GamePanel(Context context, AttributeSet attributes, int style) {
        super(context, attributes, style);

        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);
        //initializeGame();

        setFocusable(true);
    }

    /**
     * Onsurface creation, calls the initialize() method and starts the game thread running
     * @param holder The holder of the surface
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new MainThread(getHolder(), this);

        initializeGame();
        thread.setRunning(true);
        thread.start();
    }

    //This function will be called whenever a new random powerup is needed.
    private static PowerUp getRandomPowerUp(int screenWidth, int screenHeight){
        Random rand = new Random();
        int n = rand.nextInt(PowerUp.NUM_POWERUPS);
        int color = Color.rgb(225,225,153);
        if (n==0){
            return new MaxHealth(rand.nextInt(screenWidth),rand.nextInt(screenHeight),0, Color.rgb(225,225,153));
        } else if(n==1){
            return new AddHealth(rand.nextInt(screenWidth),rand.nextInt(screenHeight),0, Color.rgb(225,153,225));
        } else if(n==2){
            return new FireRateBuff(rand.nextInt(screenWidth),rand.nextInt(screenHeight),0, Color.rgb(225,153,153));
        } else if(n==3) {
            return new VelocityBuff(rand.nextInt(screenWidth), rand.nextInt(screenHeight), 0, Color.rgb(153, 225, 153));
        }else if(n==4){
            return new InvulnerabilityBuff(rand.nextInt(screenWidth), rand.nextInt(screenHeight), 0, Color.rgb(153, 153, 225));
        }
        return null;
    }

    public static void addNewPowerUp(int screenWidth, int screenHeight){
        activePowerups.add(getRandomPowerUp(screenWidth,screenHeight));
    }

    /**
     * Initializes game objects such as turrets and the websocket and starts the game
     */
    public void initializeGame() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        bottomTurret = new Turret((screenWidth / 2), screenWidth, 90);
        leftTurret = new Turret(0, (screenWidth / 2), 0);
        topTurret = new Turret((screenWidth / 2), 0, 270);
        rightTurret = new Turret(screenWidth, (screenWidth / 2), 180);

        //Initialize the list of powerups.
        activePowerups = new ArrayList<PowerUp>();
        powerUpTimer = new Timer();
        PowerUpEvent generatePowerup = new PowerUpEvent(screenWidth,screenHeight);
        powerUpTimer.scheduleAtFixedRate(generatePowerup, 10000,10000);

        //Based on value of turret string, select myTurret
        if(turret.equals("top")) {
            myTurret = topTurret;
        }
        else if(turret.equals("right")) {
            myTurret = rightTurret;
        }
        else if(turret.equals("bottom")) {
            myTurret = bottomTurret;
        }
        else if(turret.equals("left")) {
            myTurret = leftTurret;
        }
        //Get user's turret color from the bundle and set it
        myTurret.setColor(Color.rgb(redTurret, greenTurret, blueTurret));
        myTurret.setBulletColor(Color.rgb(redBullet, greenBullet, blueBullet));
        score = 0; //Initialize score to be 0

        if(isMultiplayer) {
            try {
                String myWebSocketURL = websocketURL + "/" + playerId;
                myWebSocket = new WebSocketClient(new URI(myWebSocketURL)) {
                    @Override
                    public void onOpen(ServerHandshake serverHandshake) {
                        Log.d("OPEN", "run() returned: " + "you are connecting");
                    }

                    @Override
                    public void onMessage(String s) {
                        Log.d("", "run() returned: " + s);
                    }

                    @Override
                    public void onClose(int i, String s, boolean b) {
                        Log.d("CLOSE", "onClose() returned: " + s);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.d("Exception:", e.toString());
                    }
                };
            } catch (URISyntaxException e) {
                Log.d("Exception:", e.getMessage().toString());
                e.printStackTrace();
            }
            myWebSocket.connect();

            //Websocket creation for each turret
            if (player1 != 0) {
                try {
                    String topWebSocketURL = websocketURL + "/" + player1;
                    topWebSocket = new WebSocketClient(new URI(topWebSocketURL)) {
                        @Override
                        public void onOpen(ServerHandshake serverHandshake) {
                            Log.d("OPEN", "run() returned: " + "Player 1 is connecting");
                        }

                        @Override
                        public void onMessage(String s) {
                            Log.d("", "run() returned: " + s);
                            try {
                                JSONParser parser = new JSONParser();
                                JSONObject message = (JSONObject) parser.parse(s);
                                int turretAngle = ((Long) message.get("turretAngle")).intValue();
                                int shieldAngle = ((Long) message.get("shieldAngle")).intValue();
                                int health = ((Long) message.get("health")).intValue();
                                if (!topTurret.equals(myTurret)) {
                                    topTurret.setGunDirection(turretAngle);
                                    topTurret.setShieldDirection(shieldAngle);
                                    topTurret.setHP(health);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onClose(int i, String s, boolean b) {
                            Log.d("CLOSE", "onClose() returned: " + s);
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.d("Exception:", e.toString());
                        }
                    };
                } catch (URISyntaxException e) {
                    Log.d("Exception:", e.getMessage().toString());
                    e.printStackTrace();
                }
                topWebSocket.connect();
            }

            if (player2 != 0) {
                try {
                    String rightWebSocketURL = websocketURL + "/" + player2;
                    rightWebSocket = new WebSocketClient(new URI(rightWebSocketURL)) {
                        @Override
                        public void onOpen(ServerHandshake serverHandshake) {
                            Log.d("OPEN", "run() returned: " + "Player 2 is connecting");
                        }

                        @Override
                        public void onMessage(String s) {
                            Log.d("", "run() returned: " + s);
                            try {
                                JSONParser parser = new JSONParser();
                                JSONObject message = (JSONObject) parser.parse(s);
                                int turretAngle = ((Long) message.get("turretAngle")).intValue();
                                int shieldAngle = ((Long) message.get("shieldAngle")).intValue();
                                int health = ((Long) message.get("health")).intValue();
                                if (!rightTurret.equals(myTurret)) {
                                    rightTurret.setGunDirection(turretAngle);
                                    rightTurret.setShieldDirection(shieldAngle);
                                    rightTurret.setHP(health);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onClose(int i, String s, boolean b) {
                            Log.d("CLOSE", "onClose() returned: " + s);
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.d("Exception:", e.toString());
                        }
                    };
                } catch (URISyntaxException e) {
                    Log.d("Exception:", e.getMessage().toString());
                    e.printStackTrace();
                }
                rightWebSocket.connect();
            }

            if (player3 != 0) {
                try {
                    String bottomWebSocketURL = websocketURL + "/" + player3;
                    bottomWebSocket = new WebSocketClient(new URI(bottomWebSocketURL)) {
                        @Override
                        public void onOpen(ServerHandshake serverHandshake) {
                            Log.d("OPEN", "run() returned: " + "Player3 is connecting");
                        }

                        @Override
                        public void onMessage(String s) {
                            Log.d("", "run() returned: " + s);
                            try {
                                JSONParser parser = new JSONParser();
                                JSONObject message = (JSONObject) parser.parse(s);
                                int turretAngle = ((Long) message.get("turretAngle")).intValue();
                                int shieldAngle = ((Long) message.get("shieldAngle")).intValue();
                                int health = ((Long) message.get("health")).intValue();
                                if (!bottomTurret.equals(myTurret)) {
                                    bottomTurret.setGunDirection(turretAngle);
                                    bottomTurret.setShieldDirection(shieldAngle);
                                    bottomTurret.setHP(health);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onClose(int i, String s, boolean b) {
                            Log.d("CLOSE", "onClose() returned: " + s);
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.d("Exception:", e.toString());
                        }
                    };
                } catch (URISyntaxException e) {
                    Log.d("Exception:", e.getMessage().toString());
                    e.printStackTrace();
                }
                bottomWebSocket.connect();
            }

            if (player4 != 0) {
                try {
                    String leftWebSocketURL = websocketURL + "/" + player4;
                    leftWebSocket = new WebSocketClient(new URI(leftWebSocketURL)) {
                        @Override
                        public void onOpen(ServerHandshake serverHandshake) {
                            Log.d("OPEN", "run() returned: " + "Player 4 is connecting");
                        }

                        @Override
                        public void onMessage(String s) {
                            Log.d("", "run() returned: " + s);
                            try {
                                JSONParser parser = new JSONParser();
                                JSONObject message = (JSONObject) parser.parse(s);
                                int turretAngle = ((Long) message.get("turretAngle")).intValue();
                                int shieldAngle = ((Long) message.get("shieldAngle")).intValue();
                                int health = ((Long) message.get("health")).intValue();
                                if (!leftTurret.equals(myTurret)) {
                                    leftTurret.setGunDirection(turretAngle);
                                    leftTurret.setShieldDirection(shieldAngle);
                                    leftTurret.setHP(health);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onClose(int i, String s, boolean b) {
                            Log.d("CLOSE", "onClose() returned: " + s);
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.d("Exception:", e.toString());
                        }
                    };
                } catch (URISyntaxException e) {
                    Log.d("Exception:", e.getMessage().toString());
                    e.printStackTrace();
                }
                leftWebSocket.connect();
            }
        }
        //Message before game starts
        try {
            for(int x = 3; x >= 1; x--) {
                Toast pregameMessage = Toast.makeText(this.getContext(), "Game Starts in: " + x, Toast.LENGTH_SHORT);
                pregameMessage.setGravity(Gravity.CENTER, 0, (-1) * (screenHeight / 4));
                pregameMessage.show();
                thread.sleep(Toast.LENGTH_SHORT);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Needed for surface creation. Not implemented
     * @param holder
     * @param format
     * @param width
     * @param height
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    /**
     * Stops the game thread when the game ends and destoys the gamepanel surface
     * @param holder The surface hodler of the gamepanel
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while(retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch(Exception e) {e.printStackTrace();}
            retry = false;
        }
    }

    /**
     * Interface for updating the Turret's health
     */
    public interface OnHPChangeListener{
        /**
         * Updates the health bar of the user when their turret's health changes
         * @param HP The new HP of the turret
         */
        public void onHPChanged(int HP);
    }

    /**
     * Instance of HP listener
     */
    OnHPChangeListener mOnHPChangeListener;

    /**
     * Sets the HP listener
     * @param onHPChangeListener The HP listener
     */
    public void setOnHPChangeListener(OnHPChangeListener onHPChangeListener){
        mOnHPChangeListener = onHPChangeListener;
    }

    /**
     * Interface for updating the user's score
     */
    public interface OnScoreChangeListener{
        /**
         * Updates the users score
         * @param score The user's new score
         */
        public void onScoreChanged(int score);
    }

    /**
     * Instance of score listener
     */
    OnScoreChangeListener mOnScoreChangeListener;

    /**
     * Sets the score listener
     * @param onScoreChangeListener The score listener
     */
    public void setmOnScoreChangeListener(OnScoreChangeListener onScoreChangeListener) {
        mOnScoreChangeListener = onScoreChangeListener;
    }

    /**
     * Sets the position of the turret
     * @param turret The position of the turret
     */
    public void setTurret(String turret) {
        this.turret = turret;
    }

    /**
     * Sets the player ID
     * @param playerId The player ID
     */
    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    /**
     * Sest the username of the current user
     * @param username The user's username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets the RBG colors of the turret
     * @param red Red value of the turret color
     * @param green Green value of the turret color
     * @param blue Blue value of the turret color
     */
    public void setTurretColor(int red, int green, int blue) {
        this.redTurret = red;
        this.greenTurret = green;
        this.blueTurret = blue;
    }

    /**
     * Sets the RBG colors of teh bullet
     * @param red Red value of the bullet color
     * @param green Green value of the bullet color
     * @param blue Blue value of the bullet color
     */
    public void setBulletColor(int red, int green, int blue) {
        this.redBullet = red;
        this.greenBullet = green;
        this.blueBullet = blue;
    }

    public void setMultiplayer(boolean isMultiplayer) {
        this.isMultiplayer = isMultiplayer;
    }

    public void setPlayer1(int player1) {
        this.player1 = player1;
    }

    public void setPlayer2(int player2) {
        this.player2 = player2;
    }

    public void setPlayer3(int player3) {
        this.player3 = player3;
    }

    public void setPlayer4(int player4) {
        this.player4 = player4;
    }

    public void setGameId(int gameId) { this.gameId = gameId; }
    /**
     * Updates the gamepanel and all the objects in them. Holds most of the game logic including hit detection and health updates.
     * @param frameCount The framecount of the game
     */
    public void update(int frameCount) {

        //Only update active turrets
        if(bottomTurret.isActive()) {
            bottomTurret.update(frameCount);
        }
        if(leftTurret.isActive()) {
            leftTurret.update(frameCount);
        }
        if(topTurret.isActive()) {
            topTurret.update(frameCount);
        }
        if(rightTurret.isActive()) {
            rightTurret.update(frameCount);
        }

        //detect collision between bullets and powerups.
        for(Bullet b : bottomTurret.getBullets()){
            for(PowerUp p : activePowerups){ //check bullet against each powerup.
                if(b.getBulletHitBox().intersect(p.getHitbox())){
                    p.setHP(0,b.getShooter());
                    p.setActive(false);
                    b.setActive(false);
                    activePowerups.remove(p);
                }
            }
        }
        for(Bullet b : leftTurret.getBullets()){
            for(PowerUp p : activePowerups){ //check bullet against each powerup.
                if(b.getBulletHitBox().intersect(p.getHitbox())){
                    p.setHP(0,b.getShooter());
                    p.setActive(false);
                    b.setActive(false);
                    activePowerups.remove(p);
                }
            }
        }
        for(Bullet b : rightTurret.getBullets()){
            for(PowerUp p : activePowerups){ //check bullet against each powerup.
                if(b.getBulletHitBox().intersect(p.getHitbox())){
                    p.setHP(0,b.getShooter());
                    p.setActive(false);
                    b.setActive(false);
                    activePowerups.remove(p);
                }
            }
        }
        for(Bullet b : topTurret.getBullets()){
            for(PowerUp p : activePowerups){ //check bullet against each powerup.
                if(b.getBulletHitBox().intersect(p.getHitbox())){
                    p.setHP(0,b.getShooter());
                    p.setActive(false);
                    b.setActive(false);
                    activePowerups.remove(p);
                }
            }
        }

        //Detect collision between bullets and turrets
        for(Bullet b : bottomTurret.getBullets()) {
            if(b.isActive()) {
                //Hits turrets
                if(b.getBulletHitBox().intersect(leftTurret.getTurretHitBox())) {
                    leftTurret.setHP(leftTurret.getHP() - b.getDamage());
                    if(bottomTurret.equals(myTurret)) { score += 10; }
                    b.setActive(false);
                }
                else if(b.getBulletHitBox().intersect(topTurret.getTurretHitBox())) {
                    topTurret.setHP(topTurret.getHP() - b.getDamage());
                    if(bottomTurret.equals(myTurret)) { score += 10; }
                    b.setActive(false);
                }
                else if(b.getBulletHitBox().intersect(rightTurret.getTurretHitBox())) {
                    rightTurret.setHP(rightTurret.getHP() - b.getDamage());
                    if(bottomTurret.equals(myTurret)) { score += 10; }
                    b.setActive(false);
                }
                //Hits shields
                else if(b.getBulletHitBox().intersect(leftTurret.getShield().getShieldHitBox())) {
                    b.setActive(false);
                }
                else if(b.getBulletHitBox().intersect(topTurret.getShield().getShieldHitBox())) {
                    b.setActive(false);
                }
                else if(b.getBulletHitBox().intersect(rightTurret.getShield().getShieldHitBox())) {
                    b.setActive(false);
                }
            }
        }

        for(Bullet b : leftTurret.getBullets()) {
            if(b.isActive()) {
                //Hits Turrte
                if(b.getBulletHitBox().intersect(bottomTurret.getTurretHitBox())) {
                    bottomTurret.setHP(bottomTurret.getHP() - b.getDamage());
                    if(leftTurret.equals(myTurret)) { score += 10; }
                    b.setActive(false);
                }
                else if(b.getBulletHitBox().intersect(topTurret.getTurretHitBox())) {
                    topTurret.setHP(topTurret.getHP() - b.getDamage());
                    if(leftTurret.equals(myTurret)) { score += 10; }
                    b.setActive(false);
                }
                else if(b.getBulletHitBox().intersect(rightTurret.getTurretHitBox())) {
                    rightTurret.setHP(rightTurret.getHP() - b.getDamage());
                    if(leftTurret.equals(myTurret)) { score += 10; }
                    b.setActive(false);
                }
                //Hits shields
                else if(b.getBulletHitBox().intersect(bottomTurret.getShield().getShieldHitBox())) {
                    b.setActive(false);
                }
                else if(b.getBulletHitBox().intersect(topTurret.getShield().getShieldHitBox())) {
                    b.setActive(false);
                }
                else if(b.getBulletHitBox().intersect(rightTurret.getShield().getShieldHitBox())) {
                    b.setActive(false);
                }
            }
        }

        for(Bullet b : topTurret.getBullets()) {
            //Hits turret
            if(b.isActive()) {
                if(b.getBulletHitBox().intersect(leftTurret.getTurretHitBox())) {
                    leftTurret.setHP(leftTurret.getHP() - b.getDamage());
                    if(topTurret.equals(myTurret)) { score += 10; }
                    b.setActive(false);
                }
                else if(b.getBulletHitBox().intersect(bottomTurret.getTurretHitBox())) {
                    bottomTurret.setHP(bottomTurret.getHP() - b.getDamage());
                    if(topTurret.equals(myTurret)) { score += 10; }
                    b.setActive(false);
                }
                else if(b.getBulletHitBox().intersect(rightTurret.getTurretHitBox())) {
                    rightTurret.setHP(rightTurret.getHP() - b.getDamage());
                    if(topTurret.equals(myTurret)) { score += 10; }
                    b.setActive(false);
                }
                //Hits shield
                else if(b.getBulletHitBox().intersect(leftTurret.getShield().getShieldHitBox())) {
                    b.setActive(false);
                }
                else if(b.getBulletHitBox().intersect(bottomTurret.getShield().getShieldHitBox())) {
                    b.setActive(false);
                }
                else if(b.getBulletHitBox().intersect(rightTurret.getShield().getShieldHitBox())) {
                    b.setActive(false);
                }
            }
        }

        for(Bullet b : rightTurret.getBullets()) {
            if(b.isActive()) {
                //Hits turret
                if(b.getBulletHitBox().intersect(leftTurret.getTurretHitBox())) {
                    leftTurret.setHP(leftTurret.getHP() - b.getDamage());
                    if(rightTurret.equals(myTurret)) { score += 10; }
                    b.setActive(false);
                }
                else if(b.getBulletHitBox().intersect(topTurret.getTurretHitBox())) {
                    topTurret.setHP(topTurret.getHP() - b.getDamage());
                    if(rightTurret.equals(myTurret)) { score += 10; }
                    b.setActive(false);
                }
                else if(b.getBulletHitBox().intersect(bottomTurret.getTurretHitBox())) {
                    bottomTurret.setHP(bottomTurret.getHP() - b.getDamage());
                    if(rightTurret.equals(myTurret)) { score += 10; }
                    b.setActive(false);
                }
                //Hits shields
                else if(b.getBulletHitBox().intersect(leftTurret.getShield().getShieldHitBox())) {
                    b.setActive(false);
                }
                else if(b.getBulletHitBox().intersect(topTurret.getShield().getShieldHitBox())) {
                    b.setActive(false);
                }
                else if(b.getBulletHitBox().intersect(bottomTurret.getShield().getShieldHitBox())) {
                    b.setActive(false);
                }
            }
        }

        //Update HP bar
        if(mOnHPChangeListener != null){
            mOnHPChangeListener.onHPChanged(myTurret.getHP());
        }

        //Update Score
        if(mOnScoreChangeListener != null) {
            mOnScoreChangeListener.onScoreChanged(score);
        }

        JSONObject sendData = new JSONObject();
        if(isMultiplayer) {
            sendData.put("gameId", 1);
            sendData.put("playerId", playerId);
            sendData.put("turretAngle", myTurret.getGunDirection());
            sendData.put("shieldAngle", myTurret.getShieldDirection());
            sendData.put("health", myTurret.getHP());

            if (myWebSocket.isOpen()) {
                myWebSocket.send(String.valueOf(sendData));
            }
        }

        //Count how many turrets are still alive
        numAlive = 0;
        if(topTurret.isActive()) { numAlive++; }
        if(rightTurret.isActive()) { numAlive++; }
        if(bottomTurret.isActive()) { numAlive++; }
        if(leftTurret.isActive()) { numAlive++; }

        //End game if user dies or if numAlive <= 1
        if((!myTurret.isActive()) || numAlive <= 1) {
            thread.setRunning(false);
            Intent i = new Intent(getContext(), PostgameActivity.class);
            Bundle bundleSend = new Bundle();
            //Get time turret was alive
            long time;
            if(myTurret.isActive()) {
                time = System.currentTimeMillis() - myTurret.getTimeAlive();
            }
            else {
                time = myTurret.getTimeAlive();
            }
            int bulletsFired = myTurret.getNumBulletsFired();
            bundleSend.putLong("timeAlive", time);
            bundleSend.putInt("bulletsFired", bulletsFired);
            bundleSend.putInt("playerId", playerId);
            bundleSend.putString("username", username);
            bundleSend.putInt("score", score);
            bundleSend.putInt("gameId", gameId);
            if(playerId == player1) {
                bundleSend.putInt("playerNum", 1);
            }
            else if(playerId == player2) {
                bundleSend.putInt("playerNum", 2);
            }
            else if(playerId == player3) {
                bundleSend.putInt("playerNum", 3);
            }
            else if(playerId == player4) {
                bundleSend.putInt("playerNum", 4);
            }
            if(numAlive <= 1) {
                //First place
                if(myTurret.isActive()) {
                    bundleSend.putInt("place", 1);
                }
                else {
                    bundleSend.putInt("place", 2);
                }
            }
            else {
                //Third and Fourth places
                bundleSend.putInt("place", numAlive + 1);
            }
            i.putExtras(bundleSend);
            getContext().startActivity(i);
        }
    }

    /**
     * Updates the gun and shield directions for the user's turret based on data sent from the joysticks in the game activity
     * @param gunDirection The new gun direction of the user's turret
     * @param shieldDirection The new shield direction of teh user's turret
     * @param centeredGun If the gun is centered it is true, false otherwise
     * @param centeredShield If the shield is centered it is true, false otherwise
     */
    public void update(int gunDirection, int shieldDirection, boolean centeredGun, boolean centeredShield) {
        //If turret is not being touched, each turret will point in a default direction
        if(centeredGun) {
            myTurret.setGunDirection(myTurret.getOffset());
        }
        else {
            myTurret.setGunDirection(gunDirection);
        }

        if(centeredShield) {
            myTurret.setShieldDirection(myTurret.getOffset());
        }
        else {
            myTurret.setShieldDirection(shieldDirection);
        }
    }

    /**
     * Draws the game objects on the screen every frame including turrets, bullets, and powerups
     * @param canvas The canvas to be drawn on
     */
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        canvas.drawColor(Color.WHITE);

        //draw active PowerUps
        for(PowerUp p : activePowerups){
            p.draw(canvas);
        }

        //Only draw active turrets
        if(bottomTurret.isActive()) {
            bottomTurret.draw(canvas);
        }
        if(leftTurret.isActive()) {
            leftTurret.draw(canvas);
        }
        if(topTurret.isActive()) {
            topTurret.draw(canvas);
        }
        if(rightTurret.isActive()) {
            rightTurret.draw(canvas);
        }
    }
}

