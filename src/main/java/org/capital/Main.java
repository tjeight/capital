package org.capital;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Main extends JFrame {
    private final DefaultTableModel tableModel;

    public Main() {
        setTitle("Capital");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Item");
        tableModel.addColumn("Amount");
        tableModel.addColumn("Date");
        tableModel.addColumn("Method");


        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton addButton = new JButton("Insert New Record");
        addButton.addActionListener(e -> insertNewRecord());

        add(scrollPane, BorderLayout.CENTER);
        add(addButton, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void insertNewRecord() {
        String id = JOptionPane.showInputDialog("Enter ID no:");
        String item = JOptionPane.showInputDialog("Enter Item:");
        String amount = JOptionPane.showInputDialog("Enter Amount:");
        String date = JOptionPane.showInputDialog("Enter Date:");
        String method = JOptionPane.showInputDialog("Enter Method:");

        String[] newData = {id, item, amount, date, method};
        tableModel.addRow(newData);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}
