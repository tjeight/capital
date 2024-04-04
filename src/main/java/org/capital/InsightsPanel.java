package org.capital;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class InsightsPanel extends JPanel {
    private final String username;

    public InsightsPanel(String username) {
        this.username = username;
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel insightsPanel = createInsightsPanel();
        add(insightsPanel, BorderLayout.CENTER);
    }

    private JPanel createInsightsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 1, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Financial Insights"));

        JPanel insightsContentPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        JLabel insightLabel1 = new JLabel("Insight 1: Analyze your spending patterns.");
        JLabel insightLabel2 = new JLabel("Insight 2: Consider creating an emergency fund.");
        JLabel insightLabel3 = new JLabel("Insight 3: Review your investment strategy.");

        insightsContentPanel.add(insightLabel1);
        insightsContentPanel.add(insightLabel2);
        insightsContentPanel.add(insightLabel3);

        panel.add(insightsContentPanel);

        JPanel financeInsightsPanel = createFinanceInsightsPanel();
        panel.add(financeInsightsPanel);

        return panel;
    }

    private JPanel createFinanceInsightsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 1, 10, 10));
        DashboardPanel dashboardPanel = new DashboardPanel(username);
        panel.add(dashboardPanel.createDetailsPanel());

        return panel;
    }
}