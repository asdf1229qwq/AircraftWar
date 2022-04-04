package edu.hitsz.propfactory;

import edu.hitsz.prop.AbstractProp;
import edu.hitsz.prop.Bomb;

/**
 * @author ding
 */
public class BombFactory implements PropFactory{
    @Override
    public AbstractProp createProp(int locationX, int locationY, int speedX, int speedY) {
        return new Bomb(locationX, locationY, speedX, speedY);
    }
}