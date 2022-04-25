package edu.hitsz.DAO;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class PlayerDAOlmpl implements PlayerDAO{
    private List<Player> PlayerList = new LinkedList<>();
    private File file = new File("src/edu/hitsz/DAO/Player.txt");
//    private File file = new File("Player.txt");
    public PlayerDAOlmpl(){}
//    public PlayerDAOlmpl(){
//        file = new File("Player.txt");
//        if(!file.exists()){
//            //先得到文件的上级目录，并创建上级目录，在创建文件
//            file.getParentFile();
//            try {
//                //创建文件
//                file.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    };
    @Override
    public List<Player> getAll() {
        return PlayerList;
    }
    @Override
    public void update(Player player) {
        FileReader fr = null;
        try {
            fr = new FileReader(this.file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
//            throw new RuntimeException(e);
        }
        BufferedReader br = new BufferedReader(fr);

        String line = "";
        String[] attr;
        if (file.length() != 0) {
//            System.out.println(file.length());

            try {
                while ((line = br.readLine()) != null) {
                    attr = line.split(" ");
                    PlayerList.add(new Player(attr[0], Integer.parseInt(attr[1]), attr[2]));
                }
                br.close();
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
//            throw new RuntimeException(e);
            }
        }

        int rk = 0;
        for(Player player1 : PlayerList) {
            if(player.getScore() > player1.getScore()) {
                break;
            }
            rk++;
        }
        if(rk == PlayerList.size() + 1) {
            PlayerList.add(player);
        }
        else {
            PlayerList.add(rk, player);
        }
        FileWriter fw = null;
        try {
            fw = new FileWriter(this.file);
        } catch (IOException e) {
            e.printStackTrace();
//            throw new RuntimeException(e);
        }
        BufferedWriter bw = new BufferedWriter(fw);
        try {
            for(Player player1 : PlayerList) {
                String playerStr = player1.getName() + " " + player1.getScore() + " " + player1.getDate();
//                System.out.println(playerStr);
                bw.write(playerStr);
                bw.newLine();
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
//            throw new RuntimeException(e);
        }
    }

    @Override
    public void showScoreBoard() {
        FileReader fr = null;
        try {
            fr = new FileReader(this.file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
//            throw new RuntimeException(e);
        }
        BufferedReader br = new BufferedReader(fr);

        String line = "";
        String[] attr;

        try {
            int rk = 1;
            while((line = br.readLine()) != null) {
                attr = line.split(" ");
                System.out.println("第"+rk+"名 " + attr[0] + " " + attr[1] + " " + attr[2]);
                rk++;
            }
            br.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public void delete(Player player) {
//        PlayerList.add(player);
//    }
}
