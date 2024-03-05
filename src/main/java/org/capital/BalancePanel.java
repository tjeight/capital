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

        JPanel balancePanel = createBalancePanel();
        add(balancePanel, BorderLayout.CENTER);
    }

    private JPanel createBalancePanel() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Account Balance"));

        double balance = calculateBalance();
        JLabel balanceLabel = new JLabel("Balance: " + balance);
        panel.add(balanceLabel);

        int totalTransactions = getTotalTransactions();
        JLabel transactionsLabel = new JLabel("Total Transactions: " + totalTransactions);
        panel.add(transactionsLabel);

        return panel;
    }

    private double calculateMonthlySpend() {
        double balance = 0.0;

        try (Connection connection = PostgresConnection.getConnection()) {
            String sql = "SELECT SUM(amount) AS balance FROM expenses";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        balance = resultSet.getDouble("balance");
                    }
                }
            }
        } catch (SQLException e) {
            e.getMessage();
        }

        return balance;
    }

    private double calculateBalance() {
        double monthBalance = 10000000.00;

        return monthBalance - calculateMonthlySpend();
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
}
