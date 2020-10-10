package com.example.crossfire;

import android.graphics.Color;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

public class GameUnitTest {

    //Tests if health updates when turret takes damage that does not destroy it
    @Test
    public void turretHealthTest1() {
        Turret t = new Turret(0, 0, 0);
        Bullet bulletInterface = Mockito.mock(Bullet.class);
        Mockito.when(bulletInterface.getDamage()).thenReturn(10);
        t.setHP(t.getHP() - bulletInterface.getDamage());

        Assert.assertEquals(t.MAX_HP - 10, t.getHP());
        Assert.assertTrue(t.isActive());
    }

    //Tests if health ipdates and isActive updates when turret's health reaches 0
    @Test
    public void turretHealthTest2() {
        Turret t = new Turret(0, 0, 0);
        Bullet bulletInterface = Mockito.mock(Bullet.class);
        Mockito.when(bulletInterface.getDamage()).thenReturn(t.getHP());
        t.setHP(t.getHP() - bulletInterface.getDamage());

        Assert.assertEquals(0, t.getHP());
        Assert.assertFalse(t.isActive());
    }

    @Test
    //Tests if turret gains health, it cannot go above the maximum
    public void turretHealthTest3() {
        Turret t = new Turret(0, 0, 0);
        Bullet bulletInterface = Mockito.mock(Bullet.class);
        Mockito.when(bulletInterface.getDamage()).thenReturn(10);
        t.setHP(t.getHP() + bulletInterface.getDamage());

        Assert.assertEquals(t.MAX_HP, t.getHP());
        Assert.assertTrue(t.isActive());
    }
}
