package org.capital;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SignUpUser extends JFrame implements ActionListener {
    private final JTextField nameField;
    private final JTextField bankNameField;
    private final JTextField usernameField;
    private final JPasswordField passwordField;

    public SignUpUser() {
        setTitle("Sign Up");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 550);
        setLocationRelativeTo(null);

        PoppinsFont poppinsFont = new PoppinsFont();
        poppinsFont.usePoppinsFont();

        Font usePoppinsFont = new Font("Poppins", Font.PLAIN, 12);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.anchor = GridBagConstraints.WEST;

        // Name Label
        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setFont(usePoppinsFont);
        mainPanel.add(nameLabel, gbc);

        // Name Field
        gbc.gridx = 1;
        nameField = new JTextField(20);
        nameField.setFont(usePoppinsFont);
        mainPanel.add(nameField, gbc);

        // Bank Name Label
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel bankNameLabel = new JLabel("Bank Name:");
        bankNameLabel.setFont(usePoppinsFont);
        mainPanel.add(bankNameLabel, gbc);

        // Bank Name Field
        gbc.gridx = 1;
        bankNameField = new JTextField(20);
        bankNameField.setFont(usePoppinsFont);
        mainPanel.add(bankNameField, gbc);

        // Username Label
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(usePoppinsFont);
        mainPanel.add(usernameLabel, gbc);

        // Username Field
        gbc.gridx = 1;
        usernameField = new JTextField(20);
        usernameField.setFont(usePoppinsFont);
        mainPanel.add(usernameField, gbc);

        // Password Label
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(usePoppinsFont);
        mainPanel.add(passwordLabel, gbc);

        // Password Field
        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        passwordField.setFont(usePoppinsFont);
        mainPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setBackground(Color.BLACK);
        signUpButton.setForeground(Color.WHITE);
        signUpButton.setFont(usePoppinsFont);
        signUpButton.setBorderPainted(false);
        signUpButton.setFocusPainted(false);
        signUpButton.setContentAreaFilled(false);
        signUpButton.setOpaque(true);
        signUpButton.setPreferredSize(new Dimension(120, 40));
        signUpButton.addActionListener(this);
        mainPanel.add(signUpButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JButton backButton = new JButton("Back");
        backButton.setBackground(Color.BLACK);
        backButton.setForeground(Color.WHITE);
        backButton.setFont(usePoppinsFont);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setOpaque(true);
        backButton.setPreferredSize(new Dimension(120, 40));
        backButton.addActionListener(this);
        mainPanel.add(backButton, gbc);

        add(mainPanel);
        setVisible(true);

        backButton.addActionListener(e -> {
            new WelcomeScreen().setVisible(true);
            this.setVisible(false);
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String name = nameField.getText();
        String bankName = bankNameField.getText();
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (name.isBlank() || bankName.isBlank() || username.isBlank() || password.isBlank()) {
            JOptionPane.showMessageDialog(this, "Please enter valid details..");
        } else {
            if (isUserExists(username)) {
                JOptionPane.showMessageDialog(this, "Username already exists. Please choose a different username.", "Error", JOptionPane.ERROR_MESSAGE);
                usernameField.setText("");
            } else {
                if (insertUserData(name, bankName, username, password)) {
                    createUserTable(username);
                    clearFields();
                    JOptionPane.showMessageDialog(this, "Sign Up successful!");
                    this.setVisible(false);
                    Capital capital = new Capital(username + "_transactions");
                    capital.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Sign Up failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

    }

    private boolean isUserExists(String username) {
        try (Connection conn = PostgresConnection.getConnection(); PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM users WHERE user_name = ?")) {
            checkStmt.setString(1, username);
            try (ResultSet rs = checkStmt.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean insertUserData(String name, String bankName, String username, String password) {
        try (Connection conn = PostgresConnection.getConnection(); PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO users (full_name, bank_name, user_name, password) VALUES (?, ?, ?, ?)")) {
            insertStmt.setString(1, name);
            insertStmt.setString(2, bankName);
            insertStmt.setString(3, username);
            insertStmt.setString(4, password);

            int rowsAffected = insertStmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void createUserTable(String username) {
        try (Connection conn = PostgresConnection.getConnection(); PreparedStatement createTableStmt = conn.prepareStatement("CREATE TABLE IF NOT EXISTS " + username + "_transactions (transaction_id SERIAL PRIMARY KEY, item_name VARCHAR(255) NOT NULL, item_amount DECIMAL(10,2) NOT NULL, transaction_method VARCHAR(50) NOT NULL, transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL, transaction_tag VARCHAR(50) NOT NULL)")) {
            createTableStmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void clearFields() {
        nameField.setText("");
        bankNameField.setText("");
        usernameField.setText("");
        passwordField.setText("");
    }
}