package org.capital;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class InsertPanel extends JPanel {

    private final DefaultTableModel tableModel;
    private final String transactionTableName;
    private final JTextField itemField = new JTextField();
    private final JTextField amountField = new JTextField();
    private final JTextField methodField = new JTextField();
    private final JTextField tagField = new JTextField();

    public InsertPanel(DefaultTableModel tableModel, String transactionTableName) {
        this.transactionTableName = transactionTableName;
        this.tableModel = tableModel;
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel formPanel = createInsertFormPanel();
        add(formPanel, BorderLayout.CENTER);
    }

    private JPanel createInsertFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Item:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(itemField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Amount:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(amountField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Method:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(methodField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(new JLabel("Tag:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(tagField, gbc);

        JButton insertButton = new JButton("Insert New Record");
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(insertButton, gbc);

        insertButton.addActionListener(e -> insertNewRecord(itemField.getText(), amountField.getText(), methodField.getText(), tagField.getText()));

        return panel;
    }

    private void insertNewRecord(String item, String amount, String method, String tag) {
        try {
            if (item.isEmpty() || amount.isEmpty() || method.isEmpty() || tag.isEmpty()) {
                throw new IllegalArgumentException("Please fill in all fields.");
            }

            double parsedAmount = Double.parseDouble(amount);

            try (Connection connection = PostgresConnection.getConnection()) {
                String sql = "INSERT INTO " + transactionTableName + " (item_name, item_amount, transaction_method, transaction_tag) VALUES (?, ?, ?, ?) RETURNING transaction_id, transaction_date";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, item);
                    statement.setDouble(2, parsedAmount);
                    statement.setString(3, method);
                    statement.setString(4, tag);

                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (resultSet.next()) {
                            int generatedId = resultSet.getInt("transaction_id");
                            Timestamp createdAt = resultSet.getTimestamp("transaction_date");

                            Object[] newData = {generatedId, item, parsedAmount, createdAt, method, tag};
                            tableModel.addRow(newData);
                        }
                    }
                }
            }

            itemField.setText("");
            amountField.setText("");
            methodField.setText("");
            tagField.setText("");

        } catch (SQLException | IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input. " + ex.getMessage());
        }
    }
}