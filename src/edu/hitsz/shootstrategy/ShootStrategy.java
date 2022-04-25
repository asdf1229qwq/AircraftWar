package edu.hitsz.shootstrategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.prop.Bullet;

import java.util.List;

/**
 * @author asdf1229
 */
public interface ShootStrategy {
    public List<BaseBullet> shoot(AbstractAircraft aircraft);
}
