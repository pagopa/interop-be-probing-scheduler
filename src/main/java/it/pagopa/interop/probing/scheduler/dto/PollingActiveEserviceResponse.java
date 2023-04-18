package it.pagopa.interop.probing.scheduler.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class PollingActiveEserviceResponse {

  @JsonProperty("content")
  private List<PollingActiveEserviceContent> content;

  @JsonProperty("totalElements")
  private Long totalElements;

}
