package com.example.crossfire;

import java.util.Timer;

/**
 * Power Up which doubles the fire rate of a turret for 5 seconds.
 */
public class FireRateBuff extends PowerUp {

    private Timer timer;

    /**
     * Creates a PowerUp that doubles the fire rate of a turret.
     * @param xCenter Center X coordinate.
     * @param yCenter Center Y coordinate
     * @param offset numerical offset. (Currently unused)
     * @param color Power Up color.
     */
    public FireRateBuff(float xCenter, float yCenter, int offset, int color) {
        super(xCenter,yCenter,offset,color);

        timer = new Timer();
    }

    /**
     * Function called on the death of the Power Up.
     * @param turret Turret to apply power to.
     */
    @Override
    public void onKill(Turret turret) {
        System.out.println("Fire rate buffed.");
        turret.setFireRate(turret.DEFAULT_FIRE_RATE / 2);
        timer.schedule(new ResetTurretEvent(turret), 10000);
    }
}
