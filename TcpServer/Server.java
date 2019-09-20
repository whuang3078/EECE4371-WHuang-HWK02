import java.io.*;

public class Server {
  private static final int SERVER_PORT = 6789;

  public static void main(String argv[]) throws IOException {
    TcpServer tcpServer = new TcpServer(SERVER_PORT);
    tcpServer.listen();
  }
}
