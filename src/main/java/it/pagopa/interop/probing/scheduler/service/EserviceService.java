package it.pagopa.interop.probing.scheduler.service;

import it.pagopa.interop.probing.scheduler.dto.ChangeLastRequest;
import it.pagopa.interop.probing.scheduler.dto.PollingEserviceResponse;

public interface EserviceService {
  PollingEserviceResponse getEservicesReadyForPolling(Integer limit, Integer offset);

  void updateLastRequest(Long eserviceRecordId, ChangeLastRequest changeLastRequest);
}
