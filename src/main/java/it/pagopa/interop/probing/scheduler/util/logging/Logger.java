package it.pagopa.interop.probing.scheduler.util.logging;

import java.time.OffsetDateTime;

public interface Logger {
  void logSchedulerStart();

  void logSchedulerEnd();

  void logException(Exception exception);

  void logException(Exception exception, Long eserviceRecordId);

  void logQueueSendSuccess(Long eserviceRecordId);

  void logGetEserviceActive(Integer limit, Integer offset);

  void logUpdateLastRequest(Long eserviceRecordId, OffsetDateTime lastRequest);


}
