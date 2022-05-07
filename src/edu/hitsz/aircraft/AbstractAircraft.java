package edu.hitsz.aircraft;

import edu.hitsz.application.game.Game;
import edu.hitsz.basic.AbstractFlyingObject;

import java.util.List;

/**
 * 所有种类飞机的抽象父类：
 * 敌机（BOSS, ELITE, MOB），英雄飞机
 *
 * @author hitsz
 */
public abstract class AbstractAircraft extends AbstractFlyingObject {
    /**
     * 生命值
     */
    protected int maxHp;
    protected int hp;
    protected int power = 0;
    protected int direction = 0;
    protected int shootNum = 0;

    public AbstractAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY);
        this.hp = hp;
        this.maxHp = hp;
    }

    public void decreaseHp(int decrease){
        hp -= decrease;
        if(hp <= 0){
            hp = 0;
            vanish();
        }
    }

    public int getHp() {
        return hp;
    }

    public int getDirection() { return direction; }

    public int getShootNum() { return shootNum; }

    public int getSpeedX() {
        return speedX;
    }

    public int getSpeedY() {
        return speedY;
    }

    public int getPower() {
        return power;
    }

    public abstract void update(Game game);

    public abstract void fallProp(List props);
}


