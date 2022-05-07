package edu.hitsz.aircraft;

import edu.hitsz.application.game.Game;

import java.util.List;

/**
 * 英雄飞机，游戏玩家操控
 * @author hitsz
 */
public class HeroAircraft extends AbstractAircraft {

    /**
     * 攻击方式
     * 子弹一次发射数量
     * 子弹伤害
     * 子弹射击方向 (向上发射：1，向下发射：-1)
     */
    /**
     * @param locationX 英雄机位置x坐标
     * @param locationY 英雄机位置y坐标
     * @param speedX 英雄机射出的子弹的基准速度（英雄机无特定速度）
     * @param speedY 英雄机射出的子弹的基准速度（英雄机无特定速度）
     * @param hp    初始生命值
     */
    private volatile static HeroAircraft heroAircraft;
    private HeroAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        shootNum = 1;
        power = 30;
        direction = -1;
    }

    @Override
    public void update(Game game) {
        return;
    }

    public static HeroAircraft getHeroAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        if(heroAircraft == null) {
            synchronized (HeroAircraft.class) {
                if(heroAircraft == null) {
                    heroAircraft = new HeroAircraft(locationX, locationY, speedX, speedY, hp);
                }
            }
        }
        return heroAircraft;
    }


    @Override
    public void forward() {
        // 英雄机由鼠标控制，不通过forward函数移动
    }

    /**
     * 血量道具
     */
    public void increaseHp(int increase) {
        hp += increase;
        if(hp > maxHp) {
            hp = maxHp;
        }
    }
    /**
     * 火力道具
     */
    public void increaseBullet(int bulletNumber) {shootNum += bulletNumber;}

    public void fallProp(List props) {
        return;
    }
}
