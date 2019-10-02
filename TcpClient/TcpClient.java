import java.io.*;
import java.net.*;

public class TcpClient {
  private static final String HOST_ADDRESS = "127.0.0.1";
  private static final int PORT = 6789;

  public static void main(String[] args) throws IOException {
    boolean quit = false;
    System.out.println("Please enter a message to send.");

    while (!quit)
    { 
      Socket socket = new Socket(HOST_ADDRESS, PORT);
      DataOutputStream out = new DataOutputStream(socket.getOutputStream());
      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

      BufferedReader userBufferedReader = new BufferedReader(new InputStreamReader(System.in));
      String userIn = userBufferedReader.readLine();
      out.writeBytes(userIn + "\n");

      if (userIn.equals("quit"))
      {
        quit = true;
        System.out.println("SEEya!");
      }

      System.out.println("");
      String responseFromServer = in.readLine();
      System.out.println("Response from server: " + responseFromServer);

      socket.close();
    }
  }
} 