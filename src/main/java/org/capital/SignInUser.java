package org.capital;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SignInUser extends JFrame implements ActionListener {
    private final JTextField usernameField;
    private final JPasswordField passwordField;

    public SignInUser() {
        setTitle("Sign In");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 250);
        setLocationRelativeTo(null);

        PoppinsFont poppinsFont = new PoppinsFont();
        poppinsFont.usePoppinsFont();

        Font usePoppinsFont = new Font("Poppins", Font.PLAIN, 12);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(usePoppinsFont);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(usePoppinsFont);

        usernameField = new JTextField(20);
        usernameField.setFont(usePoppinsFont);

        passwordField = new JPasswordField(20);
        passwordField.setFont(usePoppinsFont);

        JButton signInButton = new JButton("Sign In");
        signInButton.setFont(usePoppinsFont);
        signInButton.setBackground(Color.black);
        signInButton.setForeground(Color.white);
        signInButton.setBorderPainted(false);
        signInButton.setFocusPainted(false);
        signInButton.setContentAreaFilled(false);
        signInButton.setOpaque(true);

        JButton backButton = new JButton("Back");
        backButton.setFont(usePoppinsFont);
        backButton.setBackground(Color.black);
        backButton.setForeground(Color.white);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setOpaque(true);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(signInButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(backButton, gbc);

        signInButton.addActionListener(this);

        backButton.addActionListener(e -> {
            new WelcomeScreen().setVisible(true);
            this.setVisible(false);
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (isValidUser(username, password)) {
            this.setVisible(false);

            Capital capital = new Capital(username + "_transactions");
            capital.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isValidUser(String username, String password) {
        if (!username.isBlank() || !password.isBlank()) {
            try (Connection conn = PostgresConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE user_name = ? AND password = ?")) {
                stmt.setString(1, username);
                stmt.setString(2, password);
                try (ResultSet rs = stmt.executeQuery()) {
                    return rs.next();
                }
            } catch (SQLException ex) {
                ex.getMessage();
                return false;
            }
        }

        return false;
    }
}