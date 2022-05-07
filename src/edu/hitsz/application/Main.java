package edu.hitsz.application;

import edu.hitsz.DAO.Player;
import edu.hitsz.DAO.PlayerDAO;
import edu.hitsz.DAO.PlayerDAOlmpl;
import edu.hitsz.application.game.Game;
import edu.hitsz.swing.ChangePanel;
import edu.hitsz.swing.EndPanel;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import static edu.hitsz.swing.ChangePanel.mode;

/**
 * 程序入口
 * @author hitsz
 */
public class Main {

    public static final int WINDOW_WIDTH = 512;
    public static final int WINDOW_HEIGHT = 768;

    public static final Object MAIN_LOCK = new Object();

    public static void main(String[] args) {
        System.out.println("Hello Aircraft War");

        // 获得屏幕的分辨率，初始化 Frame
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        JFrame frame = new JFrame("Aircraft War");
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setResizable(false);
        //设置窗口的大小和位置,居中放置
        frame.setBounds(((int) screenSize.getWidth() - WINDOW_WIDTH) / 2, 0,
                WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //第一个界面
        ChangePanel newFrame = new ChangePanel();
        JPanel newMainPanel = newFrame.getMainPanel();
        frame.setContentPane(newMainPanel);
        frame.setVisible(true);


        synchronized (MAIN_LOCK) {
            while (newMainPanel.isVisible()) {
                // 主线程等待菜单面板关闭
                try {
                    MAIN_LOCK.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }



        frame.remove(newMainPanel);

        Game game = newFrame.getGame();

        // 游戏界面

        frame.setContentPane(game);
        frame.setVisible(true);
        game.action();

        synchronized (MAIN_LOCK) {
            while (game.isVisible()) {
                // 主线程等待游戏关闭
                try {
                    MAIN_LOCK.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }


        String name = JOptionPane.showInputDialog(game,"请输入用户名","输入用户名",1);
        System.out.println(name);



        frame.remove(game);

        int score = game.getScore();
        String date = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss").format(new Date());

        PlayerDAO playerDAO = new PlayerDAOlmpl();
        Player nowPlayer = new Player(name, score, date);
        playerDAO.update(nowPlayer);
        playerDAO.saveFile(ChangePanel.mode);

//        System.out.println(score);
//        System.out.println(date);

        EndPanel newFrame1 = new EndPanel();
        JPanel newMainPanel1 = newFrame1.getMainPanel();
        frame.setContentPane(newMainPanel1);
        frame.setVisible(true);

//        Game game = new Game();
//        frame.add(game);
//        frame.setVisible(true);
//        game.action();
    }
}
