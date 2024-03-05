package org.capital;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BalancePanel extends JPanel {
    public BalancePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Components for displaying balance from accounts
        JPanel balancePanel = createBalancePanel();
        add(balancePanel, BorderLayout.CENTER);
    }

    private JPanel createBalancePanel() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Account Balance"));

        // Retrieve and display balance
        double balance = calculateBalance();
        JLabel balanceLabel = new JLabel("Balance: $" + balance);
        panel.add(balanceLabel);

        // Display total number of transactions
        int totalTransactions = getTotalTransactions();
        JLabel transactionsLabel = new JLabel("Total Transactions: " + totalTransactions);
        panel.add(transactionsLabel);

        // Display average transaction amount
        double averageTransactionAmount = getAverageTransactionAmount();
        JLabel averageLabel = new JLabel("Average Transaction Amount: $" + averageTransactionAmount);
        panel.add(averageLabel);

        // Display other balance-related information as needed
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

    private int getTotalTransactions() {
        int totalTransactions = 0;

        try (Connection connection = PostgresConnection.getConnection()) {
            String sql = "SELECT COUNT(*) AS TotalTransactions FROM expenses";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        totalTransactions = resultSet.getInt("TotalTransactions");
                    }
                }
            }
        } catch (SQLException e) {
            e.getMessage();
        }

        return totalTransactions;
    }

    private double getAverageTransactionAmount() {
        double averageTransactionAmount = 0.0;

        try (Connection connection = PostgresConnection.getConnection()) {
            String sql = "SELECT AVG(Amount) AS AverageAmount FROM expenses";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        averageTransactionAmount = resultSet.getDouble("AverageAmount");
                    }
                }
            }
        } catch (SQLException e) {
            e.getMessage();
        }

        return averageTransactionAmount;
    }
}
