package com.gokhan.advertapi.api.model.response;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdvertStatusChangeResponse {
  private Long advertId;

  private List<StatusChange> statusChanges;
}
