package edu.hitsz.swing;

import edu.hitsz.DAO.Player;
import edu.hitsz.DAO.PlayerDAO;
import edu.hitsz.DAO.PlayerDAOlmpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.LinkedList;
import java.util.List;

import static javax.swing.JOptionPane.OK_CANCEL_OPTION;
import static javax.swing.JOptionPane.YES_NO_OPTION;

public class EndPanel {
    private JPanel endPanel;
    private JTable scoreTable;
    private JScrollPane tableScrollPane;
    private JButton deleteButton;
    private int maxListNum = 20;
    private List<Player> PlayerList = new LinkedList<>();

    public EndPanel() {
        String[] columnName = {"rank", "name", "score", "date"};
        String[][] tableData = new String[maxListNum][4];



        PlayerDAO playerDAO = new PlayerDAOlmpl();
        PlayerList = playerDAO.getAll();

        int rk = 0;
        for(Player player1 : PlayerList) {
//            System.out.println("~~~~");
            if(rk >= 20) {
                break;
            }
            tableData[rk][0] = String.valueOf(rk + 1);
            tableData[rk][1] = player1.getName();
            tableData[rk][2] = String.valueOf(player1.getScore());
            tableData[rk][3] = player1.getDate();
            rk++;
        }

        DefaultTableModel model = new DefaultTableModel(tableData, columnName) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        scoreTable.setModel(model);
        tableScrollPane.setViewportView(scoreTable);


        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int opt = JOptionPane.showConfirmDialog(null, "是否确定删除？",
                        "选择",YES_NO_OPTION); //返回值为0或1
                int row = scoreTable.getSelectedRow();
                System.out.println(row);
                System.out.println(opt);
                if(opt == 0) {
                    if(row != -1) {
                        model.removeRow(row);
                        playerDAO.delete(row);
                        playerDAO.saveFile();
                    }
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("EndPanel");
        frame.setContentPane(new EndPanel().endPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public JPanel getMainPanel() {
        return endPanel;
    }
}
