package edu.hitsz.DAO;

import edu.hitsz.application.Game;

import java.util.List;

public interface PlayerDAO {
    public List<Player> getAll();
    public void loadFile();
    public void saveFile();
    public void update(Player player);
    public void delete(Player player);
    public void delete(int pos);
    public void showScoreBoard();
}
