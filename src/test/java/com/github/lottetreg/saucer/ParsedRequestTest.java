package com.github.lottetreg.saucer;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.*;

public class ParsedRequestTest {
  @Rule
  public ExpectedException exceptionRule = ExpectedException.none();

  @Ignore
  class MockOut implements Outable {
    boolean receivedPrintln;
    String message;

    @Override
    public void println(String message) {
      this.receivedPrintln = true;
      this.message = message;
    }
  }

  @Test
  public void itCorrectlyParsesAValidRequest() throws IOException {
    ByteArrayInputStream inputStream = new RequestBuilder()
        .setMethod("GET")
        .setPath("/")
        .setVersion("HTTP/1.0")
        .addHeader("Valid-Header: something")
        .toInputStream();

    ParsedRequest parsedRequest = new ParsedRequest(inputStream);

    assertEquals("GET", parsedRequest.getMethod());
    assertEquals("/", parsedRequest.getPath());
    assertEquals("HTTP/1.0", parsedRequest.getProtocolVersion());
    assertEquals("something", parsedRequest.getHeaders().get("Valid-Header"));
  }

  @Test
  public void itSetsEmptyHeadersOnTheHeadersField() throws IOException {
    ByteArrayInputStream inputStream = new RequestBuilder()
        .setMethod("GET")
        .setPath("/")
        .setVersion("HTTP/1.0")
        .addHeader("Empty-Header: ")
        .toInputStream();

    ParsedRequest parsedRequest = new ParsedRequest(inputStream);

    assertEquals(" ", parsedRequest.getHeaders().get("Empty-Header"));
  }

  @Test
  public void itLogsAMessageAndDoesNotSetInvalidHeadersOnTheHeadersField() throws IOException {
    ByteArrayInputStream inputStream = new RequestBuilder()
        .setMethod("GET")
        .setPath("/")
        .setVersion("HTTP/1.0")
        .addHeader("Invalid-Header")
        .toInputStream();
    MockOut mockOut = new MockOut();

    ParsedRequest parsedRequest = new ParsedRequest(inputStream, mockOut);

    assertTrue(mockOut.receivedPrintln);
    assertEquals("Invalid header: Invalid-Header", mockOut.message);
    assertNull(parsedRequest.getHeaders().get("Invalid-Header"));
  }

  @Test
  public void itThrowsAnExceptionIfTheInitialLineIsEmpty() throws IOException {
    String request = "\r\n\r\n";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(request.getBytes());

    exceptionRule.expect(ParsedRequest.BadRequest.class);
    exceptionRule.expectMessage("Incorrectly formatted initial line: ");

    new ParsedRequest(inputStream);
  }

  @Test
  public void itThrowsAnExceptionIfTheInitialLineHasOnlyOneElement() throws IOException {
    String request = "GET \r\n\r\n";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(request.getBytes());

    exceptionRule.expect(ParsedRequest.BadRequest.class);
    exceptionRule.expectMessage("Incorrectly formatted initial line: GET");

    new ParsedRequest(inputStream);
  }

  @Test
  public void itThrowsAnExceptionIfTheInitialLineHasOnlyTwoElements() throws IOException {
    String request = "GET / \r\n\r\n";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(request.getBytes());

    exceptionRule.expect(ParsedRequest.BadRequest.class);
    exceptionRule.expectMessage("Incorrectly formatted initial line: GET /");

    new ParsedRequest(inputStream);
  }

  @Test
  public void itDoesNotReadTheContentIfTheContentLengthIsMissing() throws IOException {
    ByteArrayInputStream inputStream = new RequestBuilder()
        .setMethod("GET")
        .setPath("/")
        .setBody("some body to love")
        .toInputStream();

    ParsedRequest parsedRequest = new ParsedRequest(inputStream);

    assertEquals("", parsedRequest.readContent());
  }

  @Test
  public void itLogsAMessageIfTheContentLengthIsNotANumber() throws IOException {
    ByteArrayInputStream inputStream = new RequestBuilder()
        .setMethod("GET")
        .setPath("/")
        .addHeader("Content-Length: NaN")
        .setBody("some body to love")
        .toInputStream();
    MockOut mockOut = new MockOut();

    new ParsedRequest(inputStream, mockOut).readContent();

    assertTrue(mockOut.receivedPrintln);
    assertEquals("Invalid 'Content-Length' in headers: NaN", mockOut.message);
  }
}
