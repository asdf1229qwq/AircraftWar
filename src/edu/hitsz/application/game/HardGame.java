package edu.hitsz.application.game;

public class HardGame extends Game{
    public HardGame() {
        super();
        System.out.println("HardGame");
        setDifficulty();
    }
    @Override
    public void setDifficulty() {
        bossGenerate = true;

        cycleDuration = 400;

        enemyMaxNumber = 8;

        mobEnemyHP = 60;
        eliteEnemyHP = 90;
        bossHPIncrease = 100;

        eliteEnemyProbability = 0.5;
    }
    static int count = 0;
    @Override
    public void increaseDifficulty() {
        count++;
        if(count % 100 == 0) {
            mobEnemyHP += 10;
            eliteEnemyHP += 10;
            System.out.println("普通敌机血量：" + mobEnemyHP + "，精英敌机血量：" + eliteEnemyHP);
        }
    }
}