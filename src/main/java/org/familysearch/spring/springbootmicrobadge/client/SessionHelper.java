package org.familysearch.spring.springbootmicrobadge.client;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class SessionHelper {

  private HttpServletRequest httpServletRequest;

  public SessionHelper(HttpServletRequest httpServletRequest) {
    this.httpServletRequest = httpServletRequest;
  }

  public String getSessionId() {
    return UriComponentsBuilder.fromHttpRequest(new ServletServerHttpRequest(httpServletRequest))
        .build()
        .getQueryParams()
        .getFirst("sessionId");
  }

}
