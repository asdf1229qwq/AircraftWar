package edu.hitsz.aircraft;

import edu.hitsz.application.game.Game;
import edu.hitsz.application.Main;

import java.util.List;

/**
 * 普通敌机
 * 不可射击
 *
 * @author hitsz
 */
public class MobEnemy extends AbstractAircraft {

    public MobEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        shootNum = 0;
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
        game.increaseScore(10);
        this.vanish();
    }

    @Override
    public void fallProp(List props) {
        return;
    }
}
