package org.capital;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AccountPanel extends JPanel {
    public AccountPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Components for account-related functions
        JPanel accountPanel = createAccountPanel();
        add(accountPanel, BorderLayout.CENTER);
    }

    private JPanel createAccountPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Account Functions"));

        // Create and add components for account-related functions
        // ...

        return panel;
    }
}
