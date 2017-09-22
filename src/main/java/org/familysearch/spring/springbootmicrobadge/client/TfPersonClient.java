package org.familysearch.spring.springbootmicrobadge.client;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import org.familysearch.tf.jsonbind.dto.TfPerson;

@Component
public class TfPersonClient {

  private final UrlFactory urlFactory;
  private final RestTemplate restTemplate;

  @Autowired
  private HttpServletRequest httpServletRequest;

  @Autowired
  public TfPersonClient(RestTemplateBuilder restTemplateBuilder, UrlFactory urlFactory) {
    restTemplate = restTemplateBuilder.build();
    this.urlFactory = urlFactory;
  }

  public TfPerson getPerson(String personId) {
    return  restTemplate.getForObject(urlFactory.getBaseUrl() + "/tf/person/{personId}?sessionId=" + getSessionId(), TfPerson.class, personId);
  }

  private String getSessionId() {
    return UriComponentsBuilder.fromHttpRequest(new ServletServerHttpRequest(httpServletRequest)).build().getQueryParams().getFirst("sessionId");
  }

}
