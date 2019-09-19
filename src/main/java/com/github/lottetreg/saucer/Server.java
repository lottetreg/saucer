package com.github.lottetreg.saucer;

import java.io.IOException;

class Server implements Runnable {
  private Connection connection;
  private Routable router;

  Server(Connection connection, Routable router) {
    this.connection = connection;
    this.router = router;
  }

  @Override
  public void run() {
    HttpResponse response = new HttpResponse.Builder(500).build();

    try {
      ParsedRequest parsedRequest = new ParsedRequest(connection.getInputStream(), new Out());
      HttpRequest request = newHttpRequestFromParsedRequest(parsedRequest);
      response = router.route(request);

    } catch (ParsedRequest.BadRequest e) {
      e.printStackTrace();
      response = new HttpResponse.Builder(400).build();

    } catch (Throwable e) {
      e.printStackTrace();

    } finally {
      writeToConnection(response.toBytes());
    }
  }

  private HttpRequest newHttpRequestFromParsedRequest(ParsedRequest parsedRequest) throws IOException {
    return new HttpRequest(
        parsedRequest.getMethod(),
        parsedRequest.getPath(),
        parsedRequest.getProtocolVersion(),
        parsedRequest.getHeaders(),
        parsedRequest.readContent()
    );
  }

  private void writeToConnection(byte[] response) {
    try {
      new Writer().write(this.connection, response);
    } catch (Throwable e) {
      e.printStackTrace();
    } finally {
      this.connection.close();
    }
  }
}

