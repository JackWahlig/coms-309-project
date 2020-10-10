package com.example.crossfire;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * A game object and the main projectile in the game. These are created and fired by turrets. They move in a straight line and either
 * go off screen, run into other turrets and inflict damage, run into other turret's shields and are destroyed, or run into powerups and destroy them for
 * the user to gain.
 */
public class Bullet implements GameObject {
    /**
     * Default value of the radius of a bullet
     */
    private static final int BULLET_RADIUS = 10;
    /**
     * Default value of the amount of damage a bullet inflicts
     */
    private static final int DEFUALT_DAMAGE = 10;

    /**
     * Bullet hitbox
     */
    private Rect bulletHitBox;
    /**
     * Turret which fired the bullet
     */
    private Turret shooter;
    /**
     * Color value of the bullet
     */
    private int color;
    /**
     * Velocity of the bullet
     */
    private int velocity;
    /**
     * Direction the bullet is travelling
     */
    private int direction;
    /**
     * Damage value of the bullet
     */
    private int damage;
    /**
     * X and Y position coordinates of the bullet
     */
    private float x, y;
    /**
     * Active state of the bullet
     */
    private boolean isActive;

    /**
     * Bullet constructor. Creates a bullet by setting its x and y positions on the game screen, the direction it was fired, its velocity,
     * its color, and the turret who shot the bullet.
     * @param x X position in the gamepanel
     * @param y Y position in the gamepanel
     * @param direction Direction the bullet has been fired/will move in
     * @param velocity Velocity that the bullet is travelling at
     * @param color Color of the bullet
     * @param shooter Turret which fired the bullet
     */
    public Bullet(float x, float y, int direction, int velocity, int color, Turret shooter) {
        this.x = x;
        this.y = y;
        this.shooter = shooter;
        this.damage = DEFUALT_DAMAGE;
        this.direction = direction;
        this.velocity = velocity; //Constant value for now
        this.color = color;
        this.bulletHitBox = new Rect((int)(x - BULLET_RADIUS), (int)(y - BULLET_RADIUS), (int)(x + BULLET_RADIUS), (int)(y + BULLET_RADIUS));
        this.isActive = true;
    }

    /**
     * Returns whether or not the bullet is currently active/moving
     * @return True if the bullet is active, False otherwise
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Sets a bullet to be either active or inactive
     * @param isActive If true, the bullet will be active and will be drawn and move on screen. If false,
     *                 the bullet will be inactive and not drawn or move.
     */
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * Sets the amount of damage the bullet inflicts
     * @param damage The new integer value of the bullet's damage
     */
    public void setDamage(int damage) {
        this.damage = damage;
    }

    /**
     * Returns the integer amount of damage the bullet inflicts when colliding with another game object
     * @return The integer amount of damage the bullet inflicts
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Returns the hitbox of the bullet for hit detection
     * @return The Rect hitbox of the bullet
     */
    public Rect getBulletHitBox() {
        return bulletHitBox;
    }

    /**
     * Returns the turret which fired the bullet
     * @return The turret which fired the bullet
     */
    public Turret getShooter() {
        return shooter;
    }

    /**
     * Sets the turret who fired the bullet
     * @param shooter The turret who fired the bullet
     */
    public void setShooter(Turret shooter) {
        this.shooter = shooter;
    }

    /**
     * Sets the color of the bullet to a given integer value
     * @param color The integer value of the color of the bullet
     */
    public void setColor(int color) {
        this.color = color;
    }

    /**
     * Draws the bullet on the gamepanel
     * @param canvas The canvas of the gamepanel for the bullet to be drawn on
     */
    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawCircle(x, y, BULLET_RADIUS, paint);
    }

    /**
     * Updates the bullet's current position every frame based on its direction and velocity
     * @param frameCount The frame of the game thread
     */
    @Override
    public void update(int frameCount) {
        this.x += this.velocity * Math.cos(Math.toRadians(direction));
        this.y -= this.velocity * Math.sin(Math.toRadians(direction));

        this.bulletHitBox.set((int)(x - BULLET_RADIUS), (int)(y - BULLET_RADIUS), (int)(x + BULLET_RADIUS), (int)(y + BULLET_RADIUS));
    }
}
