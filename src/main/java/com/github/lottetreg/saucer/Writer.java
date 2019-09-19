package com.github.lottetreg.saucer;

import java.io.IOException;
import java.io.OutputStream;

class Writer {
  void write(Connectionable connection, byte[] output) {
    try {
      OutputStream outputStream = connection.getOutputStream();
      outputStream.write(output);
      outputStream.flush();
    } catch (Connection.FailedToGetOutputStream | IOException e) {
      throw new FailedToWriteToConnection(e);
    }
  }

  static class FailedToWriteToConnection extends RuntimeException {
    FailedToWriteToConnection(Throwable cause) {
      super(cause);
    }
  }
}
