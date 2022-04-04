package edu.hitsz.propfactory;

import edu.hitsz.prop.AbstractProp;

/**
 * @author ding
 */
public interface PropFactory {
    /**
     * 创建道具的方法
     * @param locationX
     * @param locationY
     * @param speedX
     * @param speedY
     * @return
     */
    AbstractProp createProp(int locationX, int locationY, int speedX, int speedY);
}
