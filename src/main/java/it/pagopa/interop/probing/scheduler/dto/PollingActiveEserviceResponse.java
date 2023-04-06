package it.pagopa.interop.probing.scheduler.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.pagopa.interop.probing.scheduler.util.EserviceTechnology;
import lombok.Data;


@Data
public class PollingActiveEserviceResponse {

  @JsonProperty("id")
  private String id;

  @JsonProperty("eserviceName")
  private String eserviceName;

  @JsonProperty("producerName")
  private String producerName;

  @JsonProperty("versionNumber")
  private Integer versionNumber;

  @JsonProperty("technology")
  private EserviceTechnology technology;

  @JsonProperty("basePath")
  private String[] basePath;

}
