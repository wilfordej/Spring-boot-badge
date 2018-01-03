package org.familysearch.spring.springbootmicrobadge.web;

import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.familysearch.spring.springbootmicrobadge.schema.Metrics;

import static org.assertj.core.api.Assertions.assertThat;

public class MetricsTest extends BaseComponent {

  @Test
  public void getMetricsTest() {
    ResponseEntity<Metrics> responseEntity = restTemplate.getForEntity(getMetricsUrl(), Metrics.class);

    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(responseEntity.getBody().getMem()).isGreaterThan(100);
  }

  @Test
  public void getCustomMetricsTest() throws InterruptedException {
    HttpHeaders headers = new HttpHeaders();
    headers.setCacheControl("no-cache");
    HttpEntity<String> httpEntity = new HttpEntity<>("parameters", headers);

    ResponseEntity<Metrics> responseEntity = restTemplate.getForEntity(getMetricsUrl(), Metrics.class);

    Long numMetricRequests = responseEntity.getBody().getHelloWorldMetric() == null ? 0 : responseEntity.getBody().getHelloWorldMetric();
    restTemplate.exchange(getHelloWorldUrl(), HttpMethod.GET, httpEntity, String.class);
    restTemplate.exchange(getHelloWorldUrl(), HttpMethod.GET, httpEntity, String.class);
    restTemplate.exchange(getHelloWorldUrl(), HttpMethod.GET, httpEntity, String.class);

    ResponseEntity<Metrics> latestMetricsResponseEntity = restTemplate.getForEntity(getMetricsUrl(), Metrics.class);

    assertThat(latestMetricsResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(latestMetricsResponseEntity.getBody().getHelloWorldMetric()).isGreaterThan(numMetricRequests);
  }

  private String getMetricsUrl() {
    return urlFactoryForTesting.getMgmtUrl() + "/metrics";
  }

  private String getHelloWorldUrl() {
    return urlFactoryForTesting.getTestUrl() + "/hello";
  }
}
