package edu.hitsz.DAO;

import edu.hitsz.swing.ChangePanel;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class PlayerDAOlmpl implements PlayerDAO{
    private List<Player> PlayerList = new LinkedList<>();
    private File file1 = new File("src/edu/hitsz/DAO/EasyGame.txt");
    private File file2 = new File("src/edu/hitsz/DAO/MediumGame.txt");
    private File file3 = new File("src/edu/hitsz/DAO/HardGame.txt");
    private File file;
    public PlayerDAOlmpl(){
        loadFile(ChangePanel.mode);
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
    public void loadFile(int mode) {
        FileReader fr = null;
        try {
            if(mode == 1) {
                file = file1;
//                fr = new FileReader(this.file1);
            }
            else if(mode == 2) {
                file = file2;
//                fr = new FileReader(this.file2);
            }
            else {
                file = file3;
//                fr = new FileReader(this.file3);
            }
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
    public void saveFile(int mode) {

        FileWriter fw = null;
        try {
            if(mode == 1) {
                file = file1;
//                fr = new FileReader(this.file1);
            }
            else if(mode == 2) {
                file = file2;
//                fr = new FileReader(this.file2);
            }
            else {
                file = file3;
//                fr = new FileReader(this.file3);
            }
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
