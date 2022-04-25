package edu.hitsz.shootstrategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;

import java.util.List;

/**
 * @author asdf1229
 */
public class ShootContext {
    private ShootStrategy strategy;
    public ShootContext(ShootStrategy strategy) {
        this.strategy = strategy;
    }
    public List<BaseBullet> executeStrategy(AbstractAircraft aircraft) {
        return this.strategy.shoot(aircraft);
    }
}
