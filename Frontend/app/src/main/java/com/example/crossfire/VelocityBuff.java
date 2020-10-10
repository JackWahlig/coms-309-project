package com.example.crossfire;

import java.util.Timer;

/**
 * Power Up that buffs the bullet speed of a turret.
 */
public class VelocityBuff extends PowerUp{

    private Timer timer;

    /**
     * Creates a PowerUp that doubles the bullet velocity.
     * @param xCenter Center X coordinate.
     * @param yCenter Center Y coordinate
     * @param offset numerical offset. (Currently unused)
     * @param color Power Up color.
     */
    public VelocityBuff(float xCenter, float yCenter, int offset, int color) {
        super(xCenter,yCenter,offset,color);

        timer = new Timer();
    }

    /**
     * Function called on the death of the Power Up.
     * @param turret Turret to apply power to.
     */
    @Override
    public void onKill(Turret turret) {
        System.out.println("Velocity buffed.");
        turret.setBulletVelocity(turret.DEFAULT_BULLET_VELOCITY * 2);
        timer.schedule(new ResetTurretEvent(turret), 10000);
    }

}
