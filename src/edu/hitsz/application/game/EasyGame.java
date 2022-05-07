package edu.hitsz.application.game;

public class EasyGame extends Game{
    public EasyGame() {
        super();
        System.out.println("EasyGame");
        setDifficulty();
    }
    @Override
    public void setDifficulty() {
        bossGenerate = false;

        cycleDuration = 600;

        enemyMaxNumber = 5;

        mobEnemyHP = 30;
        eliteEnemyHP = 60;
        bossHPIncrease = 0;

        eliteEnemyProbability = 0.25;
    }

    public void increaseDifficulty() {
    }
}
