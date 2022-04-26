package Swing;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class EndPanel {
    private JPanel endPanel;
    private JTable scoreTable;
    private JScrollPane tableScrollPane;
    private JButton deleteButton;
    private File file = new File("src/edu/hitsz/DAO/Player.txt");
    private int maxListNum = 20;

    public EndPanel() {
        String[] columnName = {"rank", "name", "score", "date"};
        String[][] tableData = new String[maxListNum][4];

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
            int rk = 0;
            while((line = br.readLine()) != null) {
                if(rk >= maxListNum) {
                    break;
                }
                attr = line.split(" ");
                tableData[rk][0] = String.valueOf(rk + 1);
                tableData[rk][1] = attr[0];
                tableData[rk][2] = attr[1];
                tableData[rk][3] = attr[2];
                rk++;
            }
            br.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
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
                int row = scoreTable.getSelectedRow();
                System.out.println(row);
                if(row != -1) {
                    model.removeRow(row);
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
}
