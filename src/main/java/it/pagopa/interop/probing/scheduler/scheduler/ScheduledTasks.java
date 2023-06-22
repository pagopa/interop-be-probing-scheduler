package it.pagopa.interop.probing.scheduler.scheduler;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.spring.aop.XRayEnabled;
import it.pagopa.interop.probing.scheduler.dto.impl.ChangeLastRequest;
import it.pagopa.interop.probing.scheduler.dto.impl.EserviceContent;
import it.pagopa.interop.probing.scheduler.dto.impl.PollingEserviceResponse;
import it.pagopa.interop.probing.scheduler.producer.ServicesSend;
import it.pagopa.interop.probing.scheduler.service.EserviceService;
import it.pagopa.interop.probing.scheduler.util.logging.Logger;
import it.pagopa.interop.probing.scheduler.util.logging.LoggingPlaceholders;

@Component
@XRayEnabled
public class ScheduledTasks {

  @Autowired
  private EserviceService eserviceService;

  @Autowired
  private Logger logger;

  @Autowired
  private ServicesSend servicesSend;

  @Value("${scheduler.limit}")
  private Integer limit;

  @Value("${spring.application.name}")
  private String awsXraySegmentName;

  @Autowired
  private ExecutorService executor;

  @Scheduled(cron = "${scheduler.cron.expression}")
  public void scheduleFixedDelayTask() {
    schedulerStart();
  }

  public void schedulerStart() {
    MDC.put(LoggingPlaceholders.TRACE_ID_PLACEHOLDER,
        "- [CID= " + UUID.randomUUID().toString().toLowerCase() + "]");

    logger.logSchedulerStart();
    try {
      Integer offset = 0;
      while (true) {
        AWSXRay.beginSegment(LoggingPlaceholders.SEARCH_SUBSEGMENT_PLACEHOLDER);
        MDC.put(LoggingPlaceholders.TRACE_ID_XRAY_PLACEHOLDER,
            LoggingPlaceholders.TRACE_ID_XRAY_MDC_PREFIX
                + AWSXRay.getCurrentSegment().getTraceId().toString() + "]");
        PollingEserviceResponse response =
            eserviceService.getEservicesReadyForPolling(limit, offset);
        AWSXRay.endSegment();
        if (!response.getContent().isEmpty()) {
          CompletableFuture<Void> future = new CompletableFuture<Void>();
          for (EserviceContent service : response.getContent()) {
            future = CompletableFuture.runAsync(() -> {
              try {
                AWSXRay.beginSegment(LoggingPlaceholders.UPDATE_LAST_REQ_SUBSEGMENT_PLACEHOLDER
                    + service.getEserviceRecordId());
                MDC.put(LoggingPlaceholders.TRACE_ID_XRAY_PLACEHOLDER,
                    LoggingPlaceholders.TRACE_ID_XRAY_MDC_PREFIX
                        + AWSXRay.getCurrentSegment().getTraceId().toString() + "]");
                eserviceService.updateLastRequest(service.getEserviceRecordId(), ChangeLastRequest
                    .builder().lastRequest(OffsetDateTime.now(ZoneOffset.UTC)).build());
                servicesSend.sendMessage(service);
                AWSXRay.endSegment();
              } catch (Exception e) {
                logger.logException(e, service.getEserviceRecordId());
              }
            }, executor);
          }
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
      MDC.remove(LoggingPlaceholders.TRACE_ID_XRAY_PLACEHOLDER);
    }
  }
}
