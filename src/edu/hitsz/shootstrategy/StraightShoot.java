package edu.hitsz.shootstrategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;
import edu.hitsz.prop.Bullet;

import java.util.LinkedList;
import java.util.List;

/**
 * @author asdf1229
 */
public class StraightShoot implements ShootStrategy{
    @Override
    public List<BaseBullet> shoot(AbstractAircraft aircraft) {
        List<BaseBullet> res = new LinkedList<>();
        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY() + aircraft.getDirection()*2;
        int speedX = 0;
        int speedY = aircraft.getSpeedY() + aircraft.getDirection()*5;
        int shootNum = aircraft.getShootNum();
        int power = aircraft.getPower();
        for(int i=0; i<shootNum; i++){
            // 子弹发射位置相对飞机位置向前偏移
            // 多个子弹横向分散
            BaseBullet baseBullet;
            if(aircraft instanceof HeroAircraft) {
                baseBullet = new HeroBullet(x + (i * 2 - shootNum + 1) * 10, y, speedX, speedY, power);
            }
            else {
                baseBullet = new EnemyBullet(x + (i * 2 - shootNum + 1) * 10, y, speedX, speedY, power);
            }
            res.add(baseBullet);
        }
        return res;
    }
}
