package com.github.lottetreg.saucer;

import java.util.HashMap;

public class HttpResponse {
  private int statusCode;
  private HashMap<String, String> headers;
  private byte[] body;
  private String protocolVersion;

  HttpResponse(Builder builder) {
    this.statusCode = builder.statusCode;
    this.headers = builder.headers;
    this.body = builder.body;
    this.protocolVersion = builder.protocolVersion;
  }

  int getStatusCode() {
    return this.statusCode;
  }

  HashMap<String, String> getHeaders() {
    return this.headers;
  }

  byte[] getBody() {
    return this.body;
  }

  String getProtocolVersion() {
    return this.protocolVersion;
  }

  public static class Builder {
    public int statusCode;
    public HashMap<String, String> headers;
    public byte[] body;
    public String protocolVersion;

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

    public Builder setHeaders(HashMap headers) {
      this.headers = headers;
      return this;
    }

    public HttpResponse build() {
      return new HttpResponse(this);
    }
  }
}
