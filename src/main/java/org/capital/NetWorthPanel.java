package org.capital;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class NetWorthPanel extends JPanel {

    public NetWorthPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel balancePanel = createBalancePanel();
        add(balancePanel, BorderLayout.CENTER);
    }

    private JPanel createBalancePanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Current Net Worth"));

        JLabel accountBalance1Label = new JLabel("Jupiter Federal Bank 9600:");
        JLabel accountBalance1Label1 = new JLabel("100000.00");
        panel.add(accountBalance1Label);
        panel.add(accountBalance1Label1);

        JLabel accountBalance2Label = new JLabel("Jupiter Federal Bank 8566:");
        JLabel accountBalance2Label1 = new JLabel("70467.00");
        panel.add(accountBalance2Label);
        panel.add(accountBalance2Label1);

        JLabel creditCardBalanceLabel = new JLabel("Jupiter Credit Card:");
        JLabel creditCardBalanceLabel1 = new JLabel("25000.00");
        panel.add(creditCardBalanceLabel);
        panel.add(creditCardBalanceLabel1);

        JLabel investedAmountLabel = new JLabel("Kite:");
        JLabel investedAmountLabel1 = new JLabel("90470.00");
        panel.add(investedAmountLabel);
        panel.add(investedAmountLabel1);

        JLabel otherBalanceLabel = new JLabel("Other Balance:");
        JLabel otherBalanceLabel1 = new JLabel("73580.00");
        panel.add(otherBalanceLabel);
        panel.add(otherBalanceLabel1);

        return panel;
    }

}