package it.pagopa.interop.probing.scheduler.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class PollingEserviceResponse {

  @JsonProperty("content")
  private List<EserviceContent> content;

  @JsonProperty("totalElements")
  private Integer totalElements;

}
