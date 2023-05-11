package it.pagopa.interop.probing.scheduler.dto.impl;

import it.pagopa.interop.probing.scheduler.dto.Response;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.pagopa.interop.probing.scheduler.annotations.ValidateStringArraySize;
import it.pagopa.interop.probing.scheduler.util.EserviceTechnology;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EserviceContent implements Response {

  @JsonProperty("eserviceRecordId")
  @NotNull(message = "must not be null")
  @Min(value = 1, message = "must be at least 1")
  private Long eserviceRecordId;

  @JsonProperty("technology")
  @NotNull(message = "must not be null")
  private EserviceTechnology technology;

  @JsonProperty("basePath")
  @NotEmpty(message = "list cannot be empty")
  @ValidateStringArraySize(maxSize = 2048)
  private String[] basePath;
}
