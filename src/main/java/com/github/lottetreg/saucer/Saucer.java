package com.github.lottetreg.saucer;

import com.github.lottetreg.interfaces.Routable;

import java.io.IOException;

public class Saucer {
  public void start(int portNumber, Routable router) throws IOException {
    ServerSocket serverSocket = new ServerSocket(new java.net.ServerSocket(portNumber));
    Connection connection;

    while ((connection = serverSocket.acceptConnection()) != null) {
      Thread thread = new Thread(new Server(connection, router));
      thread.start();
    }
  }
}
