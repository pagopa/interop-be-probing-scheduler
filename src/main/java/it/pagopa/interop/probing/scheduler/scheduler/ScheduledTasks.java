package it.pagopa.interop.probing.scheduler.scheduler;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
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

  private Integer offset;


  @Scheduled(cron = "${scheduler.cron.expression}")
  public void scheduleFixedDelayTask() {
    MDC.put(LoggingPlaceholders.TRACE_ID_PLACEHOLDER,
        "- [CID= " + UUID.randomUUID().toString().toLowerCase() + "]");
    logger.logSchedulerStart();
    try {
      offset = 0;
      while (true) {
        CompletableFuture<PollingEserviceResponse> response = CompletableFuture
            .supplyAsync(() -> eserviceService.getEservicesReadyForPolling(limit, offset));
        for (EserviceContent service : response.get().getContent()) {
          try {
            CompletableFuture.runAsync(() -> {
              try {
                eserviceService.updateLastRequest(service.getEserviceRecordId(), ChangeLastRequest
                    .builder().lastRequest(OffsetDateTime.now(ZoneOffset.UTC)).build());
                servicesSend.sendMessage(service);
              } catch (Exception e) {
                e.printStackTrace();
              }
            });
          } catch (Exception e) {
            logger.logException(e, service.getEserviceRecordId());
          }
        }
        if ((offset + limit) >= response.get().getTotalElements()) {
          break;
        }
        offset += limit;
      }
    } catch (Exception e) {
      logger.logException(e);
    } finally {
      logger.logSchedulerEnd();
      MDC.remove(LoggingPlaceholders.TRACE_ID_PLACEHOLDER);
    }
  }
}
