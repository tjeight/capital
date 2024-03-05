package org.capital;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class InsightsPanel extends JPanel {
    public InsightsPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel insightsPanel = createInsightsPanel();
        add(insightsPanel, BorderLayout.CENTER);
    }

    private JPanel createInsightsPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Financial Insights"));

        JLabel insightLabel1 = new JLabel("Insight 1: Analyze your spending patterns.");
        JLabel insightLabel2 = new JLabel("Insight 2: Consider creating an emergency fund.");
        JLabel insightLabel3 = new JLabel("Insight 3: Review your investment strategy.");

        panel.add(insightLabel1);
        panel.add(insightLabel2);
        panel.add(insightLabel3);

        return panel;
    }
}
