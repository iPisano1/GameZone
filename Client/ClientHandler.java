package Client;

import java.net.Socket;

public class ClientHandler {

   private Socket socket;

   public ClientHandler(Socket socket) {
      this.socket = socket;
   }

}