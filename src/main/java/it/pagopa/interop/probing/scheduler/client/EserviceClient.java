package it.pagopa.interop.probing.scheduler.client;

import javax.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import it.pagopa.interop.probing.scheduler.dto.ChangeLastRequest;
import it.pagopa.interop.probing.scheduler.dto.PollingEserviceResponse;

@FeignClient(name = "eserviceClient",
    url = "${api.operations.baseUrl}" + "${api.operations.eservice.basePath}")
public interface EserviceClient {

  @GetMapping("/polling")
  ResponseEntity<PollingEserviceResponse> getEservicesReadyForPolling(
      @RequestParam(value = "limit", required = true) Integer limit,
      @RequestParam(value = "offset", required = true) Integer offset);

  @PostMapping("/{eserviceRecordId}/updateLastRequest")
  ResponseEntity<Void> updateLastRequest(@PathVariable("eserviceRecordId") Long eserviceRecordId,
      @Valid @RequestBody ChangeLastRequest changeLastRequest);
}
