package com.gokhan.advertapi.api.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdvertStatusUpdateRequest {

  @NotNull(message = "Advert ID cannot be null")
  @Schema(description = "Unique identifier of the advert to be updated", example = "1")
  private Long advertId;

  @NotNull(message = "New status cannot be null")
  @Schema(
      description = "New status of the advert. Valid values are: ONAY_BEKLIYOR, AKTIF, DEAKTIF, MUKERRER",
      example = "AKTIF",
      allowableValues = {"ONAY_BEKLIYOR", "AKTIF", "DEAKTIF", "MUKERRER"}
  )
  private Status newStatus;

}
