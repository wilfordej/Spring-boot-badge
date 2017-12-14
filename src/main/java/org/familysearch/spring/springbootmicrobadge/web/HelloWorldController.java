package org.familysearch.spring.springbootmicrobadge.web;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloWorldController {

  private final CounterService counterService;

  public HelloWorldController(@Qualifier("counterService") CounterService counterService) {
    this.counterService = counterService;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> getHello() {
    counterService.increment("hello.world.metric");
    return ResponseEntity.ok("Hello!");
  }
}
