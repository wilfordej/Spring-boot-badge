package org.familysearch.spring.springbootmicrobadge.web;

import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.familysearch.spring.springbootmicrobadge.schema.Metrics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MetricsTest extends BaseComponent {

  @Test
  public void getMetricsTest() {
    ResponseEntity<Metrics> entity = getRestTemplate().getForEntity(getMetricsUrl(), Metrics.class);

    assertEquals(HttpStatus.OK, entity.getStatusCode());
    assertTrue(entity.getBody().getMem() > 100);
  }

  @Test
  public void getCustomMetricsTest() throws InterruptedException {
    HttpHeaders headers = new HttpHeaders();
    headers.setCacheControl("no-cache");
    HttpEntity<String> httpEntity = new HttpEntity<String>("parameters", headers);

    // ToDo: Fix the weird timing with calling the health and it updating the metrics.
    ResponseEntity<Metrics> entity = getRestTemplate().getForEntity(getMetricsUrl(), Metrics.class);
    Long numMetricRequests = entity.getBody().getHelloWorldMetric() == null ? 0 : entity.getBody().getHelloWorldMetric();
    getRestTemplate().exchange(getHelloWorldUrl(), HttpMethod.GET, httpEntity, String.class);
    Thread.sleep(500);
    getRestTemplate().exchange(getHelloWorldUrl(), HttpMethod.GET, httpEntity, String.class);
    Thread.sleep(500);
    getRestTemplate().exchange(getHelloWorldUrl(), HttpMethod.GET, httpEntity, String.class);
    Thread.sleep(500);

    Metrics latestMetrics = getRestTemplate().getForEntity(getMetricsUrl(), Metrics.class).getBody();
    assertEquals(HttpStatus.OK, entity.getStatusCode());
    assertTrue(latestMetrics.getHelloWorldMetric() > numMetricRequests);
  }

  private String getMetricsUrl() {
    return urlFactoryForTesting.getMgmtUrl() + "/metrics";
  }

  private String getHelloWorldUrl() {
    return urlFactoryForTesting.getTestUrl() + "/hello";
  }
}