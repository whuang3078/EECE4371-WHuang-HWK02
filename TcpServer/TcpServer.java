import java.io.*;
import java.net.*;
import java.util.*;

public class TcpServer {
  private ServerSocket mServerSocket;
  private Socket mClientSocket;
  private Map <String, String> clientMap = new HashMap<String, String>();

  public TcpServer(int port) throws IOException {
    mServerSocket = new ServerSocket(port);
  }

  public void listen() throws IOException {
    boolean quit = false;
    boolean waitForKey = false;
    boolean waitForValue = false;
    boolean waitForRetrieveKey = false;
    String key = null;
    String value;
    String requestKey;
    String output;

    while (!quit) {
      waitForConnection();

      BufferedReader clientReader = new BufferedReader(new InputStreamReader(mClientSocket.getInputStream()));
      DataOutputStream clientWriter = new DataOutputStream(mClientSocket.getOutputStream());

      String clientMessage = clientReader.readLine();
      System.out.println("received message: " + clientMessage);

      // begin store process by flagging "waitForKey"
      if (clientMessage.equals("store")) {
        output = "Please enter key.";
        clientWriter.writeBytes(output + "\n");
        waitForKey = true;
      }
      // begin retrieve process by flagging "waitForRetrieveKey"
      else if (clientMessage.equals("retrieve")) {
        output = "Please enter key to retrieve its value.";
        clientWriter.writeBytes(output + "\n");
        waitForRetrieveKey = true;
      }
      // if user inputs something irrelevant
      else if (!waitForKey && !waitForValue && !waitForRetrieveKey 
                && !clientMessage.equals("store") && !clientMessage.equals("retrieve")) {

        String response = "Invalid command." + "\n";
        clientWriter.writeBytes(response);
      } else {
        // hold on to user input as key and "waitForValue"
        if (waitForKey) {
          key = clientMessage;
          output = "Please enter value.";
          clientWriter.writeBytes(output + "\n");
          waitForKey = false;
          waitForValue = true;
        }
        // store key/value pair if key is not empty
        else if (waitForValue && (key != null)) {
          value = clientMessage;
          clientMap.put(key, value);
          output = "Successfully stored: " + key + ": " + value;
          clientWriter.writeBytes(output + "\n");
          waitForValue = false;
        }
        // take user input as key to lookup in clientMap
        else if (waitForRetrieveKey) {
          requestKey = clientMessage;
          // if key is not found
          if (!clientMap.containsKey(requestKey)) {
            output = "Invalid Key!";
            clientWriter.writeBytes(output + "\n");
          }
          // return value to key
          else {
            output = requestKey + ": " + clientMap.get(requestKey);
            clientWriter.writeBytes(output + "\n");
          }
          waitForRetrieveKey = false;
        }
      }
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