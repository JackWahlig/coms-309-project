package com.example.crossfire;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Abstract class which all powerups must implement.
 */
abstract class PowerUp implements GameObject {

    /**
     * Maximum PowerUp health
     */
    public static final int MAX_POWERUP_HP = 1;

    /**
     * Width of the powerup hitbox.
     */
    public static final int POWERUP_HITBOX_WIDTH = 50;

    /**
     * This number serves to denote the maximum index of the list of powerups. Its only purpose is a use as a maximum bound for a random number generator,
     * for when the GamePanel class requires a random PowerUp.
     */
    public static final int NUM_POWERUPS = 5; //This MUST be updated when adding a new powerup


    /**
     * x coordinate of the center point
     */
    protected float xCenter;

    /**
     * y coordinate of the center point
     */
    protected float yCenter;

    /**
     * Rectangle object to represent the hitbox boundaries.
     */
    protected Rect hitBox;

    /**
     *
     */
    protected int color;

    /**
     *
     */
    protected int offset;

    /**
     *
     */
    protected int HP;

    /**
     *
     */
    protected boolean isActive;

    /**
     * Default constructor creates a powerup at (0,0) with the maximum powerup health.
     */
    public PowerUp(){
        this.HP = MAX_POWERUP_HP;
        this.xCenter = 0;
        this.yCenter = 0;
        this.offset = 0;
        this.hitBox = new Rect((int)(xCenter - POWERUP_HITBOX_WIDTH), (int)(yCenter - POWERUP_HITBOX_WIDTH), (int)(xCenter + POWERUP_HITBOX_WIDTH) , (int)(yCenter + POWERUP_HITBOX_WIDTH));
        this.color = 0;
    }

    /**
     * Constructs a PowerUp with center (xCenter,yCenter), offset, and color).
     * @param xCenter The X coordinate of the power up center.
     * @param yCenter The Y coordinate of the power up center.
     * @param offset The numerical offset of the position. (Currently not used.)
     * @param color Color of the power up when created.
     */
    public PowerUp(float xCenter, float yCenter, int offset, int color){
        this.HP = MAX_POWERUP_HP;
        this.xCenter = xCenter;
        this.yCenter = yCenter;
        this.offset = offset;
        this.hitBox = new Rect((int)(xCenter - POWERUP_HITBOX_WIDTH), (int)(yCenter - POWERUP_HITBOX_WIDTH), (int)(xCenter + POWERUP_HITBOX_WIDTH) , (int)(yCenter + POWERUP_HITBOX_WIDTH));
        this.color = color;
    }

    /**
     * Returns whether this power up is active or not.
     * @return True if power powerup is active, False otherwise.
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     *
     * @param isActive
     */
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public int getOffset() {
        return offset;
    }

    public int getColor() { return color; }

    public void setHP(int HP, Turret turret){
        if(HP >= MAX_POWERUP_HP) {
            this.HP = MAX_POWERUP_HP;
        }
        else if(HP <= 0) {
            this.HP = 0;
            setActive(false);
            onKill(turret);
            hitBox.set(0, 0, 0, 0);
        }
        else {
            this.HP = HP;
        }
    }

    public int getHP() {
        return this.HP;
    }

    public Rect getHitbox(){return this.hitBox;}

    //Sets turret's color
    public void setColor(int color) {
        this.color = color;
    }

    public void draw(Canvas canvas){

        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawCircle(xCenter, yCenter, POWERUP_HITBOX_WIDTH, paint);

    }

    public void update(int frameCount){
        //Blank until implemented into the game.
    }

    public void onKill(Turret killer){

    }
}
