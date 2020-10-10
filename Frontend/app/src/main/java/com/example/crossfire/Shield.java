package com.example.crossfire;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Shield is a game object and each Turret has an instance of a shield. Shield update() and draw()
 * are called from the Turret class
 */
public class Shield implements GameObject {

    /**
     * Default value of teh height of the shield
     */
    private final static int DEFAULT_HEIGHT = 10;
    /**
     * Default value of the width of the shield
     */
    private final static int DEFAULT_WIDTH = 90;

    /**
     * Height value of the shield
     */
    private int height;
    /**
     * Width value of the shield
     */
    private int width;
    /**
     * Shield's hitbox
     */
    private Rect shieldHitBox;
    /**
     * X coordinate of the center of the shield
     */
    private float xCenter;
    /**
     * Y coordinate of the center of the shield
     */
    private float yCenter;
    /**
     * The color value of the shield
     */
    private int color;

    /**
     * Sets the width of the shield
     * @param width New width of the shield
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Returns the integer value of the shield's width
     * @return The width of the shield
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets the height of the shield
     * @param height New height of the shield
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Returns the integer value of the shield's height
     * @return The height of the shield
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets the X coordinate of the center of the shield
     * @param xCenter The new X coordinate of the center of the shield
     */
    public void setxCenter(float xCenter) {
        this.xCenter = xCenter;
    }

    /**
     * Returns the X coordinate of the center of the shield
     * @return The X coordinate of the center of the shield
     */
    public float getxCenter() {
        return xCenter;
    }

    /**
     * Sets the Y coordinate of the center of the shield
     * @param yCenter The new Y coordinate of the center of the shield
     */
    public void setyCenter(float yCenter) {
        this.yCenter = yCenter;
    }

    /**
     * Returns the Y coordinate of the center of the shield
     * @return The Y coordinate of the center of the shield
     */
    public float getyCenter() {
        return yCenter;
    }

    /**
     * Returns the Rect hitbox of the shield
     * @return The hitbox of the shield
     */
    public Rect getShieldHitBox() {
        return shieldHitBox;
    }

    /**
     * Sets the hitbox of the shield given the left, right, top, and bottom values of its position
     * @param left Coordinate of the left edge of the shield's hitbox
     * @param top Coordinate of the top edge of the shield's hitbox
     * @param right Coordinate of the right edge of the shield's hitbox
     * @param bottom Coordinate of the bottom edge of the shield's hitbox
     */
    public void setShieldHitBox(int left, int top, int right, int bottom) {
        shieldHitBox.set(left, top, right, bottom);
    }

    /**
     * Constructs a shield given its X and Y starting coordinates. Uses default values for other fields.
     * @param xCenter The X coordinate of the center of the shield
     * @param yCenter The Y coordinate of the center of the shield
     */
    public Shield(float xCenter, float yCenter) {
        this.xCenter = xCenter;
        this.yCenter = yCenter;
        this.height = DEFAULT_HEIGHT;
        this.width = DEFAULT_WIDTH;
        this.color = Color.CYAN;
        shieldHitBox = new Rect((int)(xCenter - width / 2), (int)(yCenter - height / 2), (int)(xCenter + width / 2), (int)(yCenter + height / 2));
    }

    /**
     * Draws the shield on the gamepanel canvas
     * @param canvas The canvas of the gamepanel for the object to be drawn on
     */
    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(shieldHitBox, paint);
    }

    /**
     * Updates the position of the shield baseon on the new xCenter and yCenter values sent by the left joystick of the gameactivity
     * @param frameCount The frame that the object is updating on
     */
    @Override
    public void update(int frameCount) {
        //Update shield hitbox
        shieldHitBox.set((int)(xCenter - getWidth() / 2), (int)(yCenter - getHeight() / 2), (int)(xCenter + getWidth() / 2), (int)(yCenter + getHeight() / 2));
    }
}
