package edu.hitsz.DAO;

import java.util.List;

public interface PlayerDAO {
    public List<Player> getAll();
    public void loadFile(int mode);
    public void saveFile(int mode);
    public void update(Player player);
    public void delete(Player player);
    public void delete(int pos);
    public void showScoreBoard();
}
