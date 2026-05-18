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
   private DataInputStream in = null;
   private DataOutputStream out = null;

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

   public void calculateTotal() {
      double total = 0;

      for (int i = 0; i < tableModel.getRowCount(); i++) {
         total += (Double) tableModel.getValueAt(i, 3);
      }

      totalLabel.setText("<html>Total: &#8369;" + String.format("%.2f", total) + "</html>");
   }

   private void initComponents() {

      Font poppins = LoadFont.loadPoppins(12f);
      Font poppinsBold = LoadFont.loadPoppinsBold(14f);
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

      JButton clearFormButton = new JButton("Clear Form");
      JButton clearTableButton = new JButton("Clear Order");
      JButton buyButton = new JButton("Buy");

      clearFormButton.setFont(poppinsBold);
      clearTableButton.setFont(poppinsBold);
      buyButton.setFont(poppinsBold);

      clearFormButton.addActionListener(e -> {
         clearForm();
      });

      clearTableButton.addActionListener(e -> {
         tableModel.setRowCount(0);
         calculateTotal();
      });

      buyButton.addActionListener(e -> {

         if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Your cart is empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
         }

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

         try {
            PrintWriter out = new PrintWriter(client.getSocket().getOutputStream(), true);

            StringBuilder order = new StringBuilder();

            order.append("ORDER\n");
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
            
            order.append("TOTAL: ").append(totalLabel.getText().replace("Total: ", "")).append("\n");

            out.println(order.toString());

            JOptionPane.showMessageDialog(this,
                  "Thank you for your purchase, " + firstName + "!",
                  "Success",
                  JOptionPane.INFORMATION_MESSAGE);

            clearForm();
            tableModel.setRowCount(0);
            calculateTotal();

         } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to send order!", "Error", JOptionPane.ERROR_MESSAGE);
         }
      });

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
            nameLabel.setFont(poppinsBold);

            JLabel priceLabel = new JLabel("<html>&#8369;" + product.getPrice() + "</html>");
            priceLabel.setFont(poppins);

            infoPanel.add(nameLabel);
            infoPanel.add(Box.createVerticalStrut(5));
            infoPanel.add(priceLabel);

            JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

            JSpinner qtySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
            qtySpinner.setPreferredSize(new Dimension(60, 30));

            JButton addButton = new JButton("Add to Cart");

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

                     tableModel.setValueAt(newQty, i, 1);
                     tableModel.setValueAt(newSubtotal, i, 3);

                     found = true;
                     break;
                  }
               }

               if (!found) {

                  tableModel.addRow(new Object[] {
                        product.getName(),
                        qty,
                        product.getPrice(),
                        subtotal
                  });
               }

               calculateTotal();
               qtySpinner.setValue(1);
            });

            controlPanel.add(new JLabel("Qty:"));
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