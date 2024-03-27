package org.capital;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginUser extends JFrame implements ActionListener {
    private final JTextField usernameField;
    private final JPasswordField passwordField;

    public LoginUser() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 250);
        setLocationRelativeTo(null);

        JetBrainsMonoFont monoFont = new JetBrainsMonoFont();
        monoFont.useJetBrainsMonoFont();

        Font jetBrainsMono = new Font("JetBrains Mono", Font.PLAIN, 12);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(jetBrainsMono);
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(jetBrainsMono);
        usernameField = new JTextField(20);
        usernameField.setFont(jetBrainsMono);
        passwordField = new JPasswordField(20);
        passwordField.setFont(jetBrainsMono);
        JButton loginButton = new JButton("Login");
        loginButton.setFont(jetBrainsMono);

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
        add(loginButton, gbc);

        loginButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (isValidUser(username, password)) {
            this.setVisible(false);
            Capital capital = new Capital();
            capital.setVisible(true);

        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isValidUser(String username, String password) {
        return username.equals("capital") && password.equals("seven");
    }
}