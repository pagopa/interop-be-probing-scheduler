package it.pagopa.interop.probing.scheduler.service;

import it.pagopa.interop.probing.scheduler.dto.PollingActiveEserviceResponse;

public interface EserviceService {
  PollingActiveEserviceResponse getEservicesActive(Integer limit, Integer offset);
}
