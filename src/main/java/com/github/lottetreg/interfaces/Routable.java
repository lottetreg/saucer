package com.github.lottetreg.interfaces;

import com.github.lottetreg.saucer.HttpResponse;
import com.github.lottetreg.saucer.HttpRequest;

public interface Routable {
  HttpResponse route(HttpRequest request);
}
