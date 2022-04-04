package edu.hitsz.propfactory;

import edu.hitsz.prop.AbstractProp;
import edu.hitsz.prop.Bullet;

/**
 * @author ding
 */
public class BulletFactory implements PropFactory{
    @Override
    public AbstractProp createProp(int locationX, int locationY, int speedX, int speedY) {
        return new Bullet(locationX, locationY, speedX, speedY);
    }
}
