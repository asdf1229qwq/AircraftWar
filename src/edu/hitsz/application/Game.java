package edu.hitsz.application;

import edu.hitsz.DAO.Player;
import edu.hitsz.DAO.PlayerDAO;
import edu.hitsz.DAO.PlayerDAOlmpl;
import edu.hitsz.aircraft.*;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.enemyfactory.BossEnemyFactory;
import edu.hitsz.enemyfactory.EliteEnemyFactory;
import edu.hitsz.enemyfactory.MobEnemyFactory;
import edu.hitsz.prop.*;
import edu.hitsz.propfactory.BloodFactory;
import edu.hitsz.propfactory.BombFactory;
import edu.hitsz.propfactory.BulletFactory;
import edu.hitsz.shootstrategy.ScatteredShoot;
import edu.hitsz.shootstrategy.ShootContext;
import edu.hitsz.shootstrategy.StraightShoot;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

/**
 * 游戏主面板，游戏启动
 *
 * @author hitsz
 */
public class Game extends JPanel {

    private int backGroundTop = 0;

    /**
     * Scheduled 线程池，用于任务调度
     */
    private final ScheduledExecutorService executorService;

    /**
     * 时间间隔(ms)，控制刷新频率
     */
    private int timeInterval = 40;

    private final HeroAircraft heroAircraft;
    private final List<AbstractAircraft> enemyAircrafts;
    private final List<BaseBullet> heroBullets;
    private final List<BaseBullet> enemyBullets;
    private final List<AbstractProp> props;

    private int enemyMaxNumber = 5;
    private int propMaxNumber = 5;

    private int bossScoreThreshold = 300;
    private boolean bossExistence = false;

    private boolean gameOverFlag = false;
    private int score = 0;
    private int time = 0;
    /**
     * 周期（ms)
     * 指示子弹的发射、敌机的产生频率
     */
    private int cycleDuration = 600;
    private int cycleTime = 0;


    public Game() {
        heroAircraft = HeroAircraft.getHeroAircraft(
                Main.WINDOW_WIDTH / 2,
                Main.WINDOW_HEIGHT - ImageManager.HERO_IMAGE.getHeight() ,
                0, 0, 500);

        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        props = new LinkedList<>();

        //Scheduled 线程池，用于定时任务调度
        ThreadFactory gameThread = new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("game thread");
                return t;
            }
        };
        executorService = new ScheduledThreadPoolExecutor(1, gameThread);

        //启动英雄机鼠标监听
        new HeroController(this, heroAircraft);

    }

    /**
     * 游戏启动入口，执行游戏逻辑
     */
    public void action() {

        // 定时任务：绘制、对象产生、碰撞判定、击毁及结束判定
        Runnable task = () -> {

            time += timeInterval;

            // 周期性执行（控制频率）
            if (timeCountAndNewCycleJudge()) {
                System.out.println(time);
                // 新敌机产生
                if (enemyAircrafts.size() < enemyMaxNumber) {
                    // 普通敌机产生的概率为80%，精英敌机产生的概率为20%
                    // 普通敌机血量为30，不能发射子弹
                    // 精英敌机血量为60，能发射子弹
                    Random random = new Random();
                    switch(random.nextInt(5)) {
                        case 0: case 1: case 2: case 3: {
                            enemyAircrafts.add(new MobEnemyFactory().createEnemy(
                                    (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth()))*1,
                                    (int) (Math.random() * Main.WINDOW_HEIGHT * 0.2)*1,
                                    0,
                                    5,
                                    30
                            ));
                            break;
                        }
                        case 4: {
                            enemyAircrafts.add(new EliteEnemyFactory().createEnemy(
                                    (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.ELITE_ENEMY_IMAGE.getWidth()))*1,
                                    (int) (Math.random() * Main.WINDOW_HEIGHT * 0.2)*1,
                                    0,
                                    4,
                                    60
                            ));
                            break;
                        }
                        default: {
                            break;
                        }
                    }
//                    System.out.println("BOSS = " + bossExistence);
                    if(score > 0 && score % bossScoreThreshold == 0 && bossExistence == false) {
                        bossExistence = true;
                        enemyAircrafts.add(new BossEnemyFactory().createEnemy(
                                (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.BOSS_ENEMY_IMAGE.getWidth()))*1,
                                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.2)*1,
                                2,
                                0,
                                300
                        ));
                    }
                }
                // 飞机射出子弹
                shootAction();
            }

            // 子弹移动
            bulletsMoveAction();

            // 飞机移动
            aircraftsMoveAction();

            // 道具移动
            propsMoveAction();

            // 撞击检测
            crashCheckAction();

            // 后处理
            postProcessAction();

            //每个时刻重绘界面
            repaint();

            // 游戏结束检查
            if (heroAircraft.getHp() <= 0) {
                // 游戏结束
                executorService.shutdown();
                gameOverFlag = true;
                System.out.println("Game Over!");
                PlayerDAO playerDAO = new PlayerDAOlmpl();
                /****/
                String date = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss").format(new Date());
                playerDAO.update(new Player("tom", score, date));
                playerDAO.showScoreBoard();
            }

        };

        /**
         * 以固定延迟时间进行执行
         * 本次任务执行完成后，需要延迟设定的延迟时间，才会执行新的任务
         */
        executorService.scheduleWithFixedDelay(task, timeInterval, timeInterval, TimeUnit.MILLISECONDS);

    }

    //***********************
    //      Action 各部分
    //***********************

    private boolean timeCountAndNewCycleJudge() {
        cycleTime += timeInterval;
        if (cycleTime >= cycleDuration && cycleTime - timeInterval < cycleTime) {
            // 跨越到新的周期
            cycleTime %= cycleDuration;
            return true;
        } else {
            return false;
        }
    }

    private void shootAction() {
        // 敌机射击
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            if(enemyAircraft instanceof EliteEnemy) {
                enemyBullets.addAll(new ShootContext(new StraightShoot()).executeStrategy(enemyAircraft));
            }
            else if(enemyAircraft instanceof BossEnemy) {
                enemyBullets.addAll(new ShootContext(new ScatteredShoot()).executeStrategy(enemyAircraft));
            }
        }
        // 英雄射击
        if(heroAircraft.getShootNum() == 1) {
            heroBullets.addAll(new ShootContext(new StraightShoot()).executeStrategy(heroAircraft));
        }
        else {
            heroBullets.addAll(new ShootContext(new ScatteredShoot()).executeStrategy(heroAircraft));
        }
    }

    private void bulletsMoveAction() {
        for (BaseBullet bullet : heroBullets) {
            bullet.forward();
        }
        for (BaseBullet bullet : enemyBullets) {
            bullet.forward();
        }
    }

    private void aircraftsMoveAction() {
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            enemyAircraft.forward();
        }
    }

    private void propsMoveAction() {
        for (AbstractProp prop : props) {
            prop.forward();
        }
    }



    /**
     * 碰撞检测：
     * 1. 敌机攻击英雄
     * 2. 英雄攻击/撞击敌机
     * 3. 英雄获得补给
     */
    private void crashCheckAction() {
        // 敌机子弹攻击英雄
        for(BaseBullet bullet : enemyBullets) {
            if(bullet.notValid()) {
                continue;
            }
            if(heroAircraft.crash(bullet)) {
                heroAircraft.decreaseHp(bullet.getPower());
                bullet.vanish();
            }
        }
        // 英雄子弹攻击敌机
        for (BaseBullet bullet : heroBullets) {
            if (bullet.notValid()) {
                continue;
            }
            for (AbstractAircraft enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) {
                    // 已被其他子弹击毁的敌机，不再检测
                    // 避免多个子弹重复击毁同一敌机的判定
                    continue;
                }
                if (enemyAircraft.crash(bullet)) {
                    // 敌机撞击到英雄机子弹
                    // 敌机损失一定生命值
                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();
                    if (enemyAircraft.notValid()) {
                        // 获得分数，产生道具补给
                        if(enemyAircraft instanceof EliteEnemy) {
                            score += 20;
                            /**
                             * 击落精英机有50%概率生成道具
                             * 同屏最多3个道具
                             */
                            Random random = new Random();
                            if(props.size() < propMaxNumber) {
                                    switch (random.nextInt(2)) {
                                    case 0: {
                                        switch (random.nextInt(3)) {
                                            case 0: {
                                                props.add(new BloodFactory().createProp((int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.PROP_BLOOD_IMAGE.getWidth())) * 1,
                                                        (int) ((2 + Math.random()) * Main.WINDOW_HEIGHT * 0.2) * 1,
                                                        0,
                                                        2));
                                                break;
                                            }
                                            case 1: {
                                                props.add(new BombFactory().createProp((int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.PROP_BLOOD_IMAGE.getWidth())) * 1,
                                                        (int) ((2 + Math.random()) * Main.WINDOW_HEIGHT * 0.2) * 1,
                                                        0,
                                                        2));
                                                break;
                                            }
                                            case 2: {
                                                props.add(new BulletFactory().createProp((int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.PROP_BLOOD_IMAGE.getWidth())) * 1,
                                                        (int) ((2 + Math.random()) * Main.WINDOW_HEIGHT * 0.2) * 1,
                                                        0,
                                                        2));
                                                break;
                                            }
                                            default: {
                                                break;
                                            }
                                        }
                                        break;
                                    }
                                    default: {
                                        break;
                                    }
                                }
                            }

                        }
                        else if(enemyAircraft instanceof BossEnemy) {
                            score += 50;
                            bossExistence = false;
                        }
                        else {
                            score += 10;
                        }
                        System.out.println(score);
                    }
                }
                // 英雄机 与 敌机 相撞，均损毁
                if (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft)) {
                    enemyAircraft.vanish();
                    heroAircraft.decreaseHp(Integer.MAX_VALUE);
                }
            }
        }

        // 我方获得道具，道具生效
        for(AbstractProp prop : props) {
            if(prop.notValid()) {
                continue;
            }
            if(prop.crash(heroAircraft)) {
                // 英雄机碰撞到道具

                // 加血道具
                if(prop instanceof Blood) {
                    heroAircraft.increaseHp(20);
                    prop.vanish();
                    System.out.println("HP");
                }
                // 炸弹道具
                else if(prop instanceof Bomb) {
                    bossExistence = false;
                    for(AbstractAircraft enemyAircraft : enemyAircrafts) {
                        enemyAircraft.vanish();
                    }
                    for(BaseBullet bullet : enemyBullets) {
                        bullet.vanish();
                    }
                    prop.vanish();
                    System.out.println("Bomb");
                }
                // 火力道具
                else if(prop instanceof Bullet) {
                    if(heroAircraft.getShootNum() < 5) {
                        heroAircraft.increaseBullet(1);
                        prop.vanish();
                    }
                    else {
                        prop.vanish();
                    }
                    System.out.println("Bullet");
                }
            }
        }

    }

    /**
     * 后处理：
     * 1. 删除无效的子弹
     * 2. 删除无效的敌机
     * 3. 检查英雄机生存
     * <p>
     * 无效的原因可能是撞击或者飞出边界
     */
    private void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        props.removeIf(AbstractFlyingObject::notValid);
    }


    //***********************
    //      Paint 各部分
    //***********************

    /**
     * 重写paint方法
     * 通过重复调用paint方法，实现游戏动画
     *
     * @param  g
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // 绘制背景,图片滚动
        g.drawImage(ImageManager.BACKGROUND_IMAGE, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
        g.drawImage(ImageManager.BACKGROUND_IMAGE, 0, this.backGroundTop, null);
        this.backGroundTop += 1;
        if (this.backGroundTop == Main.WINDOW_HEIGHT) {
            this.backGroundTop = 0;
        }

        // 先绘制子弹，后绘制飞机
        // 这样子弹显示在飞机的下层
        paintImageWithPositionRevised(g, enemyBullets);
        paintImageWithPositionRevised(g, heroBullets);
        paintImageWithPositionRevised(g, enemyAircrafts);
        paintImageWithPositionRevised(g, props);

        g.drawImage(ImageManager.HERO_IMAGE, heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2,
                heroAircraft.getLocationY() - ImageManager.HERO_IMAGE.getHeight() / 2, null);

        //绘制得分和生命值
        paintScoreAndLife(g);

    }

    private void paintImageWithPositionRevised(Graphics g, List<? extends AbstractFlyingObject> objects) {
        if (objects.size() == 0) {
            return;
        }

        for (AbstractFlyingObject object : objects) {
            BufferedImage image = object.getImage();
            assert image != null : objects.getClass().getName() + " has no image! ";
            g.drawImage(image, object.getLocationX() - image.getWidth() / 2,
                    object.getLocationY() - image.getHeight() / 2, null);
        }
    }

    private void paintScoreAndLife(Graphics g) {
        int x = 10;
        int y = 25;
        g.setColor(new Color(16711680));
        g.setFont(new Font("SansSerif", Font.BOLD, 22));
        g.drawString("SCORE:" + this.score, x, y);
        y = y + 20;
        g.drawString("LIFE:" + this.heroAircraft.getHp(), x, y);
    }


}
