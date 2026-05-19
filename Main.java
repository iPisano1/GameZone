import javax.swing.JOptionPane;

import Client.Client;

public class Main {

   public static void main(String[] args) {

      Client client = new Client();

      // if (client.establishConnection()) {
         new MarketSystem(client);
      // } else {
      //    JOptionPane.showMessageDialog(null, "Failed to connect to server.", "Connection Error", JOptionPane.ERROR_MESSAGE);
      // }
   }
}