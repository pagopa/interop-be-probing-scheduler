package it.pagopa.interop.probing.scheduler.scheduler;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import it.pagopa.interop.probing.scheduler.dto.impl.ChangeLastRequest;
import it.pagopa.interop.probing.scheduler.dto.impl.EserviceContent;
import it.pagopa.interop.probing.scheduler.dto.impl.PollingEserviceResponse;
import it.pagopa.interop.probing.scheduler.producer.ServicesSend;
import it.pagopa.interop.probing.scheduler.service.EserviceService;
import it.pagopa.interop.probing.scheduler.util.logging.Logger;
import it.pagopa.interop.probing.scheduler.util.logging.LoggingPlaceholders;


@Component
public class ScheduledTasks {

  @Autowired
  private EserviceService eserviceService;

  @Autowired
  private Logger logger;

  @Autowired
  private ServicesSend servicesSend;

  @Value("${scheduler.limit}")
  private Integer limit;


  @Scheduled(cron = "${scheduler.cron.expression}")
  public void scheduleFixedDelayTask() {
    MDC.put(LoggingPlaceholders.TRACE_ID_PLACEHOLDER,
        "- [CID= " + UUID.randomUUID().toString().toLowerCase() + "]");
    logger.logSchedulerStart();
    Integer offset = 0;
    while (true) {
      PollingEserviceResponse response = eserviceService.getEservicesReadyForPolling(limit, offset);
      for (EserviceContent service : response.getContent()) {
        try {
          eserviceService.updateLastRequest(service.getEserviceRecordId(),
              ChangeLastRequest.builder().lastRequest(OffsetDateTime.now(ZoneOffset.UTC)).build());
          servicesSend.sendMessage(service);
        } catch (Exception e) {
          logger.logQueueSendError(service.getEserviceRecordId());
        }
      }
      if ((offset + limit) >= response.getTotalElements()) {
        break;
      }
      offset += limit;
    }
    logger.logSchedulerEnd();
    MDC.remove(LoggingPlaceholders.TRACE_ID_PLACEHOLDER);
  }
}
