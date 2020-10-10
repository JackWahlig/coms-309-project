package com.example.crossfire;

import java.util.TimerTask;

public class ResetTurretEvent extends TimerTask {

    private Turret turret;

    public ResetTurretEvent(Turret turret) {
        this.turret = turret;
    }

    @Override
    public void run() {
        turret.setFireRate(Turret.DEFAULT_FIRE_RATE);
        turret.setBulletVelocity(Turret.DEFAULT_BULLET_VELOCITY);
        turret.setInvulnerable(false);
    }
}
