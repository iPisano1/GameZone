import Client.Client;
import UI.CustomerUI;

public class MarketSystem {

   private Client client;

   public MarketSystem(Client client) {
      this.client = client;

      new CustomerUI(client); // pass to UI
   }
}