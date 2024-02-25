package org.capital;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class Main extends JFrame {
    private final DefaultTableModel tableModel;

    public Main() {
        setTitle("Expense Tracker");
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

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(addButton, BorderLayout.SOUTH);

        add(panel);

        setVisible(true);

        loadRecords();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }

    private void loadRecords() {
        try (Connection connection = PostgresConnection.getConnection()) {
            String sql = "SELECT * FROM expenses";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        int id = resultSet.getInt("ID");
                        String item = resultSet.getString("Item");
                        double amount = resultSet.getDouble("Amount");
                        Timestamp createdAt = resultSet.getTimestamp("created_at");
                        String method = resultSet.getString("Method");

                        Object[] rowData = {id, item, amount, createdAt, method};
                        tableModel.addRow(rowData);
                    }
                }
            }
        } catch (SQLException e) {
            e.getMessage();
        }
    }

    private void insertNewRecord() {
        try {
            String item = JOptionPane.showInputDialog("Enter Item:");
            double amount = Double.parseDouble(JOptionPane.showInputDialog("Enter Amount:"));
            String method = JOptionPane.showInputDialog("Enter Method:");

            Object[] newData = {null, item, amount, null, method};
            tableModel.addRow(newData);

            try (Connection connection = PostgresConnection.getConnection()) {
                String sql = "INSERT INTO expenses (Item, Amount, Method) VALUES (?, ?, ?) RETURNING ID, created_at";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, item);
                    statement.setDouble(2, amount);
                    statement.setString(3, method);

                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (resultSet.next()) {
                            int generatedId = resultSet.getInt("ID");
                            Timestamp createdAt = resultSet.getTimestamp("created_at");

                            tableModel.setValueAt(generatedId, tableModel.getRowCount() - 1, 0);
                            tableModel.setValueAt(createdAt, tableModel.getRowCount() - 1, 3);
                        }
                    }
                }
            }
        } catch (NumberFormatException | SQLException e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter valid data.");
        }
    }
}
