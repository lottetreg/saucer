package com.github.lottetreg.saucer;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class HttpResponseTest {
  @Test
  public void a200ResponseIsCorrectlyBuiltIntoABytes() {
    HttpResponse response = new HttpResponse.Builder(200)
        .setHeaders(new HashMap<>(Map.of("Header-1", "some value")))
        .setBody("some body to love".getBytes())
        .build();

    byte[] bytes = response.toBytes();

    assertEquals("HTTP/1.0 200 OK\r\nHeader-1: some value\r\n\r\nsome body to love", new String(bytes));
  }

  @Test
  public void a200ResponseWithMultipleHeadersIsCorrectlyBuiltIntoABytes() {
    HttpResponse response = new HttpResponse.Builder(200)
        .setHeaders(new HashMap<>(Map.of("Header-1", "some value", "Header-2", "something else")))
        .setBody("some body to love".getBytes())
        .build();

    byte[] bytes = response.toBytes();

    assertEquals("HTTP/1.0 200 OK\r\nHeader-2: something else\r\nHeader-1: some value\r\n\r\nsome body to love", new String(bytes));
  }

  @Test
  public void a301ResponseIsCorrectlyBuiltIntoABytes() {
    HttpResponse response = new HttpResponse.Builder(301).build();

    byte[] bytes = response.toBytes();

    assertEquals("HTTP/1.0 301 Moved Permanently\r\n\r\n", new String(bytes));
  }

  @Test
  public void a400ResponseIsCorrectlyBuiltIntoABytes() {
    HttpResponse response = new HttpResponse.Builder(400).build();

    byte[] bytes = response.toBytes();

    assertEquals("HTTP/1.0 400 Bad Request\r\n\r\n", new String(bytes));
  }

  @Test
  public void a404ResponseIsCorrectlyBuiltIntoABytes() {
    HttpResponse response = new HttpResponse.Builder(404).build();

    byte[] bytes = response.toBytes();

    assertEquals("HTTP/1.0 404 Not Found\r\n\r\n", new String(bytes));
  }

  @Test
  public void a405ResponseIsCorrectlyBuiltIntoABytes() {
    HttpResponse response = new HttpResponse.Builder(405).build();

    byte[] bytes = response.toBytes();

    assertEquals("HTTP/1.0 405 Method Not Allowed\r\n\r\n", new String(bytes));
  }

  @Test
  public void a500ResponseIsCorrectlyBuiltIntoABytes() {
    HttpResponse response = new HttpResponse.Builder(500).build();

    byte[] bytes = response.toBytes();

    assertEquals("HTTP/1.0 500 Internal Server Error\r\n\r\n", new String(bytes));
  }
}
