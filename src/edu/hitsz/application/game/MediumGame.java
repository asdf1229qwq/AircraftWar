package edu.hitsz.application.game;

public class MediumGame extends Game{
    public MediumGame() {
        super();
        System.out.println("MediumGame");
        setDifficulty();
    }
    @Override
    public void setDifficulty() {
        bossGenerate = true;

        cycleDuration = 500;

        enemyMaxNumber = 7;

        mobEnemyHP = 30;
        eliteEnemyHP = 90;
        bossHPIncrease = 50;

        eliteEnemyProbability = 0.4;
    }
    static int count = 0;
    @Override
    public void increaseDifficulty() {
        count++;
        if(count % 200 == 0) {
            mobEnemyHP += 10;
            eliteEnemyHP += 10;
            System.out.println("普通敌机血量：" + mobEnemyHP + "，精英敌机血量：" + eliteEnemyHP);
        }
    }
}
