import java.io.*;
import java.net.*;

public class TcpServer {
  private ServerSocket mServerSocket;
  private Socket mClientSocket;

  public TcpServer(int port) throws IOException {
    mServerSocket = new ServerSocket(port);
  }

  public void listen() throws IOException {
    boolean quit = false;
    while (!quit) {
      waitForConnection();

      BufferedReader clientReader = new BufferedReader(new InputStreamReader(mClientSocket.getInputStream()));
      DataOutputStream clientWriter = new DataOutputStream(mClientSocket.getOutputStream());

      String clientMessage = clientReader.readLine();
      System.out.println("  received message: " + clientMessage);

      if (clientMessage.equals("quit")) {
        quit = true;
      }

      String response = "You sent me " + clientMessage.length() + " characters.\n";
      clientWriter.writeBytes(response);

      System.out.println("");
    }
  }

  private void waitForConnection() throws IOException {
    System.out.println("waiting for a connection");
    mClientSocket = mServerSocket.accept();
    System.out.println("connected!");
    InetAddress clientAddress = mClientSocket.getInetAddress();
    System.out.println("  client at: " + clientAddress.toString() + ":" + mClientSocket.getPort());
  }
}
