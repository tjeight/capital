package org.capital;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class Capital extends JFrame {
    private final DefaultTableModel tableModel;
    private final String transactionTableName;

    public Capital(String transactionTableName) {
        this.transactionTableName = transactionTableName;
        setTitle("Capital");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 600);

        JetBrainsMonoFont monoFont = new JetBrainsMonoFont();
        monoFont.useJetBrainsMonoFont();

        Font jetBrainsMono = new Font("JetBrains Mono", Font.PLAIN, 12);
        setUIFont(jetBrainsMono);

        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Item");
        tableModel.addColumn("Amount");
        tableModel.addColumn("Date");
        tableModel.addColumn("Method");
        tableModel.addColumn("Tag");

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Dashboard", new DashboardPanel(transactionTableName));
        tabbedPane.addTab("Insert", new InsertPanel(tableModel, transactionTableName));
        tabbedPane.addTab("History", new HistoryPanel(tableModel, transactionTableName));
        tabbedPane.addTab("Balance", new BalancePanel());
        tabbedPane.addTab("Insights", new InsightsPanel());
        tabbedPane.addTab("Account", new AccountPanel());

        add(tabbedPane);
        setVisible(true);

        loadRecords();
    }

    private static void setUIFont(Font font) {
        UIManager.put("Button.font", font);
        UIManager.put("CheckBox.font", font);
        UIManager.put("RadioButton.font", font);
        UIManager.put("ComboBox.font", font);
        UIManager.put("Label.font", font);
        UIManager.put("List.font", font);
        UIManager.put("MenuBar.font", font);
        UIManager.put("MenuItem.font", font);
        UIManager.put("RadioButtonMenuItem.font", font);
        UIManager.put("CheckBoxMenuItem.font", font);
        UIManager.put("PopupMenu.font", font);
        UIManager.put("OptionPane.font", font);
        UIManager.put("Panel.font", font);
        UIManager.put("ScrollPane.font", font);
        UIManager.put("Spinner.font", font);
        UIManager.put("TabbedPane.font", font);
        UIManager.put("Table.font", font);
        UIManager.put("TableHeader.font", font);
        UIManager.put("TextField.font", font);
        UIManager.put("PasswordField.font", font);
        UIManager.put("TextArea.font", font);
        UIManager.put("TextPane.font", font);
        UIManager.put("EditorPane.font", font);
        UIManager.put("TitledBorder.font", font);
        UIManager.put("ToolBar.font", font);
        UIManager.put("ToolTip.font", font);
        UIManager.put("Tree.font", font);
        UIManager.put("InternalFrame.titleFont", font);
        UIManager.put("TitledBorder.font", font);
    }

    private void loadRecords() {
        try (Connection connection = PostgresConnection.getConnection()) {
            String sql = "SELECT * FROM " + transactionTableName;
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        int id = resultSet.getInt("transaction_id");
                        String item = resultSet.getString("item_name");
                        double amount = resultSet.getDouble("item_amount");
                        Timestamp createdAt = resultSet.getTimestamp("transaction_date");
                        String method = resultSet.getString("transaction_method");
                        String tag = resultSet.getString("transaction_tag");

                        Object[] rowData = {id, item, amount, createdAt, method, tag};
                        tableModel.addRow(rowData);
                    }
                }
            }
        } catch (SQLException e) {
            e.getMessage();
        }
    }
}