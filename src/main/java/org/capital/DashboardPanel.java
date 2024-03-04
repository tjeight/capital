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
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Components for recent transactions
        JPanel transactionsPanel = createRecentTransactionsPanel();
        add(transactionsPanel, BorderLayout.WEST);

        // Components for balance, account name, and other finance details
        JPanel detailsPanel = createDetailsPanel();
        add(detailsPanel, BorderLayout.CENTER);
    }

    private JPanel createRecentTransactionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Recent Transactions"));

        JTable transactionsTable = new JTable(tableModel);
        transactionsTable.setPreferredScrollableViewportSize(new Dimension(250, 150));
        transactionsTable.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(transactionsTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createDetailsPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Finance Details"));

        // Retrieve and display balance
        double balance = calculateBalance();
        JLabel balanceLabel = new JLabel("Balance: $" + balance);
        panel.add(balanceLabel);

        // Retrieve and display account name
        String accountName = getAccountName();
        JLabel accountNameLabel = new JLabel("Account Name: " + accountName);
        panel.add(accountNameLabel);

        // Display other finance details as needed
        // ...

        return panel;
    }

    private double calculateBalance() {
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

    private String getAccountName() {
        // Implement logic to retrieve account name
        // For example, you might retrieve it from a user profile or settings
        return "John Doe";
    }
}
