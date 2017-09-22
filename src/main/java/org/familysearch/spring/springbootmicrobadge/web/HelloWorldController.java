package org.familysearch.spring.springbootmicrobadge.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloWorldController {

  private CounterService counterService;

  @Autowired
  public HelloWorldController(@Qualifier("counterService") CounterService counterService) {
    this.counterService = counterService;
  }

  @RequestMapping(method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<String> getHello() {
    counterService.increment("hello.world.metric");
    return new ResponseEntity("Hello!", HttpStatus.OK);
  }
}
