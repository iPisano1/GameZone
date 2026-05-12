package UI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import UI.LoadFont;

public class CustomerUI extends JFrame {

   JLabel welcomeLabel;
   JLabel firstNameLabel;
   JLabel lastNameLabel;
   JLabel phoneLabel;
   JLabel emailLabel;
   JLabel addressLabel;

   JLabel ProductLabel;
   // Item List
   JCheckBox itemCheckBox;
   JLabel ItemNameLabel;

   JTextField firstNameField;
   JTextField lastNameField;
   JTextField phoneField;
   JTextField emailField;
   JTextField addressField;

   JTable itemTable;
   DefaultTableModel tableModel;
   JScrollPane scrollPane;

   public CustomerUI() {
      super("GameZone");
      initComponents();
   }

   private void initComponents() {

      Font poppins = LoadFont.loadPoppins(12f);

      welcomeLabel = new JLabel("Welcome, Customer!");
      firstNameLabel = new JLabel("First Name:");
      lastNameLabel = new JLabel("Last Name:");
      phoneLabel = new JLabel("Phone Number:");
      emailLabel = new JLabel("Email:");
      addressLabel = new JLabel("Address:");
      ProductLabel = new JLabel("Products");

      firstNameField = new JTextField(10);
      lastNameField = new JTextField(10);
      phoneField = new JTextField(10);
      emailField = new JTextField(10);
      addressField = new JTextField(10);

      // Left Panel
      JPanel LeftPanel = new JPanel();
      LeftPanel.setLayout(new BorderLayout());

      JPanel panel = new JPanel(new GridBagLayout());
      panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
      GridBagConstraints gbc = new GridBagConstraints();

      gbc.insets = new Insets(8, 8, 8, 8);
      gbc.fill = GridBagConstraints.HORIZONTAL;
      gbc.weightx = 1;

      // Title Row
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbc.gridwidth = 4;
      panel.add(welcomeLabel, gbc);

      gbc.gridwidth = 1;

      // First Row
      gbc.gridy = 1;
      gbc.gridx = 0;
      panel.add(firstNameLabel, gbc);
      gbc.gridx = 1;
      panel.add(firstNameField, gbc);

      gbc.gridx = 2;
      panel.add(lastNameLabel, gbc);
      gbc.gridx = 3;
      panel.add(lastNameField, gbc);

      // Second Row
      gbc.gridy = 2;
      gbc.gridx = 0;
      panel.add(phoneLabel, gbc);
      gbc.gridx = 1;
      panel.add(phoneField, gbc);

      gbc.gridx = 2;
      panel.add(emailLabel, gbc);
      gbc.gridx = 3;
      panel.add(emailField, gbc);

      // Third Row
      gbc.gridy = 3;
      gbc.gridx = 0;
      panel.add(addressLabel, gbc);
      gbc.gridx = 1;
      gbc.gridwidth = 3;
      panel.add(addressField, gbc);

      LeftPanel.add(panel);

      JPanel tablePanel = new JPanel(new BorderLayout());
      tablePanel.setBorder(BorderFactory.createTitledBorder("Order Details"));

      String[] columns = {
            "Product",
            "Quantity",
            "Price",
            "Subtotal"
      };

      tableModel = new DefaultTableModel(columns, 0);

      itemTable = new JTable(tableModel);
      itemTable.getTableHeader().setReorderingAllowed(false);
      itemTable.setFont(poppins);

      scrollPane = new JScrollPane(itemTable);
      scrollPane.setPreferredSize(new Dimension(550, 150));

      tablePanel.add(scrollPane, BorderLayout.CENTER);
      LeftPanel.add(tablePanel, BorderLayout.SOUTH);

      // End of Left Panel

      // Right Panel
      JPanel RightPanel = new JPanel(new GridBagLayout());
      GridBagConstraints gbc2 = new GridBagConstraints();

      gbc2.insets = new Insets(10, 10, 10, 10);
      gbc2.gridx = 0;
      gbc2.gridy = 0;
      RightPanel.add(ProductLabel, gbc2);

      // End of Right Panel

      JSplitPane splitPane = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            LeftPanel,
            RightPanel);

      splitPane.setDividerLocation(650);
      splitPane.setEnabled(false);

      add(splitPane);

      LoadFont.applyFont(this.getContentPane(), poppins);

      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setSize(1000, 400);
      setLocationRelativeTo(null);
      setResizable(false);
      setVisible(true);
   }

}