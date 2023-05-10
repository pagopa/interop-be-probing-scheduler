package it.pagopa.interop.probing.scheduler.util.logging.impl;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.springframework.stereotype.Component;
import it.pagopa.interop.probing.scheduler.util.logging.Logger;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class LoggerImpl implements Logger {
  @Override
  public void logSchedulerStart() {
    log.info("scheduler started at: {}", LocalDateTime.now(ZoneOffset.UTC));
  }

  @Override
  public void logSchedulerEnd() {
    log.info("scheduler ended at: {}", LocalDateTime.now(ZoneOffset.UTC));
  }

  @Override
  public void logQueueSendError(Long eserviceRecordId) {
    log.error("Error while sending the service with record id {} to SQS", eserviceRecordId);
  }

  @Override
  public void logQueueSendSuccess(Long eserviceRecordId) {
    log.info("Service with record id {} has been published in SQS.", eserviceRecordId);
  }

  @Override
  public void logGetEserviceActive(Integer limit, Integer offset) {
    log.info("Getting e-services to poll. limit={}, offset={}", limit, offset);
  }

  @Override
  public void logUpdateLastRequest(Long eserviceRecordId, OffsetDateTime lastRequest) {
    log.info("Updating e-service last polling request time. eserviceRecordId={}, lastRequest={}",
        eserviceRecordId, lastRequest);
  }


}
