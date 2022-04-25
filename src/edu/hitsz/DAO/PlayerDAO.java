package edu.hitsz.DAO;

import edu.hitsz.application.Game;

import java.util.List;

public interface PlayerDAO {
    public List<Player> getAll();
    public void update(Player player);
//    public void delete(Player player);
    public void showScoreBoard();
}
