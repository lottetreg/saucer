package com.github.lottetreg.saucer;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class HttpRequestTest {
  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();

  @Test
  public void itIsInitializedWithTheCorrectObjects() {
    HttpRequest request = new HttpRequest(
        "GET",
        "/",
        "HTTP/1.0",
        new HashMap<>(Map.of("Content-Length", "17")),
        "some body to love"
    );

    assertEquals("GET", request.getMethod());
    assertEquals("/", request.getPath());
    assertEquals("HTTP/1.0", request.getProtocolVersion());
    assertEquals("17", request.getHeader("Content-Length"));
    assertEquals("some body to love", request.getBody());
  }

  @Test
  public void getHeaderReturnsNullIfTheHeaderDoesNotExist() {
    HttpRequest request = new HttpRequest(
        "GET",
        "/",
        "HTTP/1.0",
        new HashMap<>(),
        "some body to love"
    );

    assertNull(request.getHeader("Header 1"));
  }
}

