package com.github.lottetreg.saucer;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.*;
import java.net.Socket;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class ConnectionTest {
  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();

  @Test
  public void getInputStreamReturnsTheSocketsInputStream() throws IOException {
    @Ignore
    class MockSocket extends Socket {
      private ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{});

      @Override
      public InputStream getInputStream() {
        return this.inputStream;
      }
    }

    Socket socket = new MockSocket();
    Connection connection = new Connection(socket);

    assertEquals(socket.getInputStream(), connection.getInputStream());
  }

  @Test
  public void getOutputStreamReturnsTheSocketsOutputStream() throws IOException {
    @Ignore
    class MockSocket extends Socket {
      private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

      @Override
      public OutputStream getOutputStream() {
        return this.outputStream;
      }
    }

    Socket socket = new MockSocket();
    Connection connection = new Connection(socket);

    assertEquals(socket.getOutputStream(), connection.getOutputStream());
  }

  @Test
  public void getInputStreamThrowsAnException() {
    @Ignore
    class MockSocket extends Socket {
      @Override
      public InputStream getInputStream() throws IOException {
        throw new IOException();
      }
    }

    Connection connection = new Connection(new MockSocket());

    exceptionRule.expect(Connection.FailedToGetInputStream.class);

    connection.getInputStream();
  }

  @Test
  public void getOutputStreamThrowsAnException() {
    @Ignore
    class MockSocket extends Socket {
      @Override
      public OutputStream getOutputStream() throws IOException {
        throw new IOException();
      }
    }

    Connection connection = new Connection(new MockSocket());

    exceptionRule.expect(Connection.FailedToGetOutputStream.class);

    connection.getOutputStream();
  }

  @Test
  public void itClosesTheSocket() {
    Socket socket = new Socket();
    Connection connection = new Connection(socket);

    connection.close();

    assertTrue(socket.isClosed());
  }

  @Test
  public void closeThrowsAnExceptionIfItFailsToCloseTheSocket() {
    @Ignore
    class MockSocket extends Socket {
      @Override
      public void close() throws IOException {
        throw new IOException();
      }
    }

    Connection connection = new Connection(new MockSocket());

    exceptionRule.expect(Connection.FailedToCloseConnection.class);

    connection.close();
  }
}

