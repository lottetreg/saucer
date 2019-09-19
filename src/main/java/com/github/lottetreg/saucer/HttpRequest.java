package com.github.lottetreg.saucer;

import java.util.HashMap;

public class HttpRequest {
  private String method;
  private String path;
  private String protocolVersion;
  private HashMap<String, String> headers;
  private String body;

  public HttpRequest(String method, String path, String protocolVersion, HashMap<String, String> headers, String body) {
    this.method = method;
    this.path = path;
    this.protocolVersion = protocolVersion;
    this.headers = headers;
    this.body = body;
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

  public HashMap<String, String> getHeaders() {
    return this.headers;
  }

  public String getBody() {
    return this.body;
  }

  public String getHeader(String headerName) {
    return getHeaders().get(headerName);
  }
}

