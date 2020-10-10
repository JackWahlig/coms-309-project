package com.example.crossfire;

import android.graphics.Rect;

public class MaxHealth extends PowerUp implements GameObject {


    public MaxHealth(float xCenter, float yCenter, int offset, int color) {

//        this.HP = MAX_POWERUP_HP;
//        this.xCenter = xCenter;
//        this.yCenter = yCenter;
//        this.offset = offset;
//        this.turretHitBox = new Rect((int)(xCenter - POWERUP_HITBOX_WIDTH), (int)(yCenter - POWERUP_HITBOX_WIDTH), (int)(xCenter + POWERUP_HITBOX_WIDTH) , (int)(yCenter + POWERUP_HITBOX_WIDTH));
//        this.color = color;
        super(xCenter,yCenter,offset,color);

    }

    @Override
    public void onKill(Turret turret){
        giveHealth(turret);
    }

    public void giveHealth(Turret turret){
        turret.setHP(Turret.MAX_HP);
    }

}
