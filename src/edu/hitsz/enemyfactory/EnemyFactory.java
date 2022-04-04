package edu.hitsz.enemyfactory;

import edu.hitsz.aircraft.AbstractAircraft;

/**
 * @author ding
 */
public interface EnemyFactory {
    /**
     * 创建敌人的方法
     * @param locationX
     * @param locationY
     * @param speedX
     * @param speedY
     * @param hp
     * @return
     */
    AbstractAircraft createEnemy(int locationX, int locationY, int speedX, int speedY, int hp);
}
