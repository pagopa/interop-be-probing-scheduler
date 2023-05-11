package it.pagopa.interop.probing.scheduler.dto.impl;

import it.pagopa.interop.probing.scheduler.dto.Response;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class PollingEserviceResponse implements Response {

  @JsonProperty("content")
  @NotNull(message = "must not be null")
  private List<EserviceContent> content;

  @JsonProperty("totalElements")
  @NotNull(message = "must not be null")
  private Integer totalElements;

}
