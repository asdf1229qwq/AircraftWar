package edu.hitsz.application;

import edu.hitsz.aircraft.HeroAircraft;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class BulletThread extends Thread{

    HeroAircraft heroAircraft;

    public BulletThread(HeroAircraft heroAircraft) {
        this.heroAircraft = heroAircraft;
    }

    @Override
    public void run() {
        heroAircraft.increaseBullet(2);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
        heroAircraft.increaseBullet(-2);
    }



}
