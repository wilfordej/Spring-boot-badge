package org.familysearch.spring.springbootmicrobadge.client;

import javax.servlet.http.HttpServletRequest;

public class SessionHelperForTesting extends SessionHelper {
  public SessionHelperForTesting(HttpServletRequest httpServletRequest) {
    super(httpServletRequest);
  }

  @Override
  public String getSessionId() {
    return "sessionIdForTesting";
  }
}
