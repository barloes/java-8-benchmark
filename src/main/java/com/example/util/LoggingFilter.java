package com.example.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;

@Component
public class LoggingFilter implements WebFilter {
  private final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    Instant startTime = Instant.now();
    logger.info("Request URL: {} - Request Method: {}", exchange.getRequest().getURI(), exchange.getRequest().getMethod());

    return chain.filter(exchange).doFinally(signalType ->
      {
      Instant endTime = Instant.now();
      Duration duration = Duration.between(startTime, endTime);
      logger.info("API call completed in {} ms", duration.toMillis());
      });
  }
}