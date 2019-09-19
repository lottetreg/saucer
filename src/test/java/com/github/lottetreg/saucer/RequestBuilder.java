package com.github.lottetreg.saucer;

import java.io.ByteArrayInputStream;

class RequestBuilder {
  private String CRLF = "\r\n";

  private String method;
  private String path;
  private String version;
  private String headers;
  private String body;

  RequestBuilder() {
    this.method = "";
    this.path = "";
    this.version = "HTTP/1.0";
    this.headers = "";
    this.body = "";
  }

  RequestBuilder setMethod(String method) {
    this.method = method;
    return this;
  }

  RequestBuilder setPath(String path) {
    this.path = path;
    return this;
  }

  RequestBuilder setVersion(String version) {
    this.version = version;
    return this;
  }

  RequestBuilder addHeader(String header) {
    this.headers = this.headers.concat(header + this.CRLF);
    return this;
  }

  RequestBuilder setBody(String body) {
    this.body = body;
    return this;
  }

  ByteArrayInputStream toInputStream() {
    String request = this.method + " " + this.path + " " + this.version + this.CRLF +
        this.headers +
        this.CRLF +
        this.body;

    return new ByteArrayInputStream(request.getBytes());
  }
}
