package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EliteEnemyTest {
    EliteEnemy eliteEnemy = new EliteEnemy(
            100,
            200,
            5,
            5,
            60
    );


    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void forward() {
        eliteEnemy.setLocation(Main.WINDOW_WIDTH - 3, 3);
        eliteEnemy.forward();
        assertEquals(Main.WINDOW_WIDTH + 2, eliteEnemy.getLocationX());
        eliteEnemy.forward();
        assertEquals(Main.WINDOW_WIDTH - 3, eliteEnemy.getLocationX());
        assertEquals(-5, eliteEnemy.getSpeedX());
        assertEquals(13, eliteEnemy.getLocationY());
    }

    @Test
    void setLocation() {
        assertEquals(100, eliteEnemy.getLocationX());
        assertEquals(200, eliteEnemy.getLocationY());
        eliteEnemy.setLocation(3, 0);
        assertEquals(3, eliteEnemy.getLocationX());
        assertEquals(0, eliteEnemy.getLocationY());
    }
}