package org.capital;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class InsertPanel extends JPanel {
    private final DefaultTableModel tableModel;

    public InsertPanel(DefaultTableModel tableModel) {
        this.tableModel = tableModel;
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Components for inserting new records
        JPanel formPanel = createInsertFormPanel();
        add(formPanel, BorderLayout.CENTER);
    }

    private JPanel createInsertFormPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));

        JTextField itemField = new JTextField();
        JTextField amountField = new JTextField();
        JTextField methodField = new JTextField();

        panel.add(new JLabel("Item:"));
        panel.add(itemField);
        panel.add(new JLabel("Amount:"));
        panel.add(amountField);
        panel.add(new JLabel("Method:"));
        panel.add(methodField);

        JButton insertButton = new JButton("Insert New Record");
        insertButton.addActionListener(e -> insertNewRecord(itemField.getText(), amountField.getText(), methodField.getText()));
        panel.add(insertButton);

        return panel;
    }

    private void insertNewRecord(String item, String amount, String method) {
        try {
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
        } catch (NumberFormatException | SQLException e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter valid data.");
        }
    }
}
