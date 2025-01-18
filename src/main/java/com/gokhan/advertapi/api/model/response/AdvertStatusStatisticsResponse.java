package com.gokhan.advertapi.api.model.response;

import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdvertStatusStatisticsResponse {
  private Map<String, Long> statusCounts;
}
