package edu.hitsz.aircraft;

import edu.hitsz.application.game.Game;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.propfactory.BloodFactory;
import edu.hitsz.propfactory.BombFactory;
import edu.hitsz.propfactory.BulletFactory;

import java.util.List;
import java.util.Random;

/**
 * 精英敌机
 * 可射击
 *
 * @author ding
 */
public class EliteEnemy extends AbstractAircraft {
    /**
     * 攻击方式
     * 子弹一次发射数量
     * 子弹伤害
     * 子弹射击方向 (向上发射：1，向下发射：-1)
     */
    public EliteEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        shootNum = 1;
        power = 25;
        direction = 1;
    }

    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= Main.WINDOW_HEIGHT ) {
            vanish();
        }
    }

    @Override
    public void update(Game game) {
        game.increaseScore(20);
        this.vanish();
    }


    @Override
    public void fallProp(List props) {
        /**
         * 击落精英机有90%概率生成道具
         * 同屏最多3个道具
         */
        double rand = Math.random();
        if(rand < 0.9) {
            switch (new Random().nextInt(3)) {
                case 0:
                    props.add(new BloodFactory().createProp((int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.PROP_BLOOD_IMAGE.getWidth())) * 1,
                            (int) ((2 + Math.random()) * Main.WINDOW_HEIGHT * 0.2) * 1,
                            0,
                            2));
                    break;

                case 1:
                    props.add(new BombFactory().createProp((int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.PROP_BLOOD_IMAGE.getWidth())) * 1,
                            (int) ((2 + Math.random()) * Main.WINDOW_HEIGHT * 0.2) * 1,
                            0,
                            2));
                    break;
                case 2:
                    props.add(new BulletFactory().createProp((int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.PROP_BLOOD_IMAGE.getWidth())) * 1,
                            (int) ((2 + Math.random()) * Main.WINDOW_HEIGHT * 0.2) * 1,
                            0,
                            2));
                    break;
                default:
                    break;
            }
        }
    }
}
