package org.capital;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class Main extends JFrame {
    private final DefaultTableModel tableModel;

    public Main() {
        setTitle("Capital");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 600);

        Font customFont = new Font("JetBrains Mono", Font.PLAIN, 12);
        this.setFont(customFont);
        this.setIconImage(new ImageIcon("../../assets/favicon.png").getImage());

        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Item");
        tableModel.addColumn("Amount");
        tableModel.addColumn("Date");
        tableModel.addColumn("Method");

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Dashboard", new DashboardPanel());
        tabbedPane.addTab("Insert", new InsertPanel(tableModel));
        tabbedPane.addTab("History", new HistoryPanel(tableModel));
        tabbedPane.addTab("Balance", new BalancePanel());
        tabbedPane.addTab("Insights", new InsightsPanel());
        tabbedPane.addTab("Account", new AccountPanel());

        add(tabbedPane);
        setVisible(true);

        boolean isLoggedIn = false;
        if (!isLoggedIn) {
            new LoginUser().setVisible(true);
            dispose();
        } else {
            loadRecords();
        }
    }

    public static void main(String[] args) {
        JetBrainsMonoFont geistFont = new JetBrainsMonoFont();
        geistFont.useJetBrainsMonoFont();

        Font customFont = new Font("JetBrains Mono", Font.PLAIN, 12);
        setUIFont(customFont);

        SwingUtilities.invokeLater(Main::new);
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
            String sql = "SELECT * FROM expenses";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        int id = resultSet.getInt("ID");
                        String item = resultSet.getString("Item");
                        double amount = resultSet.getDouble("Amount");
                        Timestamp createdAt = resultSet.getTimestamp("created_at");
                        String method = resultSet.getString("Method");

                        Object[] rowData = {id, item, amount, createdAt, method};
                        tableModel.addRow(rowData);
                    }
                }
            }
        } catch (SQLException e) {
            e.getMessage();
        }
    }
}
