package org.familysearch.spring.springbootmicrobadge.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import org.familysearch.tf.jsonbind.dto.TfPerson;

@Component
public class TfPersonClient {

  private final SessionHelper sessionHelper;
  private RestTemplate restTemplate;

  public TfPersonClient(@Value("${url.base}") String rootUri,
                        SessionHelper sessionHelper,
                        RestTemplateBuilder restTemplateBuilder) {
    this.sessionHelper = sessionHelper;
    restTemplate = restTemplateBuilder.rootUri(rootUri).build();
  }

  public TfPerson getPerson(String personId) {
    final String sessionId = sessionHelper.getSessionId();
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

}
