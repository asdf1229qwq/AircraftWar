package edu.hitsz.aircraft;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HeroAircraftTest {

    HeroAircraft heroAircraft = HeroAircraft.getHeroAircraft(
            100,
            200,
            5,
            5,
            90
    );

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void increaseHp() {
        assertEquals(90, heroAircraft.getHp());
        heroAircraft.decreaseHp(25);
        assertEquals(65, heroAircraft.getHp());
        heroAircraft.increaseHp(50);
        assertEquals(90, heroAircraft.getHp());
    }

    @Test
    void increaseBullet() {
        assertEquals(1, heroAircraft.getShootNum());
        heroAircraft.increaseBullet(1);
        assertEquals(2, heroAircraft.getShootNum());
        heroAircraft.increaseBullet(5);
        assertEquals(7, heroAircraft.getShootNum());
    }
}