package org.familysearch.spring.springbootmicrobadge.client;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import org.familysearch.tf.jsonbind.dto.TfPerson;

@Component
public class TfPersonClient {

  private final RestTemplate restTemplate;

  @Autowired
  private HttpServletRequest httpServletRequest;

  public TfPersonClient(RestTemplateBuilder restTemplateBuilder, @Value("${url.base}") String rootUri) {
    restTemplate = restTemplateBuilder
      .rootUri(rootUri)
      .build();
  }

  public TfPerson getPerson(String personId) {
    final String sessionId = getSessionId();
    try {
      return restTemplate.getForObject("/tf/person/{personId}?sessionId={sessionId}",
                                       TfPerson.class, personId, sessionId);
    }
    catch (HttpClientErrorException e) {
      if (HttpStatus.NOT_FOUND.equals(e.getStatusCode())) {
        return null;
      }
      throw e;
    }
  }

  private String getSessionId() {
    return UriComponentsBuilder.fromHttpRequest(new ServletServerHttpRequest(httpServletRequest))
      .build()
      .getQueryParams()
      .getFirst("sessionId");
  }

}
