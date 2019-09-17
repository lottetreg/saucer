package com.github.lottetreg.saucer;

import java.util.Map;

public class HttpResponsePresenter {
  private HttpResponse response;
  private String CRLF = "\r\n";

  private Map<Integer, String> statuses = Map.of(
      200, "OK",
      301, "Moved Permanently",
      400, "Bad Request",
      404, "Not Found",
      405, "Method Not Allowed",
      500, "Internal Server Error"
  );

  HttpResponsePresenter(HttpResponse response) {
    this.response = response;
  }

  public byte[] toBytes() {
    byte[] responseData = (initialLine() + joinedHeaders() + this.CRLF).getBytes();
    return concatByteArrays(responseData, this.response.getBody());
  }

  private String initialLine() {
    return getProtocolVersionString() + " " + getStatus() + this.CRLF;
  }

  private String getProtocolVersionString() {
    return "HTTP/" + this.response.getProtocolVersion();
  }

  private String getStatus() {
    return this.response.getStatusCode() + " " + this.statuses.get(this.response.getStatusCode());
  }

  private String joinedHeaders() {
    return this.response.getHeaders().keySet().stream()
        .map(key -> key + ": " + this.response.getHeaders().get(key) + this.CRLF)
        .reduce("", (header, acc) -> acc + header);
  }

  private byte[] concatByteArrays(byte[] firstArray, byte[] secondArray) {
    byte[] finalArray = new byte[firstArray.length + secondArray.length];

    System.arraycopy(firstArray, 0, finalArray, 0, firstArray.length);
    System.arraycopy(secondArray, 0, finalArray, firstArray.length, secondArray.length);

    return finalArray;
  }
}
