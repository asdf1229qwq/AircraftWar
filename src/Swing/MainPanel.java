package Swing;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainPanel {
    private JButton EasyButton;
    private JButton NormalButton;
    private JButton HardButton;
    private JComboBox MusicComboBox;
    private JLabel music;
    private JPanel MainPanel;

    public MainPanel() {
        EasyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Pressed Easy Button!");
            }
        });
        NormalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Pressed Normal Button!");
            }
        });
        HardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Pressed Hard Button!");
            }
        });
        MusicComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(e);
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("MainPanel");
        frame.setContentPane(new MainPanel().MainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
