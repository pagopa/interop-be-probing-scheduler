package it.pagopa.interop.probing.scheduler.dto.impl;

import it.pagopa.interop.probing.scheduler.dto.Dto;
import java.time.OffsetDateTime;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangeLastRequest implements Dto {

  @NotNull(message = "must not be null")
  @JsonProperty("lastRequest")
  private OffsetDateTime lastRequest;

}
