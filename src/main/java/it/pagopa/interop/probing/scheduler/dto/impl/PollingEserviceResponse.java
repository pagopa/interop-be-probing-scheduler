package it.pagopa.interop.probing.scheduler.dto.impl;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.pagopa.interop.probing.scheduler.dto.Response;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class PollingEserviceResponse implements Response {

  @JsonProperty("content")
  @NotNull(message = "must not be null")
  @Builder.Default
  private List<EserviceContent> content = new ArrayList<>();

  @JsonProperty("totalElements")
  @NotNull(message = "must not be null")
  private Integer totalElements;

}
