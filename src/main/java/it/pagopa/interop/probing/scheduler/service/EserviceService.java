package it.pagopa.interop.probing.scheduler.service;

import java.util.List;
import it.pagopa.interop.probing.scheduler.dto.PollingActiveEserviceResponse;

public interface EserviceService {
  List<PollingActiveEserviceResponse> getEservicesActive();
}
