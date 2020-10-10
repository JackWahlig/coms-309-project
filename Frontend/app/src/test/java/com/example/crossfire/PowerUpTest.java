package com.example.crossfire;

import android.graphics.Color;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.*;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.*;

public class PowerUpTest {


    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Test
    public void powerUpAddsHealthTest() {
        int color = Color.rgb(225,225,153);
        PowerUp powerUp = new AddHealth(0,0,0,color);
        Bullet bullet = Mockito.mock(Bullet.class);
        Turret t = new Turret(0,0,0);
        t.setHP(Turret.MAX_HP-50);
        bullet.setShooter(t);
        Mockito.when(bullet.getDamage()).thenReturn(10);
        powerUp.setHP(powerUp.getHP()-bullet.getDamage(),t);

        Assert.assertEquals(500,t.getHP());
    }

    @Test
    public void powerUpInactiveAfterUseTest() {
        int color = Color.rgb(225,225,153);
        PowerUp powerUp = new AddHealth(0,0,0,color);
        Bullet bullet = Mockito.mock(Bullet.class);
        Turret t = new Turret(0,0,0);
        t.setHP(Turret.MAX_HP-50);
        bullet.setShooter(t);
        Mockito.when(bullet.getDamage()).thenReturn(10);
        powerUp.setHP(powerUp.getHP()-bullet.getDamage(),t);

        Assert.assertEquals(false,powerUp.isActive());
    }

    @Test
    public void bulletDamagePowerupTest() {
        int color = Color.rgb(225,225,153);
        PowerUp powerUp = new AddHealth(0,0,0,color);
        Bullet bullet = Mockito.mock(Bullet.class);
        Turret t = new Turret(0,0,0);
        bullet.setShooter(t);
        Mockito.when(bullet.getDamage()).thenReturn(10);
        powerUp.setHP(powerUp.getHP()-bullet.getDamage(),t);

        Assert.assertEquals(0, powerUp.getHP());
        Assert.assertTrue(t.isActive());

    }
}