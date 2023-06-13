package it.pagopa.interop.probing.scheduler.config.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class ThreadConfig implements AsyncConfigurer {

  @Value("${threadpool.max-pool-size}")
  private int maxPoolSize;


  @Bean
  public ExecutorService executor() {
    ExecutorService executor = Executors.newFixedThreadPool(maxPoolSize);
    return executor;
  }
}
