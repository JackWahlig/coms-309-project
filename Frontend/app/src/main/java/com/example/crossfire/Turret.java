package com.example.crossfire;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * The turret which a user will control when playing the game. There will be 4 turrets at the start of the game, each with the same amount
 * of health. Turret fire bullets continuously and will be destoryed when their health reaches 0.
 */
public class Turret implements GameObject {

    /**
     * Default value of the maximum amount of healt a turret has
     */
    public static final int MAX_HP = 500;
    /**
     * Default value of the velocity of bullets fired by the turret
     */
    public static final int DEFAULT_BULLET_VELOCITY = 20;
    /**
     * Default value of the rate of fire of the turret
     */
    public static final int DEFAULT_FIRE_RATE = 10; //Frames per bullet fire
    /**
     * Default value of the maximum number of bullets a turret can have on screen at a time
     */
    public static final int MAX_BULLETS = 30;
    /**
     * Default value of the length of a turret's gun
     */
    public static final int GUN_LENGTH = 30;
    /**
     * Default value of the width of a turret's gun
     */
    public static final int GUN_WIDTH = 10;
    /**
     * Default value of the width of the turret's hitbox
     */
    public static final int HITBOX_WIDTH = 100;
    /**
     * Default value of the distance the Turret's shield is from the turret itself
     */
    private static final int SHIELD_DISTANCE = 180;

    /**
     * X coordinate of the center of the turret
     */
    private float xCenter;
    /**
     * Y coordinate of the center of the turret
     */
    private float yCenter;
    /**
     * Hitbox of the turret
     */
    private Rect turretHitBox;
    /**
     * Value of the turret's color
     */
    private int color;
    /**
     * Value of the turre't bullet's color
     */
    private int bulletColor;
    /**
     * The direction the turret's gun faces
     */
    private int gunDirection;
    /**
     * The direction the turret's shield faces
     */
    private int shieldDirection;
    /**
     * The angle offset of the turret depending on what side of the screen it is on
     */
    private int offset;
    /**
     * The X coordinate of the end of the gun where Bullets are fired from
     */
    private float gunEndX;
    /**
     * The Y coordinate of teh end of the gun where Bullets are fired from
     */
    private float gunEndY;
    /**
     * The array of bullets the turret will fire
     */
    private Bullet[] bullets; //30 bullets on screen at max
    /**
     * The shield protecting the turret
     */
    private Shield shield;
    /**
     * The number of bullets the Turret has fired out of the number of bullets it controls
     */
    private int bulletCount;
    /**
     * The total number of bullets the turret has fired
     */
    private int numBulletsFired;
    /**
     * The amount of time in milliseconds that the turret has been alive for
     */
    private long timeAlive;
    /**
     * The health value of the turret
     */
    private int HP;
    /**
     * The fire rate of the turret
     */
    private int fireRate;
    /**
     * The velocity at which the turret fire bullets
     */
    private int bulletVelocity;
    /**
     * Flag telling if the turret is still alive or not
     */
    private boolean isActive;

    /**
     * Invulnerability flag.
     */
    private boolean isInvulnerable;

    /**
     * Constructs a turret given its X and Y coordinates and the offset it has depending on what side of the screen it is on
     * @param xCenter The X coordinate of the center of the turret
     * @param yCenter The Y coordinate of the center of the turret
     * @param offset The angle offset of the turret
     */
    public Turret(float xCenter, float yCenter, int offset) {
        this.xCenter = xCenter;
        this.yCenter = yCenter;
        HP = MAX_HP;
        fireRate = DEFAULT_FIRE_RATE;
        bulletVelocity = DEFAULT_BULLET_VELOCITY;
        turretHitBox = new Rect((int)(xCenter - HITBOX_WIDTH), (int)(yCenter - HITBOX_WIDTH), (int)(xCenter + HITBOX_WIDTH) , (int)(yCenter + HITBOX_WIDTH));
        color = Color.rgb(128, 128, 128); //By default, Turret's are grey
        bulletColor = Color.rgb(0, 0, 0); //By default, bullet's are black
        gunDirection = offset;
        shieldDirection = offset;
        this.offset = offset;
        bullets = new Bullet[MAX_BULLETS];
        numBulletsFired = 0;
        shield = new Shield(xCenter, yCenter);
        timeAlive = System.currentTimeMillis();
        isActive = true;

        //Sets bullets to null essentially
        for(int i = 0; i < MAX_BULLETS; i++) {
            bullets[i] = new Bullet(0, 0, 0, 0, 0,this); //Bullet that does not move to start
            bullets[i].setActive(false);
        }
    }

    /**
     * Returns whether or not the turret is still alive
     * @return True if the turret is alive, False otherwise
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Sets the turret to be alive or not
     * @param isActive True if the turret is being set to alive and false otherwise
     */
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * Returns the angle offset of the turret
     * @return The angle offset of the turret
     */
    public int getOffset() {
        return offset;
    }

    /**
     * Returns the instance of the turret's shield
     * @return The shield of the turret
     */
    public Shield getShield() {
        return shield;
    }

    /**
     * Sets the turrets health to a given value. Checks to see that the turret's health does not go over the maximum value.
     * If the new value is less than or equal to 0, the turret is set to inactive and is killed.
     * @param HP the new HP value of the turret
     */
    public void setHP(int HP) {
        if(!isInvulnerable) {
            if (HP >= MAX_HP) {
                this.HP = MAX_HP;
            } else if (HP <= 0) {
                //Turret is set inactive if its HP reaches 0
                this.HP = 0;
                setActive(false);
                //Turret and shield hitboxes needs to be removed along with turret
                turretHitBox.set(0, 0, 0, 0);
                shield.setShieldHitBox(0, 0, 0, 0);
                //Get time alive
                long timeOfDeath = System.currentTimeMillis();
                timeAlive = timeOfDeath - timeAlive;
            } else {
                this.HP = HP;
            }
        }
    }

    /**
     * Returns the HP value of the turret
     * @return The HP of the turret
     */
    public int getHP() {
        return this.HP;
    }

    /**
     * Sets the turret's color value
     * @param color The new color of the turret
     */
    public void setColor(int color) {
        this.color = color;
    }

    /**
     * Sets the color of the turret's bullets
     * @param color The color of the turret's bullets
     */
    public void setBulletColor(int color) { this.bulletColor = color; }

    /**
     * Sets the direction the turret's gun faces
     * @param gunDirection The direction of the turret's gun
     */
    public void setGunDirection(int gunDirection) {
        this.gunDirection = gunDirection;
    }

    /**
     * Returns the direction the turret's gun is facing
     * @return The direction of the turret's gun
     */
    public int getGunDirection() {
        return gunDirection;
    }

    /**
     * Sets the direction the turret's shield faces
     * @param shieldDirection The direction of the turret's gun
     */
    public void setShieldDirection(int shieldDirection) {
        this.shieldDirection = shieldDirection;
    }

    /**
     * Returns the direction the turret's shield is facing
     * @return The direction of the turret's shield
     */
    public int getShieldDirection() {
        return shieldDirection;
    }

    /**
     * Returns the Rect hitbox of the turret
     * @return The turret's hitbox
     */
    public Rect getTurretHitBox() {
        return turretHitBox;
    }

    /**
     * Returns the array of the bullets the turret fires
     * @return The bullets the turret fires
     */
    public Bullet[] getBullets() {
        return bullets;
    }

    /**
     * Returns the total number of bullets the turret has fired so far
     * @return The number of bullets the turret has fired
     */
    public int getNumBulletsFired() {
        return numBulletsFired;
    }

    /**
     * Returns the amount of time in milliseconds that the turret has been alive in a game for
     * @return The amount of time the turret has been alive
     */
    public long getTimeAlive() {
        return timeAlive;
    }

    /**
     * Returns The X coordinate of the end of the gun
     * @return The X coordinate of the end of the gun
     */
    public float getGunEndX() {
        return gunEndX;
    }

    /**
     * Returns The Y coordinate of the end of the gun
     * @return The Y coordinate of the end of the gun
     */
    public float getGunEnyY() {
        return gunEndY;
    }

    /**
     * Returns the fire rate at which the turret fires bullets
     * @return The fire rate of the turret
     */
    public int getFireRate() {
        return fireRate;
    }

    /**
     * Sets the fire rate at which the turret fire bullets
     * @param fireRate The fire rate of the turret
     */
    public void setFireRate(int fireRate) {
        this.fireRate = fireRate;
    }

    /**
     * Returns the velocity of bullets fired by the turret
     * @return The velocity of the turret's bullets
     */
    public int getBulletVelocity() {
        return bulletVelocity;
    }

    /**
     * Sets the velocity at which the turret fires bullets
     * @param bulletVelocity The velocity of the bullets fired by the turret
     */
    public void setBulletVelocity(int bulletVelocity) {
        this.bulletVelocity = bulletVelocity;
    }

    /**
     * Sets the invulnerability of this turret
     * @param b boolean to set isInvulnerable to.
     */
    public void setInvulnerable(boolean b){ isInvulnerable = b; }

    /**
     * Draws the turret on the gamepanel in the game
     * @param canvas The canvas of the gamepanel for the object to be drawn on
     */
    @Override
    public void draw(Canvas canvas) {
        //Draw circle
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawCircle(xCenter, yCenter, HITBOX_WIDTH, paint);

        //Draw gun
        canvas.save();
        canvas.rotate(90 - gunDirection, xCenter, yCenter);
        canvas.drawRect(xCenter - GUN_WIDTH, yCenter - HITBOX_WIDTH - GUN_LENGTH, xCenter + GUN_WIDTH, yCenter, paint);
        canvas.restore();

        //Draw shield
        canvas.save();
        //Right and left hitboxes are changed, so rotations need to change here as well
        if(offset == 0 || offset == 180) {
            canvas.rotate(180 - shieldDirection, shield.getxCenter(), shield.getyCenter());
            shield.draw(canvas);
        }
        else {
            canvas.rotate(90 - shieldDirection, shield.getxCenter(), shield.getyCenter());
            shield.draw(canvas);
        }
        canvas.restore();

        for(int i = 0; i < MAX_BULLETS; i++) {
            //Only draw active bullets
            if(bullets[i].isActive()) {
                bullets[i].draw(canvas);
            }
        }
    }

    /**
     * Updates the position og the turret and fires bullets depending on the fire rate of the turret. Updates the bullet array
     * of the turret accordingly
     * @param frameCount The frame that the object is updating on
     */
    @Override
    public void update(int frameCount) {
        //Find where the tip of the gun should be (can be off screen)
        gunEndX = (float)(xCenter + ((HITBOX_WIDTH + (GUN_LENGTH - 5)) * (Math.cos(Math.toRadians(gunDirection)))));
        gunEndY = (float) (yCenter - ((HITBOX_WIDTH + (GUN_LENGTH - 5)) * (Math.sin(Math.toRadians(gunDirection)))));

        shield.setxCenter((float)(xCenter + (SHIELD_DISTANCE) * (Math.cos(Math.toRadians(shieldDirection)))));
        shield.setyCenter((float)(yCenter - (SHIELD_DISTANCE) * (Math.sin(Math.toRadians(shieldDirection)))));

        shield.update(frameCount);
        //Shield hitboxes for  right and left turrets should be rotated (needs fixing)
        if(offset == 0 || offset == 180) {
            shield.setShieldHitBox((int)(shield.getxCenter() - shield.getHeight() / 2), (int)(shield.getyCenter() - shield.getWidth() / 2),
                    (int)(shield.getxCenter() + shield.getHeight() / 2), (int)(shield.getyCenter() + shield.getWidth() / 2));
        }

        if(frameCount % fireRate == 0) {
            if(bulletCount == MAX_BULLETS) {
                bulletCount = 0;
            }
            bullets[bulletCount] = new Bullet(gunEndX, gunEndY, gunDirection, bulletVelocity, bulletColor,this);
            bulletCount++;
            numBulletsFired++;
        }
        //Update bullets
        for(int i = 0; i < MAX_BULLETS; i++) {
            //Only update active bullets
            if(bullets[i].isActive()) {
                bullets[i].update(frameCount);
            }
        }
    }
}
