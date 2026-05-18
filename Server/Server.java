import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Server extends JFrame {
   private JLabel statusLabel;
   private JLabel clientCountLabel;
   private JButton startButton;
   private JButton stopButton;

   private ServerSocket serverSocket;
   private ExecutorService executor;
   private volatile boolean running;
   private int connectedClients;

   public Server() {
      super("Server");
      initComponents();
   }

   private void initComponents() {
      statusLabel = new JLabel("Server Status: Stopped");
      clientCountLabel = new JLabel("Clients Connected: 0");
      startButton = new JButton("Start Server");
      stopButton = new JButton("Stop Server");

      stopButton.setEnabled(false);

      startButton.addActionListener(e -> {
         startServer();
      });

      stopButton.addActionListener(e -> {
         stopServer();
      });

      JPanel panel = new JPanel();
      panel.setLayout(new GridBagLayout());
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(10, 10, 10, 10);
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbc.gridwidth = 2;
      gbc.anchor = GridBagConstraints.WEST;
      panel.add(statusLabel, gbc);

      gbc.gridy = 1;
      panel.add(clientCountLabel, gbc);

      gbc.gridwidth = 1;
      gbc.gridy = 2;
      gbc.gridx = 0;
      panel.add(startButton, gbc);

      gbc.gridx = 1;
      panel.add(stopButton, gbc);

      add(panel);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setSize(350, 180);
      setResizable(false);
      setLocationRelativeTo(null);
   }

   private void startServer() {
      if (running) {
         return;
      }

      try {
         serverSocket = new ServerSocket(5000);
         executor = Executors.newCachedThreadPool();
         running = true;
         connectedClients = 0;
         updateStatus("Server Status: Running");
         updateClientCount();
         startButton.setEnabled(false);
         stopButton.setEnabled(true);

         executor.execute(() -> {
            while (running) {
               try {
                  Socket clientSocket = serverSocket.accept();
                  connectedClients++;
                  updateClientCount();
                  executor.execute(() -> handleClient(clientSocket));
               } catch (IOException ex) {
                  if (running) {
                     updateStatus("Server Status: Error accepting connection");
                  }
               }
            }
         });
      } catch (IOException ex) {
         updateStatus("Server Status: Failed to start");
         JOptionPane.showMessageDialog(this, "Unable to start server: " + ex.getMessage(), "Error",
               JOptionPane.ERROR_MESSAGE);
      }
   }

   private void stopServer() {
      running = false;
      startButton.setEnabled(true);
      stopButton.setEnabled(false);
      updateStatus("Server Status: Stopped");

      if (executor != null) {
         executor.shutdownNow();
      }

      if (serverSocket != null) {
         try {
            serverSocket.close();
         } catch (IOException ignored) {
         }
      }
   }

   private void handleClient(Socket clientSocket) {
      try {
         System.out.println("Client connected: " + clientSocket.getInetAddress());

         BufferedReader in = new BufferedReader(
               new InputStreamReader(clientSocket.getInputStream()));

         String message;

         while ((message = in.readLine()) != null) {
            System.out.println("CLIENT: " + message);
         }

      } catch (IOException ex) {
         System.out.println("Client error: " + ex.getMessage());
      } finally {
         try {
            clientSocket.close();
         } catch (IOException ignored) {
         }

         connectedClients--;
         updateClientCount();

         System.out.println("Client disconnected");
      }
   }

   private void updateStatus(String status) {
      SwingUtilities.invokeLater(() -> statusLabel.setText(status));
   }

   private void updateClientCount() {
      SwingUtilities.invokeLater(() -> clientCountLabel.setText("Clients Connected: " + connectedClients));
   }

   public static void main(String[] args) {
      SwingUtilities.invokeLater(() -> {
         Server ui = new Server();
         ui.setVisible(true);
      });
   }
}
