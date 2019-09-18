package com.github.lottetreg.saucer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

class Connection implements Connectionable {
  private Socket socket;

  Connection(Socket socket) {
    this.socket = socket;
  }

  public InputStream getInputStream() {
    try {
      return this.socket.getInputStream();
    } catch (IOException e) {
      throw new FailedToGetInputStream(e);
    }
  }

  public OutputStream getOutputStream() {
    try {
      return this.socket.getOutputStream();
    } catch (IOException e) {
      throw new FailedToGetOutputStream(e);
    }
  }

  void close() {
    try {
      this.socket.close();
    } catch (IOException e) {
      throw new FailedToCloseConnection(e);
    }
  }

  static class FailedToGetInputStream extends RuntimeException {
    FailedToGetInputStream(Throwable cause) {
      super(cause);
    }
  }

  static class FailedToGetOutputStream extends RuntimeException {
    FailedToGetOutputStream(Throwable cause) {
      super(cause);
    }
  }

  static class FailedToCloseConnection extends RuntimeException {
    FailedToCloseConnection(Throwable cause) {
      super(cause);
    }
  }
}

