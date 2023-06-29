package it.pagopa.interop.probing.scheduler.scheduler;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
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

  @Autowired
  private ExecutorService executor;

  @Scheduled(cron = "${scheduler.cron.expression}")
  public void scheduleFixedDelayTask() {
    MDC.put(LoggingPlaceholders.TRACE_ID_PLACEHOLDER,
        "- [CID= " + UUID.randomUUID().toString().toLowerCase() + "]");
    logger.logSchedulerStart();
    try {
      Integer offset = 0;
      while (true) {
        PollingEserviceResponse response =
            eserviceService.getEservicesReadyForPolling(limit, offset);

        if (!response.getContent().isEmpty()) {
          List<CompletableFuture<Void>> completableFutures = response.getContent().stream()
              .map(service -> processEservice(service)).collect(Collectors.toList());
          CompletableFuture<Void> future = CompletableFuture
              .allOf(completableFutures.toArray(new CompletableFuture[completableFutures.size()]));
          future.get();
        }
        if (limit >= response.getTotalElements()) {
          break;
        }
      }
    } catch (Exception e) {
      logger.logException(e);
    } finally {
      logger.logSchedulerEnd();
      MDC.remove(LoggingPlaceholders.TRACE_ID_PLACEHOLDER);
    }
  }

  private CompletableFuture<Void> processEservice(EserviceContent service) {
    return CompletableFuture.runAsync(() -> {
      try {
        eserviceService.updateLastRequest(service.getEserviceRecordId(),
            ChangeLastRequest.builder().lastRequest(OffsetDateTime.now(ZoneOffset.UTC)).build());
        servicesSend.sendMessage(service);
      } catch (Exception e) {
        logger.logException(e, service.getEserviceRecordId());
      }
    }, executor);
  }
}
