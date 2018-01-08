package org.familysearch.spring.springbootmicrobadge.schema;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Metrics {
  private Long mem;
  private Long processors;
  private Long threads;
  private Long httpSessionsActive;
  private Long helloWorldMetric;

  public Long getMem() {
    return mem;
  }

  public void setMem(Long mem) {
    this.mem = mem;
  }

  public Long getProcessors() {
    return processors;
  }

  public void setProcessors(Long processors) {
    this.processors = processors;
  }

  public Long getThreads() {
    return threads;
  }

  public void setThreads(Long threads) {
    this.threads = threads;
  }

  @JsonProperty("httpsessions.active")
  public Long getHttpSessionsActive() {
    return httpSessionsActive;
  }

  public void setHttpSessionsActive(Long httpSessionsActive) {
    this.httpSessionsActive = httpSessionsActive;
  }

  @JsonProperty("counter.status.200.hello")
  public Long getHelloWorldMetric() {
    return helloWorldMetric;
  }

  public void setHelloWorldMetric(Long helloWorldMetric) {
    this.helloWorldMetric = helloWorldMetric;
  }
}
