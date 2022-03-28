package edu.hitsz.prop_factory;

import edu.hitsz.prop.AbstractProp;

public interface PropFactory {
    AbstractProp createProp(int locationX, int locationY, int speedX, int speedY);
}
