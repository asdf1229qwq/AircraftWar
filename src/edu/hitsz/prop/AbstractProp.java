package edu.hitsz.prop;

import edu.hitsz.application.Main;
import edu.hitsz.basic.AbstractFlyingObject;

public abstract class AbstractProp extends AbstractFlyingObject {
    public AbstractProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴出界
        if (locationY >= Main.WINDOW_HEIGHT ) {
            // 向下飞行出界
            vanish();
        }
    }
}
