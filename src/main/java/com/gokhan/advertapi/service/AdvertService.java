package com.gokhan.advertapi.service;

import static com.gokhan.advertapi.util.mapper.AdvertMapper.toAdvert;
import static com.gokhan.advertapi.util.mapper.AdvertMapper.toNewAdvertStatusChange;
import static com.gokhan.advertapi.util.mapper.AdvertMapper.toUpdatedAdvert;
import static org.apache.commons.lang3.BooleanUtils.isFalse;

import com.gokhan.advertapi.api.model.request.AdvertRequest;
import com.gokhan.advertapi.api.model.request.AdvertStatusUpdateRequest;
import com.gokhan.advertapi.api.model.response.StatusChange;
import com.gokhan.advertapi.exception.BadWordsException;
import com.gokhan.advertapi.exception.NotFoundException;
import com.gokhan.advertapi.repository.AdvertRepository;
import com.gokhan.advertapi.repository.entity.Advert;
import com.gokhan.advertapi.util.ValidatorUtil;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdvertService {

  private final AdvertRepository advertRepository;
  private final BadWordsService badWordsService;

  @Transactional
  public void createAdvert(String correlationId, AdvertRequest request) {
    log.info("Creating Advert with the correlation ID {}.", correlationId);

    checkBadWords(request.getTitle());

    Advert advert = advertRepository.findAllByTitleAndDescriptionAndCategory(
            request.getTitle().trim(),
            request.getDescription().trim(),
            request.getCategory()
        ).stream()
        .findFirst()
        .map(existingIlan -> {
          Advert newAdvert = toAdvert(request, true);
          log.info("Mukerrer Advert with the correlation ID {} is created.", correlationId);
          return newAdvert;
        })
        .orElseGet(() -> {
          Advert newAdvert = toAdvert(request, false);
          log.info("Advert with the correlation ID {} is created.", correlationId);
          return newAdvert;
        });

    advert.getStatusHistory().add(toNewAdvertStatusChange(advert));
    advertRepository.save(advert);
  }


  @Transactional
  public void updateStatus(String correlationId, AdvertStatusUpdateRequest request) {
    Advert advert = advertRepository.findById(request.getAdvertId())
        .orElseThrow(
            () -> new NotFoundException("No advert found with ID " + request.getAdvertId()));

    log.info("Updating Advert with the correlation ID {}.", correlationId);

    ValidatorUtil.validateStatusTransition(advert.getStatus().toString(), request.getNewStatus().toString());
    Advert updatedAdvert = toUpdatedAdvert(advert, request);
    advertRepository.save(updatedAdvert);

    log.info("Advert with the correlation ID {} is updated.", correlationId);
  }

  public Map<String, Long> getStatusStatistics(String correlationId) {
    log.info("Getting the status statistics with the correlation ID {}.", correlationId);
    return advertRepository.findAll().stream()
        .collect(
            Collectors.groupingBy(advert -> advert.getStatus().toString(), Collectors.counting()));
  }

  public List<StatusChange> getStatusChanges(String correlationId, Long advertId) {
    log.info("Getting status changes for advert ID {} with correlation ID: {}", advertId,
        correlationId);

    Advert advert = advertRepository.findById(advertId)
        .orElseThrow(() -> new NotFoundException("No advert found with ID " + advertId));

    return advert.getStatusHistory().stream()
        .map(change -> StatusChange.builder()
            .oldStatus(change.getOldStatus() != null ? change.getOldStatus().toString() : null)
            .newStatus(change.getNewStatus() != null ? change.getNewStatus().toString() : null)
            .changeDateTime(change.getChangeTime())
            .build())
        .collect(Collectors.toList());
  }

  private void checkBadWords(String title) {
    List<String> badWordlist = badWordsService.isBadWordListed(title);
    if (isFalse(badWordlist.isEmpty())) {
      throw new BadWordsException(
          "Title has bad words: " + String.join(", ", badWordlist) + ". Cannot create advert.");
    }
  }

}
