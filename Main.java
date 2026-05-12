import java.util.*;
import Client.Client;

public class Main{
   
   public static void main(String[] args){
      
      Client client = new Client();
      client.establishConnection();
      
      new MarketSystem();
      
   }
   
}