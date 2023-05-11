package it.pagopa.interop.probing.scheduler.dto.impl;

import it.pagopa.interop.probing.scheduler.dto.Request;
import it.pagopa.interop.probing.scheduler.dto.Response;
import java.time.OffsetDateTime;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangeLastRequest implements Request {

  @NotNull(message = "must not be null")
  @JsonProperty("lastRequest")
  private OffsetDateTime lastRequest;

}
