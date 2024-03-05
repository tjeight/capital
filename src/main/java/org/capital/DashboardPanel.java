package org.capital;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DashboardPanel extends JPanel {
    private final DefaultTableModel tableModel;

    public DashboardPanel(DefaultTableModel tableModel) {
        this.tableModel = tableModel;
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Components for recent transactions
        JPanel transactionsPanel = createRecentTransactionsPanel();

        // Components for all financial details
        JPanel detailsPanel = createDetailsPanel();

        // Create a horizontal split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, transactionsPanel, detailsPanel);
        splitPane.setDividerLocation(150);  // Adjust the initial divider location

        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel createRecentTransactionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Recent Transactions"));

        JTable transactionsTable = new JTable(tableModel);
        transactionsTable.setPreferredScrollableViewportSize(new Dimension(800, 150));
        transactionsTable.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(transactionsTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createDetailsPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Finance Insights"));

        // Retrieve and display Current balance
        double currentBalance = calculateBalance();
        JLabel currentBalanceLabel = new JLabel("Current Balance: " + currentBalance);
        panel.add(currentBalanceLabel);

        // Monthly Spends
        double monthlySpend = calculateMonthlySpend();
        JLabel monthSpendLabel = new JLabel("Monthly Spends: " + monthlySpend);
        panel.add(monthSpendLabel);

        return panel;
    }

    private double calculateMonthlySpend() {
        double balance = 0.0;

        try (Connection connection = PostgresConnection.getConnection()) {
            String sql = "SELECT SUM(Amount) AS Balance FROM expenses";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        balance = resultSet.getDouble("Balance");
                    }
                }
            }
        } catch (SQLException e) {
            e.getMessage();
        }

        return balance;
    }

    private double calculateBalance() {
        double MonthBalance = 10000.00;

        return MonthBalance - calculateMonthlySpend();
    }
}
