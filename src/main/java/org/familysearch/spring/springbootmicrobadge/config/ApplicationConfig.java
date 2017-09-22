package org.familysearch.spring.springbootmicrobadge.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"org.familysearch.spring"})
@EnableAutoConfiguration
public class ApplicationConfig  {


}
