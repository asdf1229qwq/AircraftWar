package edu.hitsz.basic;

import edu.hitsz.aircraft.EliteEnemy;
import edu.hitsz.application.Main;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AbstractFlyingObjectTest {
    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }


    @Test
    void crash() {
        EliteEnemy eliteEnemy1 = new EliteEnemy(
                100,
                200,
                5,
                5,
                60
        );
        EliteEnemy eliteEnemy2 = new EliteEnemy(
                100,
                200,
                5,
                5,
                60
        );
        EliteEnemy eliteEnemy3 = new EliteEnemy(
                400,
                400,
                5,
                5,
                60
        );
        assertTrue(eliteEnemy1.crash(eliteEnemy2));
        assertFalse(eliteEnemy1.crash(eliteEnemy3));
    }
    @Test
    void notValid() {
        EliteEnemy eliteEnemy1 = new EliteEnemy(
                100,
                200,
                5,
                5,
                60
        );
        assertEquals(false, eliteEnemy1.notValid());
        eliteEnemy1.vanish();
        assertEquals(true, eliteEnemy1.notValid());
    }
}