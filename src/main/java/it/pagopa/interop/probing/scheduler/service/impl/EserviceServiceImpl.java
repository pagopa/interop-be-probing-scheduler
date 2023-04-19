package it.pagopa.interop.probing.scheduler.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.pagopa.interop.probing.scheduler.client.EserviceClient;
import it.pagopa.interop.probing.scheduler.dto.PollingEserviceResponse;
import it.pagopa.interop.probing.scheduler.service.EserviceService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EserviceServiceImpl implements EserviceService {

  @Autowired
  private EserviceClient eserviceClient;

  @Override
  public PollingEserviceResponse getEservicesReadyForPolling(Integer limit, Integer offset) {
    log.info("calling operations getEserviceActive. limit={}, offset={}", limit, offset);
    return eserviceClient.getEservicesReadyForPolling(limit, offset).getBody();
  }

}
