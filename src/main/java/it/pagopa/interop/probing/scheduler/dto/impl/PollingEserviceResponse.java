package it.pagopa.interop.probing.scheduler.dto.impl;

import it.pagopa.interop.probing.scheduler.dto.Dto;
import it.pagopa.interop.probing.scheduler.dto.impl.EserviceContent;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class PollingEserviceResponse implements Dto {

  @JsonProperty("content")
  @NotNull(message = "must not be null")
  private List<EserviceContent> content;

  @JsonProperty("totalElements")
  @NotNull(message = "must not be null")
  private Integer totalElements;

}
