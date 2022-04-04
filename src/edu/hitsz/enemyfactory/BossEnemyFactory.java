package edu.hitsz.enemyfactory;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.BossEnemy;

/**
 * @author ding
 */
public class BossEnemyFactory implements EnemyFactory{
    @Override
    public AbstractAircraft createEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        return new BossEnemy(locationX, locationY, speedX, speedY, hp);
    }
}
