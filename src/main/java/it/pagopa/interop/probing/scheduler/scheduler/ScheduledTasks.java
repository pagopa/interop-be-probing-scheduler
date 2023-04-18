package it.pagopa.interop.probing.scheduler.scheduler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import it.pagopa.interop.probing.scheduler.dto.PollingActiveEserviceContent;
import it.pagopa.interop.probing.scheduler.dto.PollingActiveEserviceResponse;
import it.pagopa.interop.probing.scheduler.producer.ServicesSend;
import it.pagopa.interop.probing.scheduler.service.EserviceService;
import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class ScheduledTasks {

  @Autowired
  EserviceService eserviceService;

  @Autowired
  ServicesSend servicesSend;

  @Value("${scheduler.limit}")
  Integer limit;

  @Scheduled(cron = "${scheduler.cron.expression}")
  public void scheduleFixedDelayTask() {
    log.info("scheduler started at: {}", LocalDateTime.now(ZoneOffset.UTC));
    Integer offset = 0;
    while (true) {
      PollingActiveEserviceResponse response = eserviceService.getEservicesActive(limit, offset);
      for (PollingActiveEserviceContent service : response.getContent()) {
        try {
          servicesSend.sendMessage(service);
        } catch (IOException e) {
          log.error("Error while sending the service with id {} to SQS", service.getId());
        }
      }
      if ((offset + limit) >= response.getTotalElements()) {
        break;
      }
      offset += limit;
    }
    log.info("scheduler ended at: {}", LocalDateTime.now(ZoneOffset.UTC));
  }
}
