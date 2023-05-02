package it.pagopa.interop.probing.scheduler.util.logging;

public interface Logger {
  void logSchedulerStart();

  void logSchedulerEnd();

  void logQueueSendError(Long eserviceRecordId);

  void logQueueSendSuccess(Long eserviceRecordId);
}
