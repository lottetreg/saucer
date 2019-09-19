package com.github.lottetreg.saucer;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ParsedRequest {
  private Outable out;
  private StreamReader streamReader;
  private String method;
  private String path;
  private String version;
  private HashMap<String, String> headers;

  ParsedRequest(InputStream inputStream) throws IOException {
    this.out = new Out();
    parseRequest(inputStream);
  }

  ParsedRequest(InputStream inputStream, Outable out) throws IOException {
    this.out = out;
    parseRequest(inputStream);
  }

  private void parseRequest(InputStream inputStream) throws IOException {
    this.streamReader = new StreamReader(inputStream);
    String initialLine = this.streamReader.readLine();
    Matcher parsedInitialLine = parseInitialLine(initialLine);

    try {
      this.method = parsedInitialLine.group("method");
      this.path = parsedInitialLine.group("path");
      this.version = parsedInitialLine.group("version");

    } catch (IllegalStateException e) {
      throw new BadRequest("Incorrectly formatted initial line: " + initialLine, e);
    }

    this.headers = parseHeaders(this.streamReader.readLinesUntilEmptyLine());
  }

  String getMethod() {
    return this.method;
  }

  String getPath() {
    return this.path;
  }

  String getProtocolVersion() {
    return this.version;
  }

  HashMap<String, String> getHeaders() {
    return this.headers;
  }

  String readContent() throws IOException {
    return this.streamReader.readNChars(getContentLength());
  }

  private Matcher parseInitialLine(String initialLine) {
    Pattern pattern = Pattern.compile("^(?<method>\\S+)\\s+(?<path>\\S+)\\s+(?<version>\\S+)$");
    Matcher matcher = pattern.matcher(initialLine);
    matcher.find();

    return matcher;
  }

  private HashMap<String, String> parseHeaders(List<String> headers) {
    HashMap<String, String> validHeaders = new HashMap<>();

    for (String header : headers) {
      Pattern pattern = Pattern.compile("^(?<key>\\S+):\\s*(?<value>.+)");
      Matcher matcher = pattern.matcher(header);
      matcher.find();

      try {
        validHeaders.put(matcher.group("key"), matcher.group("value"));
      } catch (IllegalStateException e) {
        this.out.println("Invalid header: " + header);
      }
    }

    return validHeaders;
  }

  private int getContentLength() {
    String contentLength = getHeaders().getOrDefault(
        "Content-Length",
        "0");
    try {
      return Integer.valueOf(contentLength);
    } catch (NumberFormatException e) {
      this.out.println("Invalid 'Content-Length' in headers: " + contentLength);
      return 0;
    }
  }

  static class BadRequest extends RuntimeException {
    BadRequest(String message, Throwable cause) {
      super(message, cause);
    }
  }
}
