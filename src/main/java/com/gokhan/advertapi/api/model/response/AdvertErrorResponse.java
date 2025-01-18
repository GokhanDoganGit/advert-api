package com.gokhan.advertapi.api.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.gokhan.advertapi.constants.ErrorMessageConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@JsonInclude(Include.NON_EMPTY)
public class AdvertErrorResponse {

  @Schema(example = ErrorMessageConstants.BAD_REQUEST_MESSAGE)
  String message;

  @Schema
  List<AdvertApiErrorDetailResponse> details;
}