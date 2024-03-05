package org.capital;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DashboardPanel extends JPanel {

    public DashboardPanel() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel transactionsPanel = createRecentTransactionsPanel();

        JPanel detailsPanel = createDetailsPanel();

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, transactionsPanel, detailsPanel);
        splitPane.setDividerLocation(150);

        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel createRecentTransactionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Recent Transactions"));

        List<Transaction> recentTransactions = getLast10Transactions();
        String[] columnNames = {"Item", "Amount", "Method", "Date"};

        DefaultTableModel recentTransactionsModel = new DefaultTableModel(columnNames, 0);
        JTable transactionsTable = new JTable(recentTransactionsModel);

        for (Transaction transaction : recentTransactions) {
            recentTransactionsModel.addRow(new Object[]{transaction.item, transaction.amount, transaction.method, transaction.createdAt});
        }

        transactionsTable.setEnabled(false);
        transactionsTable.getTableHeader().setReorderingAllowed(false);
        transactionsTable.setPreferredScrollableViewportSize(transactionsTable.getPreferredSize());

        panel.add(new JScrollPane(transactionsTable), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createDetailsPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Finance Insights"));

        double currentBalance = calculateBalance();
        JLabel currentBalanceLabel = new JLabel("Current Balance: " + currentBalance);
        panel.add(currentBalanceLabel);

        double monthlySpend = calculateMonthlySpend();
        JLabel monthSpendLabel = new JLabel("Monthly Spends: " + monthlySpend);
        panel.add(monthSpendLabel);

        Transaction biggestTransaction = getBiggestTransaction();
        if (biggestTransaction != null) {
            JLabel biggestTransactionLabel = new JLabel("Biggest Transaction: " + biggestTransaction.item + " - " + biggestTransaction.amount);
            panel.add(biggestTransactionLabel);
        } else {
            JLabel noTransactionLabel = new JLabel("No major transactions available.");
            panel.add(noTransactionLabel);
        }

        return panel;
    }

    private List<Transaction> getLast10Transactions() {
        List<Transaction> transactions = new ArrayList<>();

        try (Connection connection = PostgresConnection.getConnection()) {
            String sql = "SELECT item, amount, method, created_at FROM expenses ORDER BY created_at DESC LIMIT 10";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        String item = resultSet.getString("item");
                        double amount = resultSet.getDouble("amount");
                        String method = resultSet.getString("method");
                        String createdAt = resultSet.getString("created_at");

                        transactions.add(new Transaction(item, amount, method, createdAt));
                    }
                }
            }
        } catch (SQLException e) {
            e.getMessage();
        }

        return transactions;
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

    private Transaction getBiggestTransaction() {
        Transaction biggestTransaction = null;

        try (Connection connection = PostgresConnection.getConnection()) {
            String sql = "SELECT item, amount, method, created_at FROM expenses ORDER BY amount DESC LIMIT 1";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        String item = resultSet.getString("item");
                        double amount = resultSet.getDouble("amount");
                        String method = resultSet.getString("method");
                        String createdAt = resultSet.getString("created_at");

                        biggestTransaction = new Transaction(item, amount, method, createdAt);
                    }
                }
            }
        } catch (SQLException e) {
            e.getMessage();
        }

        return biggestTransaction;
    }

    private record Transaction(String item, double amount, String method, String createdAt) {
    }
}
