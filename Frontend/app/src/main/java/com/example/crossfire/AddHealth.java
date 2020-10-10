package com.example.crossfire;


//This PowerUp restores 10% of a users max HP.
public class AddHealth extends PowerUp {

    /**
     * Creates a PowerUp that adds 10% of a turrets maximum health back.
     * @param xCenter Center X coordinate.
     * @param yCenter Center Y coordinate
     * @param offset numerical offset. (Currently unused)
     * @param color Power Up color.
     */
    public AddHealth(float xCenter, float yCenter, int offset, int color) {
        super(xCenter,yCenter,offset,color);
    }

    /**
     * Function called on the death of the Power Up.
     * @param turret Turret to add health to.
     */
    @Override
    public void onKill(Turret turret){
        giveHealth(turret);
    }

    /**
     * Performs the health addition on the turret passed as a parameter
     * @param turret Turret to add health to.
     */
    public void giveHealth(Turret turret){
        turret.setHP(turret.getHP()+(Turret.MAX_HP/10));
    }

}
