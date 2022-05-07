package edu.hitsz.swing;

import edu.hitsz.application.game.EasyGame;
import edu.hitsz.application.game.Game;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.application.game.HardGame;
import edu.hitsz.application.game.MediumGame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;

public class ChangePanel {
    private JButton EasyButton;
    private JButton NormalButton;
    private JButton HardButton;
    private JPanel changePanel;
    private JLabel music;
    private JCheckBox MusicCheckBox;
    public static int mode;

    private Game game;

    private EndPanel endPanel;

    public Game getGame() {
        return this.game;
    }
    public EndPanel getEndPanel() {
        return endPanel;
    }

    public ChangePanel() {
        EasyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Pressed Easy Button!");
                try {
                    ImageManager.BACKGROUND_IMAGE = ImageIO.read(new FileInputStream("src/images/bg.jpg"));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                game = new EasyGame();
                mode = 1;
                changePanel.setVisible(false);
                synchronized (Main.MAIN_LOCK){
                    // 选定难度，通知主线程结束等待
                    Main.MAIN_LOCK.notify();
                }
            }
        });
        NormalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Pressed Normal Button!");
                try {
                    ImageManager.BACKGROUND_IMAGE = ImageIO.read(new FileInputStream("src/images/bg2.jpg"));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                game = new MediumGame();
                mode = 2;
                changePanel.setVisible(false);
                synchronized (Main.MAIN_LOCK){
                    // 选定难度，通知主线程结束等待
                    Main.MAIN_LOCK.notify();
                }
            }
        });
        HardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Pressed Hard Button!");
                try {
                    ImageManager.BACKGROUND_IMAGE = ImageIO.read(new FileInputStream("src/images/bg5.jpg"));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                game = new HardGame();
                mode = 3;
                changePanel.setVisible(false);
                synchronized (Main.MAIN_LOCK){
                    // 选定难度，通知主线程结束等待
                    Main.MAIN_LOCK.notify();
                }
            }
        });
        MusicCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Game.musicState = MusicCheckBox.isSelected();
                System.out.println("music:" + MusicCheckBox.isSelected());
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("MainPanel");
        frame.setContentPane(new ChangePanel().changePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public JPanel getMainPanel() {
        return changePanel;
    }
}
