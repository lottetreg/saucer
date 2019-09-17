package com.github.lottetreg.saucer;

import com.github.lottetreg.saucer.support.RequestStringBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

class RequestHelpers {
  static HttpRequest buildHTTPRequest(String method, String path) throws IOException {
    String request = new RequestStringBuilder()
        .setMethod(method)
        .setPath(path)
        .build();

    ByteArrayInputStream inputStream = new ByteArrayInputStream(request.getBytes());
    ParsedRequest parsedRequest = new ParsedRequest(inputStream);

    return new HttpRequest(parsedRequest);
  }

  static HttpRequest buildHTTPRequest(String method, String path, List<String> headers) throws IOException {
    RequestStringBuilder requestBuilder = new RequestStringBuilder()
        .setMethod(method)
        .setPath(path);

    headers.forEach(header -> {
      requestBuilder.addHeader(header);
    });

    String request = requestBuilder.build();
    ByteArrayInputStream inputStream = new ByteArrayInputStream(request.getBytes());
    ParsedRequest parsedRequest = new ParsedRequest(inputStream);

    return new HttpRequest(parsedRequest);
  }
}

