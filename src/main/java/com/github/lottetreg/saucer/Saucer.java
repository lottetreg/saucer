package com.github.lottetreg.saucer;

import java.io.IOException;

public class Saucer {
  public void start(int portNumber, Respondable servlet) throws IOException {
    ServerSocket serverSocket = new ServerSocket(new java.net.ServerSocket(portNumber));
    Connection connection;

    while ((connection = serverSocket.acceptConnection()) != null) {
      Thread thread = new Thread(new Server(connection, servlet));
      thread.start();
    }
  }
}
