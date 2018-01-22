package org.familysearch.spring.springbootmicrobadge.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import org.familysearch.tf.jsonbind.dto.TfPerson;

@Component
public class TfPersonClient {

  private final SessionHelper sessionHelper;

  public TfPersonClient(@Value("${url.base}") String rootUri,
                        SessionHelper sessionHelper) {
    this.sessionHelper = sessionHelper;
  }

  public TfPerson getPerson(String personId) {
    return new TfPerson();
  }

}
