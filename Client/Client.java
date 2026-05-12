package Client;

import java.net.*;

public class Client{
   
   private Socket socket = null;
   
   public boolean establishConnection(){
      try{
      
         socket = new Socket("127.0.0.1", 5000);

         System.out.println("Connected to server");
         
         return true;
         
      }catch(ConnectException ce){
         System.out.println("Connection Error: Failed to Connect to Server.");
         return false;
         
      }catch(Exception e){
         System.out.println(e);
         return false;
      }
   }
   
}