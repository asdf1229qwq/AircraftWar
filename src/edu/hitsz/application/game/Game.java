package edu.hitsz.application.game;

import edu.hitsz.aircraft.*;
import edu.hitsz.application.*;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.enemyfactory.BossEnemyFactory;
import edu.hitsz.enemyfactory.EliteEnemyFactory;
import edu.hitsz.enemyfactory.MobEnemyFactory;
import edu.hitsz.observerPattern.BombPublisher;
import edu.hitsz.prop.*;
import edu.hitsz.shootstrategy.ScatteredShoot;
import edu.hitsz.shootstrategy.ShootContext;
import edu.hitsz.shootstrategy.StraightShoot;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

/**
 * 游戏主面板，游戏启动
 *
 * @author hitsz
 */
public abstract class Game extends JPanel {

    public int backGroundTop = 0;

    /**
     * Scheduled 线程池，用于任务调度
     */
    public final ScheduledExecutorService executorService;
    /**
     * 音乐线程
     */
    public final MusicThread musicThread = new MusicThread("src/videos/bgm.wav");
    public MusicThread bossMusicThread = new MusicThread("src/videos/bgm_boss.wav");
    public static boolean musicState = false;
    /**
     * 时间间隔(ms)，控制刷新频率
     */
    public int timeInterval = 40;

    public final HeroAircraft heroAircraft;
    public final List<AbstractAircraft> enemyAircrafts;
    public final List<BaseBullet> heroBullets;
    public final List<BaseBullet> enemyBullets;
    public final List<AbstractProp> props;

    public int enemyMaxNumber = 5;
    public int propMaxNumber = 3;

    public int bossScoreThreshold = 300;
    public boolean bossExistence = false;
    public boolean bossGenerate = true;
    public int bossHP = 300;
    public int mobEnemyHP = 30;
    public int eliteEnemyHP = 60;
    public int bossHPIncrease = 0;

    public double eliteEnemyProbability = 0.25;

    public boolean gameOverFlag = false;
    public int score = 0;
    public int lastScore = 0;
    public int time = 0;
    /**
     * 周期（ms)
     * 指示子弹的发射、敌机的产生频率
     */
    public int cycleDuration = 600;
    public int cycleTime = 0;

    public int getScore() {
        return this.score;
    }
    public void increaseScore(int num) {
        this.score += num;
    }
    public Game() {
        heroAircraft = HeroAircraft.getHeroAircraft(
                Main.WINDOW_WIDTH / 2,
                Main.WINDOW_HEIGHT - ImageManager.HERO_IMAGE.getHeight() ,
                0, 0, 1500);

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

        // 定时任务：绘制、对象产生、碰撞判定、击毁及结束判定、音乐

        if(musicState) {
//            musicThread.reverseMusic();
            musicThread.start();
            musicThread.Loop(true);
        }


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
//                    Random random = new Random();
                    AbstractAircraft aircraft;
                    double rand = Math.random();
                    if(rand < eliteEnemyProbability) {
                        aircraft = new EliteEnemyFactory().createEnemy(
                                (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.ELITE_ENEMY_IMAGE.getWidth()))*1,
                                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.2)*1,
                                0,
                                4,
                                eliteEnemyHP
                        );
                        enemyAircrafts.add(aircraft);
                    }
                    else {
                        aircraft = new MobEnemyFactory().createEnemy(
                                (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth()))*1,
                                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.2)*1,
                                0,
                                5,
                                mobEnemyHP);
                        enemyAircrafts.add(aircraft);
                    }
                }

                if(bossGenerate && score > 0 && score - lastScore >= bossScoreThreshold && !bossExistence) {
                    if(musicState) {
                        bossMusicThread = new MusicThread("src/videos/bgm_boss.wav");
                        bossMusicThread.start();
                        bossMusicThread.Loop(true);
                    }
                    lastScore = score;
                    bossExistence = true;
                    AbstractAircraft aircraft = new BossEnemyFactory().createEnemy(
                            (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.BOSS_ENEMY_IMAGE.getWidth()))*1,
                            (int) (Math.random() * Main.WINDOW_HEIGHT * 0.2)*1,
                            2,
                            0,
                            bossHP
                    );
                    bossHP += bossHPIncrease;
                    enemyAircrafts.add(aircraft);
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

            // 增加难度
            increaseDifficulty();

            //每个时刻重绘界面
            repaint();

            if(!bossExistence) {
                bossMusicThread.pause();
//                bossMusicThread.Loop(false);
            }

            // 游戏结束检查
            if (heroAircraft.getHp() <= 0) {
                // 游戏结束
                executorService.shutdown();
                gameOverFlag = true;
                new MusicThread("src/videos/game_over.wav").start();
                System.out.println("Game Over!");
//                mainPanel.setVisible(false);
                this.setVisible(false);
                synchronized (Main.MAIN_LOCK){
                    // 选定难度，通知主线程结束等待
                    Main.MAIN_LOCK.notify();
                }
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

    public boolean timeCountAndNewCycleJudge() {
        cycleTime += timeInterval;
        if (cycleTime >= cycleDuration && cycleTime - timeInterval < cycleTime) {
            // 跨越到新的周期
            cycleTime %= cycleDuration;
            return true;
        } else {
            return false;
        }
    }

    public void shootAction() {
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

    public void bulletsMoveAction() {
        for (BaseBullet bullet : heroBullets) {
            bullet.forward();
        }
        for (BaseBullet bullet : enemyBullets) {
            bullet.forward();
        }
    }

    public void aircraftsMoveAction() {
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            enemyAircraft.forward();
        }
    }

    public void propsMoveAction() {
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
    public void crashCheckAction() {
        // 敌机子弹攻击英雄
        for(BaseBullet bullet : enemyBullets) {
            if(bullet.notValid()) {
                continue;
            }
            if(heroAircraft.crash(bullet)) {
                if(musicState) {
                    new MusicThread("src/videos/bullet.wav").start();
                }
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
                    if(musicState) {
                        new MusicThread("src/videos/bullet_hit.wav").start();
                    }
                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();
                    if (enemyAircraft.notValid()) {
                        // 获得分数，产生道具补给
                        if(enemyAircraft instanceof EliteEnemy) {
                            enemyAircraft.update(this);
                            if(props.size() < propMaxNumber) {
                                enemyAircraft.fallProp(props);
                            }
                        }
                        else if(enemyAircraft instanceof BossEnemy) {
                            enemyAircraft.update(this);
                            if(props.size() < propMaxNumber) {
                                enemyAircraft.fallProp(props);
                            }
                            bossExistence = false;
                            bossMusicThread.pause();
                        }
                        else {
                            enemyAircraft.update(this);
                        }
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
                if(musicState) {
                    new MusicThread("src/videos/get_supply.wav").start();
                }
                // 加血道具
                if(prop instanceof Blood) {
                    heroAircraft.increaseHp(20);
                    prop.vanish();
                    System.out.println("HP");
                }
                // 炸弹道具
                else if(prop instanceof Bomb) {
                    if(musicState) {
                        new MusicThread("src/videos/bomb_explosion.wav").start();
                    }
                    BombPublisher bombPublisher = new BombPublisher();
                    for(AbstractAircraft enemyAircraft : enemyAircrafts) {
                        if (enemyAircraft.notValid()) {
                            // 已被其他子弹击毁的敌机，不再检测
                            // 避免多个子弹重复击毁同一敌机的判定
                            continue;
                        }
                        if(enemyAircraft instanceof BossEnemy) {
                            continue;
                        }
                        bombPublisher.addEnemyAircraft(enemyAircraft);
                        System.out.println(props.size());
                    }
                    bombPublisher.notifyAll(this);
                    for(AbstractAircraft enemyAircraft : enemyAircrafts) {
                        if (enemyAircraft.notValid()) {
                            // 已被其他子弹击毁的敌机，不再检测
                            // 避免多个子弹重复击毁同一敌机的判定
                            continue;
                        }
                        if(enemyAircraft instanceof BossEnemy) {
                            continue;
                        }
                        bombPublisher.removeEnemyAircraft(enemyAircraft);
                    }
                    for(BaseBullet bullet : enemyBullets) {
                        if (bullet.notValid()) {
                            continue;
                        }
                        bullet.vanish();
                    }
                    prop.vanish();
                    System.out.println("Bomb");
                }
                // 火力道具
                else if(prop instanceof Bullet) {
                    new BulletThread(heroAircraft).start();
                    prop.vanish();
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
    public void postProcessAction() {
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

    public void paintImageWithPositionRevised(Graphics g, List<? extends AbstractFlyingObject> objects) {
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

    public void paintScoreAndLife(Graphics g) {
        int x = 10;
        int y = 25;
        g.setColor(new Color(16711680));
        g.setFont(new Font("SansSerif", Font.BOLD, 22));
        g.drawString("SCORE:" + this.score, x, y);
        y = y + 20;
        g.drawString("LIFE:" + this.heroAircraft.getHp(), x, y);
    }

    abstract public void setDifficulty();

    abstract public void increaseDifficulty();
}
