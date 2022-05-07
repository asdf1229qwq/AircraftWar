package edu.hitsz.observerPattern;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.application.game.Game;

import java.util.LinkedList;
import java.util.List;

public class BombPublisher {

    private List<AbstractAircraft> enemyAircrafts = new LinkedList<>();

    public void addEnemyAircraft(AbstractAircraft enemyAircraft) {
        enemyAircrafts.add(enemyAircraft);
    }

    public void removeEnemyAircraft(AbstractAircraft enemyAircraft) {
        enemyAircrafts.remove(enemyAircraft);
    }

    public void notifyAll(Game game) {
        for(AbstractAircraft enemyAircraft: enemyAircrafts) {
            enemyAircraft.update(game);
        }
    }
}
