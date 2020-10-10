package com.example.crossfire;

import android.app.Activity;
import android.util.DisplayMetrics;

import java.util.Timer;
import java.util.TimerTask;

public class PowerUpEvent extends TimerTask {

    /**
     * Width of the screen.
     */
    private int screenWidth;

    /**
     * Height of the screen.
     */
    private int screenHeight;
    /**
     * Constructs a new power up event that will add a new power up to the game panel when it is passed through a timer object.
     * @param screenWidth Integer screen width of the client's device.
     * @param screenHeight Integer screen height of the client's device.
     */
    public PowerUpEvent(int screenWidth, int screenHeight){

        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

    }

    /**
     * Adds a new power up to a game panel when called.
     */
    @Override
    public void run() {
        GamePanel.addNewPowerUp(screenWidth,screenHeight);
    }
}
