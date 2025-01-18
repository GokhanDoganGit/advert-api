package com.gokhan.advertapi.controller;

import com.gokhan.advertapi.api.AdvertApi;
import com.gokhan.advertapi.api.model.request.AdvertRequest;
import com.gokhan.advertapi.api.model.request.AdvertStatusUpdateRequest;
import com.gokhan.advertapi.api.model.response.AdvertStatusChangeResponse;
import com.gokhan.advertapi.api.model.response.AdvertStatusStatisticsResponse;
import com.gokhan.advertapi.service.AdvertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdvertController implements AdvertApi {

  private final AdvertService advertService;

  @Override
  public ResponseEntity<Void> createAdvert(String xCorrelationId, AdvertRequest request) {
    advertService.createAdvert(xCorrelationId, request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .build();
  }

  @Override
  public ResponseEntity<Void> updateAdvertStatus(String correlationId,
      AdvertStatusUpdateRequest request) {
    advertService.updateStatus(correlationId, request);
    return ResponseEntity.status(HttpStatus.OK)
        .build();
  }

  @Override
  public ResponseEntity<AdvertStatusStatisticsResponse> getAdvertStatusStatistics(
      String correlationId) {
    AdvertStatusStatisticsResponse response = AdvertStatusStatisticsResponse.builder()
        .statusCounts(advertService.getStatusStatistics(correlationId))
        .build();
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<AdvertStatusChangeResponse> getAdvertStatusChanges(String correlationId,
      Long advertId) {
    AdvertStatusChangeResponse response = AdvertStatusChangeResponse.builder()
        .advertId(advertId)
        .statusChanges(advertService.getStatusChanges(correlationId, advertId))
        .build();
    return ResponseEntity.ok(response);
  }

}
