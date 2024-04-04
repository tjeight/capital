package org.capital;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountPanel extends JPanel {
    private final String transactionTableName;

    public AccountPanel(String transactionTableName) {
        this.transactionTableName = transactionTableName;

        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel userInfoPanel = createUserInfoPanel();
        add(userInfoPanel, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        add(gridPanel, BorderLayout.CENTER);

        JPanel creditCardPanel = createCardPanel("Visa", "XXXX-XXXX-XXXX-3333", "Balance: 5000", "Credit Capacity: 100000");
        gridPanel.add(createBorderedPanel(creditCardPanel, "Credit Cards"));

        JPanel debitCardPanel = createCardPanel("RuPay", "XXXX-XXXX-XXXX-6666", "Balance: 20000");
        gridPanel.add(createBorderedPanel(debitCardPanel, "Debit Cards"));

        JPanel savingAccountPanel = createCardPanel("HDFC Bank", "XXXX-XXXX-XXXX-1234", "Balance: 150000");
        gridPanel.add(createBorderedPanel(savingAccountPanel, "Saving Accounts"));

        JPanel currentAccountPanel = createCardPanel("HDFC Bank", "XXXX-XXXX-XXXX-1111", "Balance: 50000");
        gridPanel.add(createBorderedPanel(currentAccountPanel, "Current Accounts"));
    }

    private JPanel createUserInfoPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("User Information"));

        String[] usersDetails = getUserDetails(getTrimmedString(transactionTableName));

        JLabel fullNameLabel = new JLabel("Full Name: " + usersDetails[0]);
        JLabel bankNameLabel = new JLabel("Bank Name: " + usersDetails[1]);
        JLabel usernameLabel = new JLabel("Username: " + usersDetails[2]);
        Font usePoppinsFont = new Font("Poppins", Font.PLAIN, 12);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(Color.black);
        logoutButton.setForeground(Color.white);
        logoutButton.setPreferredSize(new Dimension(100, 25));
        logoutButton.setFont(usePoppinsFont);
        logoutButton.setBorderPainted(false);
        logoutButton.setFocusPainted(false);
        logoutButton.setContentAreaFilled(false);
        logoutButton.setOpaque(true);

        logoutButton.addActionListener(e -> {
            SwingUtilities.getWindowAncestor(this).dispose();
            new WelcomeScreen().setVisible(true);
        });

        panel.add(fullNameLabel);
        panel.add(bankNameLabel);
        panel.add(usernameLabel);
        panel.add(logoutButton);

        return panel;
    }

    private JPanel createCardPanel(String cardType, String cardNumber, String... details) {
        JPanel cardPanel = new JPanel(new GridLayout(details.length + 3, 1, 10, 10));
        cardPanel.setBackground(new Color(240, 240, 240));

        JLabel typeLabel = new JLabel("Type: " + cardType);
        typeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardPanel.add(typeLabel);

        JLabel numberLabel = new JLabel("Number: " + cardNumber);
        numberLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardPanel.add(numberLabel);

        for (String detail : details) {
            JLabel detailLabel = new JLabel(detail);
            detailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            cardPanel.add(detailLabel);
        }

        return cardPanel;
    }

    private JPanel createBorderedPanel(JPanel contentPanel, String title) {
        JPanel borderedPanel = new JPanel(new BorderLayout());
        borderedPanel.setBorder(BorderFactory.createTitledBorder(title));
        borderedPanel.add(contentPanel, BorderLayout.CENTER);

        return borderedPanel;
    }

    private String[] getUserDetails(String username) {
        String[] userDetails = new String[3];

        try (Connection connection = PostgresConnection.getConnection()) {
            String sql = "SELECT full_name, bank_name, user_name FROM users WHERE user_name = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, username);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        userDetails[0] = resultSet.getString("full_name");
                        userDetails[1] = resultSet.getString("bank_name");
                        userDetails[2] = resultSet.getString("user_name");
                    }
                }
            }
        } catch (SQLException e) {
            e.getMessage();
        }

        return userDetails;
    }

    private String getTrimmedString(String input) {
        if (input != null && !input.isEmpty()) {
            int underscoreIndex = input.indexOf("_");
            if (underscoreIndex != -1) {
                return input.substring(0, underscoreIndex);
            }
        }
        return input;
    }
}