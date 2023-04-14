package it.pagopa.interop.probing.scheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class InteropSchedulerApplication {

  public static void main(String[] args) {
    SpringApplication.run(InteropSchedulerApplication.class, args);
  }

}
