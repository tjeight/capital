package org.capital;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class HistoryPanel extends JPanel {
    private final DefaultTableModel tableModel;
    private final String transationTableName;

    public HistoryPanel(DefaultTableModel tableModel, String transactionTableName) {
        this.transationTableName = transactionTableName;
        this.tableModel = tableModel;
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel historyPanel = createHistoryPanel();
        add(historyPanel, BorderLayout.CENTER);
    }

    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Transaction History"));

        JTable historyTable = new JTable(tableModel);
        historyTable.setPreferredScrollableViewportSize(new Dimension(400, 200));
        historyTable.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(historyTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }
}
