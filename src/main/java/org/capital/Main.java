package org.capital;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

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

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Dashboard", new DashboardPanel(tableModel));
        tabbedPane.addTab("Insert", new InsertPanel(tableModel));
        tabbedPane.addTab("History", new HistoryPanel(tableModel));
        tabbedPane.addTab("Balance", new BalancePanel());
        tabbedPane.addTab("Insights", new InsightsPanel());
        tabbedPane.addTab("Account", new AccountPanel());

        add(tabbedPane);

        setVisible(true);

        loadRecords();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }

    private void loadRecords() {
        // Load existing records from the database
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
}
