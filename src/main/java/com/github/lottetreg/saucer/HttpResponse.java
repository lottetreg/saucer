package com.github.lottetreg.saucer;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
  private int statusCode;
  private HashMap<String, String> headers;
  private byte[] body;
  private String protocolVersion;
  private String CRLF = "\r\n";

  private Map<Integer, String> statuses = Map.of(
      200, "OK",
      301, "Moved Permanently",
      400, "Bad Request",
      404, "Not Found",
      405, "Method Not Allowed",
      500, "Internal Server Error"
  );

  private HttpResponse(Builder builder) {
    this.statusCode = builder.statusCode;
    this.headers = builder.headers;
    this.body = builder.body;
    this.protocolVersion = builder.protocolVersion;
  }

  public static class Builder {
    int statusCode;
    HashMap<String, String> headers;
    byte[] body;
    String protocolVersion;

    public Builder(int statusCode) {
      this.statusCode = statusCode;
      this.headers = new HashMap<>();
      this.body = new byte[] {};
      this.protocolVersion = "1.0";
    }

    public Builder setBody(byte[] body) {
      this.body = body;
      return this;
    }

    public Builder setHeaders(HashMap<String, String> headers) {
      this.headers = headers;
      return this;
    }

    public HttpResponse build() {
      return new HttpResponse(this);
    }
  }

  byte[] toBytes() {
    byte[] responseData = (initialLine() + joinedHeaders() + this.CRLF).getBytes();
    return concatByteArrays(responseData, getBody());
  }

  public int getStatusCode() {
    return this.statusCode;
  }

  public HashMap<String, String> getHeaders() {
    return this.headers;
  }

  public byte[] getBody() {
    return this.body;
  }

  public String getProtocolVersion() {
    return this.protocolVersion;
  }

  private String initialLine() {
    return getProtocolVersionString() + " " + getStatus() + this.CRLF;
  }

  private String getProtocolVersionString() {
    return "HTTP/" + getProtocolVersion();
  }

  private String getStatus() {
    return getStatusCode() + " " + this.statuses.get(getStatusCode());
  }

  private String joinedHeaders() {
    return getHeaders().keySet().stream()
        .map(key -> key + ": " + getHeaders().get(key) + this.CRLF)
        .reduce("", (header, acc) -> acc + header);
  }

  private byte[] concatByteArrays(byte[] firstArray, byte[] secondArray) {
    byte[] finalArray = new byte[firstArray.length + secondArray.length];

    System.arraycopy(firstArray, 0, finalArray, 0, firstArray.length);
    System.arraycopy(secondArray, 0, finalArray, firstArray.length, secondArray.length);

    return finalArray;
  }
}
