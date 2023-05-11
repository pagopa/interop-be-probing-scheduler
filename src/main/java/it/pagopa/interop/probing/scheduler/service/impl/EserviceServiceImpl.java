package it.pagopa.interop.probing.scheduler.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.pagopa.interop.probing.scheduler.client.EserviceClient;
import it.pagopa.interop.probing.scheduler.dto.impl.ChangeLastRequest;
import it.pagopa.interop.probing.scheduler.dto.impl.PollingEserviceResponse;
import it.pagopa.interop.probing.scheduler.service.EserviceService;
import it.pagopa.interop.probing.scheduler.util.logging.Logger;

@Service
public class EserviceServiceImpl implements EserviceService {

  @Autowired
  private EserviceClient eserviceClient;

  @Autowired
  private Logger logger;

  @Override
  public PollingEserviceResponse getEservicesReadyForPolling(Integer limit, Integer offset) {
    logger.logGetEserviceActive(limit, offset);
    return eserviceClient.getEservicesReadyForPolling(limit, offset).getBody();
  }

  @Override
  public void updateLastRequest(Long eserviceRecordId, ChangeLastRequest changeLastRequest)
      throws Exception {
    logger.logUpdateLastRequest(eserviceRecordId, changeLastRequest.getLastRequest());
    eserviceClient.updateLastRequest(eserviceRecordId, changeLastRequest);
  }

}
