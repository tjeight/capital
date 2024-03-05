package org.capital;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class InsertPanel extends JPanel {

    private final DefaultTableModel tableModel;
    JTextField itemField = new JTextField();
    JTextField amountField = new JTextField();
    JTextField methodField = new JTextField();

    public InsertPanel(DefaultTableModel tableModel) {
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

        JButton insertButton = new JButton("Insert New Record");
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(insertButton, gbc);

        insertButton.addActionListener(e -> insertNewRecord(itemField.getText(), amountField.getText(), methodField.getText()));

        return panel;
    }

    private void insertNewRecord(String item, String amount, String method) {
        try {
            if (item.isEmpty() || amount.isEmpty() || method.isEmpty()) {
                throw new IllegalArgumentException("Please fill in all fields.");
            }

            double parsedAmount = Double.parseDouble(amount);

            Object[] newData = {null, item, parsedAmount, null, method};
            tableModel.addRow(newData);

            try (Connection connection = PostgresConnection.getConnection()) {
                String sql = "INSERT INTO expenses (Item, Amount, Method) VALUES (?, ?, ?) RETURNING ID, created_at";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, item);
                    statement.setDouble(2, parsedAmount);
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

            itemField.setText("");
            amountField.setText("");
            methodField.setText("");

        } catch (SQLException | IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input. " + ex.getMessage());
        }
    }
}
