package edu.hitsz.propfactory;

import edu.hitsz.prop.AbstractProp;
import edu.hitsz.prop.Blood;

/**
 * @author ding
 */
public class BloodFactory implements PropFactory{
    @Override
    public AbstractProp createProp(int locationX, int locationY, int speedX, int speedY) {
        return new Blood(locationX, locationY, speedX, speedY);
    }
}