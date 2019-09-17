package com.github.lottetreg.saucer;

import java.io.IOException;
import java.util.HashMap;

public class HttpRequest {
  private Outable out;
  private String method;
  private String path;
  private String protocolVersion;
  private HashMap<String, String> headers;
  private String body;

  HttpRequest(ParsedRequest parsedRequest) throws IOException {
    this.out = new Out();
    setFieldsFromParsedRequest(parsedRequest);
  }

  HttpRequest(ParsedRequest parsedRequest, Outable out) throws IOException {
    this.out = out;
    setFieldsFromParsedRequest(parsedRequest);
  }

  private void setFieldsFromParsedRequest(ParsedRequest parsedRequest) throws IOException {
    this.method = parsedRequest.getMethod();
    this.path = parsedRequest.getPath();
    this.protocolVersion = parsedRequest.getVersion();
    this.headers = parsedRequest.getHeaders();
    this.body = parsedRequest.getStreamReader().readNChars(getContentLength());
  }

  public String getMethod() {
    return this.method;
  }

  public String getPath() {
    return this.path;
  }

  public String getProtocolVersion() {
    return this.protocolVersion;
  }

  String getHeader(String headerName) {
    return getHeaders().get(headerName);
  }

  public HashMap<String, String> getHeaders() {
    return this.headers;
  }

  public String getBody() {
    return this.body;
  }

  int getContentLength() {
    String contentLength = this.headers.getOrDefault(
        "Content-Length",
        "0");
    try {
      return Integer.valueOf(contentLength);
    } catch (NumberFormatException e) {
      this.out.println("Invalid Content-Length: " + contentLength);
      return 0;
    }
  }
}

