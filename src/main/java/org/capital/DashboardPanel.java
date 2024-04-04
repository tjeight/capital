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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardPanel extends JPanel {
    private final String transactionTableName;

    public DashboardPanel(String transactionTableName) {
        this.transactionTableName = transactionTableName;

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel transactionsPanel = createRecentTransactionsPanel();
        JPanel detailsPanel = createDetailsPanel();

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, transactionsPanel, detailsPanel);
        splitPane.setDividerLocation(300);

        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel createRecentTransactionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Recent Transactions"));

        List<Transaction> recentTransactions = getLast10Transactions();
        String[] columnNames = {"Item", "Amount", "Method", "Date", "Tag"};

        DefaultTableModel recentTransactionsModel = new DefaultTableModel(columnNames, 0);
        JTable transactionsTable = new JTable(recentTransactionsModel);

        JButton refreshTransactionsButton = new JButton("Refresh Transactions");
        refreshTransactionsButton.setPreferredSize(new Dimension(30, 20));
        refreshTransactionsButton.setBackground(Color.BLACK);
        refreshTransactionsButton.setForeground(Color.WHITE);
        refreshTransactionsButton.setBorderPainted(false);
        refreshTransactionsButton.setFocusPainted(false);
        refreshTransactionsButton.setContentAreaFilled(false);
        refreshTransactionsButton.setOpaque(true);

        refreshTransactionsButton.addActionListener(e -> {
            recentTransactionsModel.setRowCount(0);
            recentTransactions.clear();
            getLast10Transactions().forEach(transaction -> recentTransactionsModel.addRow(new Object[]{transaction.item, transaction.amount, transaction.method, transaction.createdAt, transaction.transactionTag}));
        });

        panel.add(refreshTransactionsButton, BorderLayout.PAGE_END);

        for (Transaction transaction : recentTransactions) {
            recentTransactionsModel.addRow(new Object[]{transaction.item, transaction.amount, transaction.method, transaction.createdAt, transaction.transactionTag});
        }

        transactionsTable.setEnabled(false);
        transactionsTable.getTableHeader().setReorderingAllowed(false);
        transactionsTable.setPreferredScrollableViewportSize(new Dimension(panel.getWidth() / 2, 150));

        panel.add(new JScrollPane(transactionsTable), BorderLayout.CENTER);

        return panel;
    }

    public JPanel createDetailsPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Insights"));

        double monthlySpend = calculateMonthlySpend();
        JLabel monthSpendLabel = new JLabel("Monthly Spends: " + String.format("%.2f", monthlySpend));
        panel.add(monthSpendLabel);

        String biggestTransactions = getBiggestTransactions();

        if (biggestTransactions != null) {
            JLabel biggestTransactionLabel = new JLabel("Biggest Transaction: " + biggestTransactions);
            panel.add(biggestTransactionLabel);
        } else {
            JLabel biggestTransactionLabel = new JLabel("Biggest Transaction: N/A");
            panel.add(biggestTransactionLabel);
        }


        Map<String, Integer> transactionTagCount = getTransactionTagCount();
        Map<String, Integer> transactionMethodCount = getTransactionMethodCount();

        JLabel topTransactionTagLabel = new JLabel("Top Transaction Tag: " + getTopTransactionTag(transactionTagCount));
        panel.add(topTransactionTagLabel);

        JLabel topTransactionMethodLabel = new JLabel("Top Transaction Method: " + getTopTransactionMethod(transactionMethodCount));
        panel.add(topTransactionMethodLabel);

        double todaySpend = calculateTodaySpend();
        JLabel todaySpendLabel = new JLabel("Today's Spend:" + String.format("%.2f", todaySpend));
        panel.add(todaySpendLabel);

        return panel;
    }

    private List<Transaction> getLast10Transactions() {
        List<Transaction> transactions = new ArrayList<>();

        try (Connection connection = PostgresConnection.getConnection()) {
            String sql = "SELECT item_name, item_amount, transaction_method, transaction_date, transaction_tag FROM " + transactionTableName + " ORDER BY transaction_date DESC LIMIT 10";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        String item = resultSet.getString("item_name");
                        double amount = resultSet.getDouble("item_amount");
                        String method = resultSet.getString("transaction_method");
                        String createdAt = resultSet.getString("transaction_date");
                        String transactionTag = resultSet.getString("transaction_tag");

                        transactions.add(new Transaction(item, amount, method, createdAt, transactionTag));
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
            String sql = "SELECT SUM(item_amount) AS balance FROM " + transactionTableName + " WHERE transaction_date >= DATE_TRUNC('month', CURRENT_DATE)";
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

    private String getBiggestTransactions() {
        String biggestTransactions = null;

        try (Connection connection = PostgresConnection.getConnection()) {
            String sql = "SELECT item_name FROM " + transactionTableName + " ORDER BY item_amount DESC LIMIT 1";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        biggestTransactions = resultSet.getString("item_name");
                    }
                }
            }
        } catch (SQLException e) {
            e.getMessage();
        }

        return biggestTransactions;
    }

    private Map<String, Integer> getTransactionTagCount() {
        Map<String, Integer> transactionTagCount = new HashMap<>();

        try (Connection connection = PostgresConnection.getConnection()) {
            String sql = "SELECT transaction_tag, COUNT(*) AS count FROM " + transactionTableName + " WHERE transaction_date >= DATE_TRUNC('month', CURRENT_DATE) GROUP BY transaction_tag ORDER BY count DESC LIMIT 1";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        String transactionTag = resultSet.getString("transaction_tag");
                        int count = resultSet.getInt("count");
                        transactionTagCount.put(transactionTag, count);
                    }
                }
            }
        } catch (SQLException e) {
            e.getMessage();
        }

        return transactionTagCount;
    }

    private Map<String, Integer> getTransactionMethodCount() {
        Map<String, Integer> transactionMethodCount = new HashMap<>();

        try (Connection connection = PostgresConnection.getConnection()) {
            String sql = "SELECT transaction_method, COUNT(*) AS count FROM " + transactionTableName + " WHERE transaction_date >= DATE_TRUNC('month', CURRENT_DATE) GROUP BY transaction_method ORDER BY count DESC LIMIT 1";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        String transactionMethod = resultSet.getString("transaction_method");
                        int count = resultSet.getInt("count");
                        transactionMethodCount.put(transactionMethod, count);
                    }
                }
            }
        } catch (SQLException e) {
            e.getMessage();
        }

        return transactionMethodCount;
    }

    private String getTopTransactionTag(Map<String, Integer> transactionTagCount) {
        if (!transactionTagCount.isEmpty()) {
            return transactionTagCount.keySet().iterator().next();
        }
        return "N/A";
    }

    private String getTopTransactionMethod(Map<String, Integer> transactionMethodCount) {
        if (!transactionMethodCount.isEmpty()) {
            return transactionMethodCount.keySet().iterator().next();
        }
        return "N/A";
    }

    private double calculateTodaySpend() {
        double todaySpend = 0.0;

        try (Connection connection = PostgresConnection.getConnection()) {
            String sql = "SELECT SUM(item_amount) AS balance FROM " + transactionTableName + " WHERE DATE(transaction_date) = DATE(CURRENT_TIMESTAMP)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        todaySpend = resultSet.getDouble("balance");
                    }
                }
            }
        } catch (SQLException e) {
            e.getMessage();
        }

        return todaySpend;
    }

    private record Transaction(String item, double amount, String method, String createdAt, String transactionTag) {
    }
}