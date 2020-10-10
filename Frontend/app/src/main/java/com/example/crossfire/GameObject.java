package com.example.crossfire;

import android.graphics.Canvas;

/**
 * An interface for objects in the game
 */
public interface GameObject {
    /**
     * Draws the game object in the gamepanel
     * @param canvas The canvas of the gamepanel for the object to be drawn on
     */
    public void draw(Canvas canvas);

    /**
     * Updates the current position and/or state of the game object
     * @param frameCount The frame that the object is updating on
     */
    public void update(int frameCount);
}
