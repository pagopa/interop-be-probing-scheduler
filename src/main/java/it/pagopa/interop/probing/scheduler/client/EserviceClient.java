package it.pagopa.interop.probing.scheduler.client;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import it.pagopa.interop.probing.scheduler.dto.PollingActiveEserviceResponse;

@FeignClient(name = "eserviceClient",
    url = "${api.operations.baseUrl}" + "${api.eservice.basePath}")
public interface EserviceClient {

  @GetMapping("/pollingEserviceActive")
  ResponseEntity<List<PollingActiveEserviceResponse>> getEservicesActive();

}
