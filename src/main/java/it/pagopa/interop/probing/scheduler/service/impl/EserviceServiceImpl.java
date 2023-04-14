package it.pagopa.interop.probing.scheduler.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.pagopa.interop.probing.scheduler.client.EserviceClient;
import it.pagopa.interop.probing.scheduler.dto.PollingActiveEserviceResponse;
import it.pagopa.interop.probing.scheduler.service.EserviceService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EserviceServiceImpl implements EserviceService {

  @Autowired
  private EserviceClient eserviceClient;

  @Override
  public List<PollingActiveEserviceResponse> getEservicesActive() {
    log.info("Get polling active e-services");
    return eserviceClient.getEservicesActive().getBody();
  }

}
