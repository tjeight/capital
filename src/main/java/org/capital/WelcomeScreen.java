package org.capital;

import javax.swing.*;
import java.awt.*;

public class WelcomeScreen extends JFrame {
    public WelcomeScreen() {
        setTitle("Welcome to Capital");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);

        JetBrainsMonoFont jetBrainsMonoFont = new JetBrainsMonoFont();
        jetBrainsMonoFont.useJetBrainsMonoFont();

        JPanel welcomePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 20, 20);

        JLabel welcomeLabel = new JLabel("Welcome to Capital");
        welcomeLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 28));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomePanel.add(welcomeLabel, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        JButton signInButton = new JButton("Sign In");
        signInButton.setFont(new Font("JetBrains Mono", Font.BOLD, 12));
        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setFont(new Font("JetBrains Mono", Font.BOLD, 12));

        signInButton.setBackground(Color.BLACK);
        signInButton.setForeground(Color.WHITE);
        signInButton.setBorderPainted(false);
        signInButton.setFocusPainted(false);
        signInButton.setContentAreaFilled(false);
        signInButton.setOpaque(true);
        signInButton.setPreferredSize(new Dimension(120, 40));

        signUpButton.setBackground(Color.BLACK);
        signUpButton.setForeground(Color.WHITE);
        signUpButton.setBorderPainted(false);
        signUpButton.setFocusPainted(false);
        signUpButton.setContentAreaFilled(false);
        signUpButton.setOpaque(true);
        signUpButton.setPreferredSize(new Dimension(120, 40));

        buttonPanel.add(signInButton);
        buttonPanel.add(signUpButton);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints mainGbc = new GridBagConstraints();
        mainGbc.gridx = 0;
        mainGbc.gridy = 0;
        mainGbc.insets = new Insets(20, 20, 20, 20);
        mainPanel.add(welcomePanel, mainGbc);

        mainGbc.gridy = 1;
        mainPanel.add(buttonPanel, mainGbc);

        add(mainPanel);
        setVisible(true);

        signInButton.addActionListener(e -> {
            LoginUser loginUser = new LoginUser();
            loginUser.setVisible(true);
            this.setVisible(false);
        });

        signUpButton.addActionListener(e -> {
            SignUpUser signUpUser = new SignUpUser();
            signUpUser.setVisible(true);
            this.setVisible(false);
        });
    }
}