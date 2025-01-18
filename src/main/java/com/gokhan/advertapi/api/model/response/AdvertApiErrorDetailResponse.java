package com.gokhan.advertapi.api.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AdvertApiErrorDetailResponse {

  @Schema(example = "NOT_FOUND")
  private final String errorName;

  @Schema(example = "must be a valid request.")
  private final String errorReason;
}
