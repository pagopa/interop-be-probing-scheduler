package it.pagopa.interop.probing.scheduler.util.logging.impl;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.apache.commons.lang3.exception.ExceptionUtils;
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
  public void logException(Exception exception, Long eserviceRecordId) {
    log.error("An error occurred during elaboration. eserviceRecordId={}, stacktrace=\n{}", eserviceRecordId, ExceptionUtils.getStackTrace(exception));
  }

  @Override
  public void logQueueSendSuccess(Long eserviceRecordId) {
    log.info("e-service has been published to SQS queue. eserviceRecordId={}", eserviceRecordId);
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

  @Override
  public void logException(Exception exception) {
    log.error(ExceptionUtils.getStackTrace(exception));
  }
}
