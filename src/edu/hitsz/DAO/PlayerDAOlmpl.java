package edu.hitsz.DAO;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class PlayerDAOlmpl implements PlayerDAO{
    private List<Player> PlayerList = new LinkedList<>();
    private File file = new File("src/edu/hitsz/DAO/Player.txt");
//    private File file = new File("Player.txt");
    public PlayerDAOlmpl(){
        loadFile();
    }
    @Override
    public List<Player> getAll() {
        return PlayerList;
    }

    @Override
    public void delete(Player player) {
        PlayerList.remove(player);
    }
    @Override
    public void delete(int pos) {
        PlayerList.remove(pos);
    }
    @Override
    public void loadFile() {
        FileReader fr = null;
        try {
            fr = new FileReader(this.file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br = new BufferedReader(fr);

        String line = "";
        String[] attr;
        if (file.length() != 0) {
            try {
                while ((line = br.readLine()) != null) {
                    attr = line.split(" ");
                    PlayerList.add(new Player(attr[0], Integer.parseInt(attr[1]), attr[2]));
                }
                br.close();
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void saveFile() {
        FileWriter fw = null;
        try {
            fw = new FileWriter(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedWriter bw = new BufferedWriter(fw);

        // 清空
        try {
            fw.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            for(Player player1 : PlayerList) {
                String playerStr = player1.getName() + " " + player1.getScore() + " " + player1.getDate();
                bw.write(playerStr);
                bw.newLine();
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void update(Player player) {
        int rk = 0;
        for(Player player1 : PlayerList) {
            if(player.getScore() > player1.getScore()) {
                break;
            }
            rk++;
        }
        if(rk == PlayerList.size() + 1) {
            PlayerList.add(player);
        } else {
            PlayerList.add(rk, player);
        }
    }

    @Override
    public void showScoreBoard() {
        int rk = 1;
        for(Player player1 : PlayerList) {
            System.out.println("第" + rk + "名 " + player1.getName() + " " + player1.getScore() + " " + player1.getDate());
            rk++;
        }
    }
}
