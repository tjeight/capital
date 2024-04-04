package org.capital;

import javax.swing.*;
import java.awt.*;

public class WelcomeScreen extends JFrame {
    public WelcomeScreen() {
        setTitle("Welcome to Capital");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setBackground(Color.WHITE);

        PoppinsFont poppinsFont = new PoppinsFont();
        poppinsFont.usePoppinsFont();

        Font usePoppinsFont = new Font("Poppins", Font.BOLD, 14);

        JPanel welcomePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel titleLabel = new JLabel("<html><div style='color: black'>Capital</div></html>");
        titleLabel.setFont(new Font("Poppins", Font.BOLD, 38));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(50, 20, 20, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        welcomePanel.add(titleLabel, gbc);

        JLabel introLabel = new JLabel("<html><div style='color: black;'>Welcome to Capital, The Personal Finance Assistant. </div></html>");
        introLabel.setFont(new Font("Poppins", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        welcomePanel.add(introLabel, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        JButton signInButton = new JButton("Sign In");
        signInButton.setFont(usePoppinsFont);
        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setFont(usePoppinsFont);

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
            SignInUser loginUser = new SignInUser();
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