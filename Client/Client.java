package Client;

import java.io.IOException;
import java.net.*;

public class Client {

   private Socket socket;

   public boolean establishConnection() {
      try {
         socket = new Socket("127.0.0.1", 5000);
         return true;

      } catch (ConnectException ce) {
         return false;
      } catch (Exception e) {
         System.out.println(e);
         return false;
      }
   }

   public void closeConnection() throws IOException {
      if (socket != null && !socket.isClosed()) {
         socket.close();
         socket = null;
      }
   }

   public Socket getSocket() {
      return socket;
   }
}