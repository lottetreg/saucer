package com.github.lottetreg.saucer;

public interface Routable {
  HttpResponse route(HttpRequest request);
}
