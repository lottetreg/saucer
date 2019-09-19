package com.github.lottetreg.saucer;

import java.io.IOException;
import java.net.Socket;

class ServerSocket {
  private java.net.ServerSocket serverSocket;

  ServerSocket(java.net.ServerSocket serverSocket) {
    this.serverSocket = serverSocket;
  }

  Connection acceptConnection() throws IOException {
    Socket socket = this.serverSocket.accept();
    return new Connection(socket);
  }
}
