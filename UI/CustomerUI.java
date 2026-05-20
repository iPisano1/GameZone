package UI;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.xml.crypto.Data;

import java.awt.*;
import java.io.*;

import UI.LoadFont;
import UI.data.Products;
import Client.Client;

public class CustomerUI extends JFrame {

   JMenuItem connectItem;
   JMenuItem disconnectItem;

   JLabel welcomeLabel;
   JLabel firstNameLabel;
   JLabel lastNameLabel;
   JLabel phoneLabel;
   JLabel emailLabel;
   JLabel addressLabel;
   JLabel totalLabel;

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

   private Client client;

   private Products[] products;

   public CustomerUI(Client client) {
      super("GameZone");
      this.client = client;
      initComponents();
   }

   public void clearForm() {
      firstNameField.setText("");
      lastNameField.setText("");
      phoneField.setText("");
      emailField.setText("");
      addressField.setText("");
   }

   public void setTotal() {
      double total = 0;

      for (int i = 0; i < tableModel.getRowCount(); i++) {
         total += (Double) tableModel.getValueAt(i, 3);
      }

      totalLabel.setText("<html>Total: &#8369;" + String.format("%.2f", total) + "</html>");
   }

   public double getTotal() {
      double total = 0;

      for (int i = 0; i < tableModel.getRowCount(); i++) {
         total += (Double) tableModel.getValueAt(i, 3);
      }

      return total;
   }

   public void checkConnectionStatus() {
      if (client.getSocket() != null && client.getSocket().isConnected()) {
         connectItem.setEnabled(false);
         disconnectItem.setEnabled(true);
      } else {
         connectItem.setEnabled(true);
         disconnectItem.setEnabled(false);
      }
   }

   private void initComponents() {

      Font poppins = LoadFont.loadPoppins(12f);
      Font poppinsBold = LoadFont.loadPoppinsBold(14f);
      Font poppinsSemi = LoadFont.loadPoppinsSemi(12f);
      products = Products.loadProducts();

      welcomeLabel = new JLabel("Welcome, Customer!");
      welcomeLabel.setFont(poppinsBold);
      firstNameLabel = new JLabel("First Name:");
      firstNameLabel.setFont(poppins);
      lastNameLabel = new JLabel("Last Name:");
      lastNameLabel.setFont(poppins);
      phoneLabel = new JLabel("Phone Number:");
      phoneLabel.setFont(poppins);
      emailLabel = new JLabel("Email:");
      emailLabel.setFont(poppins);
      addressLabel = new JLabel("Address:");
      addressLabel.setFont(poppins);
      ProductLabel = new JLabel("Products");
      ProductLabel.setFont(poppinsBold);

      firstNameField = new JTextField(10);
      lastNameField = new JTextField(10);
      phoneField = new JTextField(10);
      emailField = new JTextField(10);
      addressField = new JTextField(10);

      JMenuBar menuBar = new JMenuBar();

      JMenu fileMenu = new JMenu("File");
      JMenuItem exitItem = new JMenuItem("Exit");

      JMenu connectMenu = new JMenu("Connection");
      connectItem = new JMenuItem("Connect to Server");
      disconnectItem = new JMenuItem("Disconnect from Server");

      JMenu debugMenu = new JMenu("Debug");
      JMenuItem fillClientFormItem = new JMenuItem("Fill Form");
      JMenu setStocksItem = new JMenu("Set All Stocks");
      JMenuItem setStocksto0Item = new JMenuItem("0");
      JMenuItem setStocksTo5Item = new JMenuItem("5");
      JMenuItem setStocksTo10Item = new JMenuItem("10");
      JMenuItem clearAllItem = new JMenuItem("Clear All");

      fileMenu.add(exitItem);
      menuBar.add(fileMenu);

      connectMenu.add(connectItem);
      connectMenu.add(disconnectItem);
      menuBar.add(connectMenu);

      debugMenu.add(fillClientFormItem);
      setStocksItem.add(setStocksto0Item);
      setStocksItem.add(setStocksTo5Item);
      setStocksItem.add(setStocksTo10Item);
      debugMenu.add(setStocksItem);
      debugMenu.add(clearAllItem);
      menuBar.add(debugMenu);

      setJMenuBar(menuBar);

      checkConnectionStatus();

      // Debug Actions
      fillClientFormItem.addActionListener(e -> {
         firstNameField.setText("John");
         lastNameField.setText("Doe");
         phoneField.setText("09123456789");
         emailField.setText("test@gmail.com");
         addressField.setText("Purok 2, Barangay 3, Opaon, Philippines");
      });

      setStocksto0Item.addActionListener(e -> {
         for (Products product : products) {
            product.setStocks(0);
         }
         JOptionPane.showMessageDialog(this, "Stocks updated!", "Stock Update", JOptionPane.INFORMATION_MESSAGE);
      });

      setStocksTo5Item.addActionListener(e -> {
         for (Products product : products) {
            product.setStocks(5);
         }
         JOptionPane.showMessageDialog(this, "Stocks updated!", "Stock Update", JOptionPane.INFORMATION_MESSAGE);
      });

      setStocksTo10Item.addActionListener(e -> {
         for (Products product : products) {
            product.setStocks(10);
         }
         JOptionPane.showMessageDialog(this, "Stocks updated!", "Stock Update", JOptionPane.INFORMATION_MESSAGE);
      });

      clearAllItem.addActionListener(e -> {
         clearForm();
         tableModel.setRowCount(0);
         setTotal();
      });

      // Connection Actions
      connectItem.addActionListener(e -> {
         if (client.establishConnection()) {
            JOptionPane.showMessageDialog(this, "Connected to server!", "Connection Status",
                  JOptionPane.INFORMATION_MESSAGE);
            checkConnectionStatus();
         } else {
            JOptionPane.showMessageDialog(this, "Failed to connect to server.", "Connection Error",
                  JOptionPane.ERROR_MESSAGE);
         }
      });

      disconnectItem.addActionListener(e -> {
         try {
            client.closeConnection();
            JOptionPane.showMessageDialog(this, "Disconnected from server!", "Connection Status",
                  JOptionPane.INFORMATION_MESSAGE);
            checkConnectionStatus();
         } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Failed to disconnect from server.", "Connection Error",
                  JOptionPane.ERROR_MESSAGE);
         }
      });

      // File Actions
      exitItem.addActionListener(e -> {
         try {
            client.closeConnection();
         } catch (IOException ex) {
            // Ignore
         }
         System.exit(0);
      });

      // Left Panel
      JPanel LeftPanel = new JPanel(new BorderLayout(10, 10));

      JPanel formPanel = new JPanel(new GridBagLayout());
      formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
      GridBagConstraints gbc = new GridBagConstraints();

      gbc.insets = new Insets(8, 8, 8, 8);
      gbc.fill = GridBagConstraints.HORIZONTAL;
      gbc.weightx = 1;

      // Title Row
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbc.gridwidth = 4;
      formPanel.add(welcomeLabel, gbc);

      gbc.gridwidth = 1;

      // First Row
      gbc.gridy = 1;
      gbc.gridx = 0;
      formPanel.add(firstNameLabel, gbc);
      gbc.gridx = 1;
      formPanel.add(firstNameField, gbc);

      gbc.gridx = 2;
      formPanel.add(lastNameLabel, gbc);
      gbc.gridx = 3;
      formPanel.add(lastNameField, gbc);

      // Second Row
      gbc.gridy = 2;
      gbc.gridx = 0;
      formPanel.add(phoneLabel, gbc);
      gbc.gridx = 1;
      formPanel.add(phoneField, gbc);

      gbc.gridx = 2;
      formPanel.add(emailLabel, gbc);
      gbc.gridx = 3;
      formPanel.add(emailField, gbc);

      // Third Row
      gbc.gridy = 3;
      gbc.gridx = 0;
      formPanel.add(addressLabel, gbc);
      gbc.gridx = 1;
      gbc.gridwidth = 3;
      formPanel.add(addressField, gbc);

      // Message Panel (for validation errors)
      // JPanel messagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
      // JLabel messageLabel = new JLabel();
      // messageLabel.setForeground(Color.RED);
      // messageLabel.setFont(poppinsSemi);
      // messageLabel.setVisible(false);

      // messagePanel.add(messageLabel);

      // gbc.gridy = 4;
      // gbc.gridx = 0;
      // gbc.gridwidth = 4;
      // formPanel.add(messagePanel, gbc);

      LeftPanel.add(formPanel, BorderLayout.NORTH);

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
      itemTable.setRowHeight(30);

      itemTable.getTableHeader().setFont(poppins);
      itemTable.setFont(poppins);

      DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
      centerRenderer.setHorizontalAlignment(JLabel.CENTER);

      for (int i = 0; i < itemTable.getColumnCount(); i++) {
         itemTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
      }

      scrollPane = new JScrollPane(itemTable);
      scrollPane.setPreferredSize(new Dimension(550, 150));

      tablePanel.add(scrollPane, BorderLayout.CENTER);

      JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

      totalLabel = new JLabel("<html>Total: &#8369;0.00</html>");
      totalLabel.setFont(poppinsBold);

      totalPanel.add(totalLabel);
      tablePanel.add(totalPanel, BorderLayout.SOUTH);
      LeftPanel.add(tablePanel, BorderLayout.CENTER);

      // Bottom Buttons
      JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));

      JButton removeButton = new JButton("Remove Selected");
      removeButton.setFocusPainted(false);
      removeButton.setBackground(Color.decode("#D9C914"));
      removeButton.setForeground(Color.WHITE);
      JButton clearFormButton = new JButton("Clear Form");
      clearFormButton.setFocusPainted(false);
      clearFormButton.setBackground(Color.decode("#D83A3A"));
      clearFormButton.setForeground(Color.WHITE);
      JButton clearTableButton = new JButton("Clear Order");
      clearTableButton.setFocusPainted(false);
      clearTableButton.setBackground(Color.decode("#D83A3A"));
      clearTableButton.setForeground(Color.WHITE);
      removeButton.setForeground(Color.WHITE);
      JButton buyButton = new JButton("Buy");
      buyButton.setFocusPainted(false);
      buyButton.setBackground(Color.decode("#2e7d32"));
      buyButton.setForeground(Color.WHITE);

      removeButton.setFont(poppinsBold);
      clearFormButton.setFont(poppinsBold);
      clearTableButton.setFont(poppinsBold);
      buyButton.setFont(poppinsBold);

      removeButton.addActionListener(e -> {
         int selectedRow = itemTable.getSelectedRow();
         if (selectedRow != -1) {
            tableModel.removeRow(selectedRow);
            setTotal();
         } else {
            JOptionPane.showMessageDialog(this, "Please select an item to remove!", "Error",
                  JOptionPane.ERROR_MESSAGE);
         }
      });

      clearFormButton.addActionListener(e -> {
         int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to clear the form?", "Confirm",
               JOptionPane.YES_NO_OPTION);
         if (response == JOptionPane.YES_OPTION) {
            clearForm();
         } else {
            return;
         }
      });

      clearTableButton.addActionListener(e -> {
         int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to clear the order?", "Confirm",
               JOptionPane.YES_NO_OPTION);
         if (response == JOptionPane.YES_OPTION) {
            tableModel.setRowCount(0);
            setTotal();
         } else {
            return;
         }
      });

      buyButton.addActionListener(e -> {

         String firstName = firstNameField.getText().trim();
         String lastName = lastNameField.getText().trim();
         String phone = phoneField.getText().trim();
         String email = emailField.getText().trim();
         String address = addressField.getText().trim();

         if (firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty()
               || email.isEmpty() || address.isEmpty()) {

            JOptionPane.showMessageDialog(this, "Please fill out all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
         }

         if (!firstName.matches("[a-zA-Z]+") || !lastName.matches("[a-zA-Z]+")) {
            JOptionPane.showMessageDialog(this, "Names can only contain letters!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
         }

         if (phone.length() != 11) {
            JOptionPane.showMessageDialog(this, "Phone number must be 11 digits!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
         }

         boolean isDigit = phone.chars().allMatch(Character::isDigit);
         if (!isDigit) {
            JOptionPane.showMessageDialog(this, "Phone number can only contain digits!", "Error",
                  JOptionPane.ERROR_MESSAGE);
            return;
         }

         if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            JOptionPane.showMessageDialog(this, "Invalid email format!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
         }

         if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Your cart is empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
         }

         if (client.getSocket() == null) {
            JOptionPane.showMessageDialog(this, "Failed to connect to server!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
         }

         for (int i = 0; i < tableModel.getRowCount(); i++) {

            String productName = (String) tableModel.getValueAt(i, 0);
            int qtyBought = (Integer) tableModel.getValueAt(i, 1);

            for (Products product : products) {

               if (product.getName().equals(productName)) {

                  if (product.getStocks() >= qtyBought) {
                     product.decreaseStocks(qtyBought);
                  } else {
                     JOptionPane.showMessageDialog(this,
                           "Not enough stock for " + productName,
                           "Stock Error",
                           JOptionPane.ERROR_MESSAGE);
                     return;
                  }
               }
            }
         }

         try {
            PrintWriter out = new PrintWriter(client.getSocket().getOutputStream(), true);

            StringBuilder order = new StringBuilder();

            order.append("===========================================\n");
            order.append("NAME: ").append(firstName).append(" ").append(lastName).append("\n");
            order.append("PHONE: ").append(phone).append("\n");
            order.append("EMAIL: ").append(email).append("\n");
            order.append("ADDRESS: ").append(address).append("\n");
            order.append("ITEMS:\n");

            for (int i = 0; i < tableModel.getRowCount(); i++) {
               order.append(
                     tableModel.getValueAt(i, 0) + " | " +
                           tableModel.getValueAt(i, 1) + " | " +
                           tableModel.getValueAt(i, 2) + " | " +
                           tableModel.getValueAt(i, 3) + "\n");
            }

            order.append("TOTAL: ").append(getTotal()).append("\n");
            order.append("===========================================");

            out.println(order.toString());

            JOptionPane.showMessageDialog(this,
                  "Thank you for your purchase, " + firstName + "!",
                  "Success",
                  JOptionPane.INFORMATION_MESSAGE);

            clearForm();
            tableModel.setRowCount(0);
            setTotal();

         } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to send order!", "Error", JOptionPane.ERROR_MESSAGE);
         }
      });

      actionPanel.add(removeButton);
      actionPanel.add(clearFormButton);
      actionPanel.add(clearTableButton);
      actionPanel.add(buyButton);

      LeftPanel.add(actionPanel, BorderLayout.SOUTH);

      // End of Left Panel

      // Right Panel
      JPanel RightPanel = new JPanel(new BorderLayout(10, 10));
      RightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

      RightPanel.add(ProductLabel, BorderLayout.NORTH);

      JPanel productsPanel = new JPanel();
      productsPanel.setLayout(new BoxLayout(productsPanel, BoxLayout.Y_AXIS));
      productsPanel.setBackground(Color.WHITE);

      try {
         for (Products product : products) {

            JPanel card = new JPanel(new BorderLayout(10, 10));
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
            card.setBorder(BorderFactory.createCompoundBorder(
                  BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                  BorderFactory.createEmptyBorder(10, 10, 10, 10)));

            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

            JLabel nameLabel = new JLabel(product.getName());
            nameLabel.setFont(poppinsSemi);

            JLabel priceLabel = new JLabel("<html>&#8369;" + product.getPrice() + "</html>");
            priceLabel.setFont(poppins);

            infoPanel.add(nameLabel);
            infoPanel.add(Box.createVerticalStrut(5));
            infoPanel.add(priceLabel);

            JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

            JLabel qtyLabel = new JLabel("Qty:");
            qtyLabel.setFont(poppins);

            JSpinner qtySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
            qtySpinner.setPreferredSize(new Dimension(60, 30));

            JButton addButton = new JButton("Add to Cart");
            addButton.setFont(poppinsSemi);
            addButton.setFocusPainted(false);
            addButton.setBackground(Color.decode("#2e7d32"));
            addButton.setForeground(Color.WHITE);

            addButton.addActionListener(e -> {

               int qty = (Integer) qtySpinner.getValue();
               double subtotal = qty * product.getPrice();

               boolean found = false;

               for (int i = 0; i < tableModel.getRowCount(); i++) {

                  String itemName = (String) tableModel.getValueAt(i, 0);

                  if (itemName.equals(product.getName())) {

                     int existingQty = (Integer) tableModel.getValueAt(i, 1);
                     double price = Double.parseDouble(tableModel.getValueAt(i, 2).toString());

                     int newQty = existingQty + qty;
                     double newSubtotal = price * newQty;

                     if (newQty > product.getStocks()) {
                        JOptionPane.showMessageDialog(this,
                              "Only " + product.getStocks() + " stocks available!",
                              "Stock Error",
                              JOptionPane.ERROR_MESSAGE);
                        qtySpinner.setValue(1);
                        return;
                     }

                     tableModel.setValueAt(newQty, i, 1);
                     tableModel.setValueAt(newSubtotal, i, 3);

                     found = true;
                     break;
                  }
               }

               if (!found) {

                  if (qty > product.getStocks()) {
                     JOptionPane.showMessageDialog(this,
                           "Only " + product.getStocks() + " stocks available!",
                           "Stock Error",
                           JOptionPane.ERROR_MESSAGE);
                     qtySpinner.setValue(1);
                     return;
                  }

                  tableModel.addRow(new Object[] {
                        product.getName(),
                        qty,
                        product.getPrice(),
                        subtotal
                  });
               }

               setTotal();
               qtySpinner.setValue(1);
            });

            controlPanel.add(qtyLabel);
            controlPanel.add(qtySpinner);
            controlPanel.add(addButton);

            card.add(infoPanel, BorderLayout.CENTER);
            card.add(controlPanel, BorderLayout.SOUTH);

            productsPanel.add(card);
            productsPanel.add(Box.createVerticalStrut(10));
         }
      } catch (Exception e) {
         System.out.println("Error loading products: " + e.getMessage());
      }

      JScrollPane productScroll = new JScrollPane(productsPanel);
      productScroll.setBorder(null);
      productScroll.getVerticalScrollBar().setUnitIncrement(10);

      RightPanel.add(productScroll, BorderLayout.CENTER);

      // End of Right Panel

      JSplitPane splitPane = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            LeftPanel,
            RightPanel);

      splitPane.setDividerLocation(650);
      splitPane.setEnabled(false);

      add(splitPane);

      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setSize(1000, 600);
      setLocationRelativeTo(null);
      setResizable(false);
      setVisible(true);
   }

}