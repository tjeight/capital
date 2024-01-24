package org.capital;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Main extends JFrame {
    private final DefaultTableModel tableModel;

    private static final String DB_CONNECTION_URL = "jdbc:postgresql://ep-green-bush-10717632-pooler.ap-southeast-1.aws.neon.tech/capital?user=vgseven&password=zK3WXCe0gYox&sslmode=require";
    private static final String DB_USERNAME = "vgseven";
    private static final String DB_PASSWORD = "zK3WXCe0gYox";

    public Main() {
        setTitle("Capital");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Item");
        tableModel.addColumn("Amount");
        tableModel.addColumn("Date");
        tableModel.addColumn("Method");


        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton addButton = new JButton("Insert New Record");
        addButton.addActionListener(e -> insertNewRecord());

        add(scrollPane, BorderLayout.CENTER);
        add(addButton, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void insertNewRecord() {
        String id = JOptionPane.showInputDialog("Enter ID:");
        String item = JOptionPane.showInputDialog("Enter Item:");
        String amount = JOptionPane.showInputDialog("Enter Amount:");
        String date = JOptionPane.showInputDialog("Enter Date:");
        String method = JOptionPane.showInputDialog("Enter Method:");

        String[] newData = {id, item, amount, date, method };
        tableModel.addRow(newData);

        insertDataIntoDatabase(id, item, amount, date, method);
    }

    private void insertDataIntoDatabase(String id, String item, String amount, String date, String method) {
        try {

            Class.forName("org.postgresql.Driver");

            Connection connection = DriverManager.getConnection(DB_CONNECTION_URL, DB_USERNAME, DB_PASSWORD);

            String sql = "INSERT INTO expenses (id, item, amount, date, method) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, id);
                preparedStatement.setString(2, item);
                preparedStatement.setString(3, amount);
                preparedStatement.setString(4, date);
                preparedStatement.setString(5, method);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Record inserted successfully.");
                    connection.commit();
                } else {
                    System.out.println("Failed to insert record.");
                }
            }

            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.getMessage();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}
