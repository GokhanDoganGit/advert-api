package com.gokhan.advertapi.util.mapper;

import com.gokhan.advertapi.api.model.request.AdvertRequest;
import com.gokhan.advertapi.api.model.request.AdvertStatusUpdateRequest;
import com.gokhan.advertapi.api.model.request.Category;
import com.gokhan.advertapi.api.model.request.Status;
import com.gokhan.advertapi.repository.entity.Advert;
import com.gokhan.advertapi.repository.entity.AdvertStatusChange;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AdvertMapper {

  private static final long EMLAK_EXPIRY_DATE = 4;
  private static final long VASITA_EXPIRY_DATE = 3;
  private static final long ALISVERIS_DIGER_EXPIRY_DATE = 8;

  public static Advert toAdvert(final AdvertRequest advertRequest, boolean isMukerrer) {
    final LocalDateTime now = LocalDateTime.now();
    return Advert.builder()
        .title(advertRequest.getTitle().trim())
        .description(advertRequest.getDescription().trim())
        .category(advertRequest.getCategory())
        .status(isMukerrer ? Status.MUKERRER : getStatus(advertRequest.getCategory()))
        .createdDate(now)
        .expiryDate(getEndDate(advertRequest.getCategory(), now))
        .build();
  }

  public static Advert toUpdatedAdvert(final Advert advert,
      final AdvertStatusUpdateRequest request) {
    return Advert.builder()
        .id(advert.getId())
        .title(advert.getTitle())
        .description(advert.getDescription())
        .category(advert.getCategory())
        .createdDate(advert.getCreatedDate())
        .expiryDate(advert.getExpiryDate())
        .status(request.getNewStatus())
        .statusHistory(toUpdatedAdvertStatusChange(advert, request))
        .build();
  }

  public static AdvertStatusChange toNewAdvertStatusChange(final Advert advert) {
    AdvertStatusChange advertStatusChange = new AdvertStatusChange();
    advertStatusChange.setAdvert(advert);
    advertStatusChange.setOldStatus(null);
    advertStatusChange.setNewStatus(advert.getStatus());
    advertStatusChange.setChangeTime(LocalDateTime.now());
    return advertStatusChange;
  }

  private static List<AdvertStatusChange> toUpdatedAdvertStatusChange(final Advert advert,
      final AdvertStatusUpdateRequest request) {
    AdvertStatusChange advertStatusChange = new AdvertStatusChange();
    advertStatusChange.setAdvert(advert);
    advertStatusChange.setOldStatus(advert.getStatus());
    advertStatusChange.setNewStatus(request.getNewStatus());
    advertStatusChange.setChangeTime(LocalDateTime.now());
    advert.getStatusHistory().add(advertStatusChange);
    return advert.getStatusHistory();
  }

  private static Status getStatus(final Category category) {
    if (category == Category.ALISVERIS) {
      return Status.AKTIF;
    } else {
      return Status.ONAY_BEKLIYOR;
    }
  }

  private static LocalDateTime getEndDate(final Category category, LocalDateTime now) {
    if (category == Category.ALISVERIS || category == Category.DIGER) {
      return now.plusWeeks(ALISVERIS_DIGER_EXPIRY_DATE);
    } else if (category == Category.EMLAK) {
      return now.plusWeeks(EMLAK_EXPIRY_DATE);
    } else {
      return now.plusWeeks(VASITA_EXPIRY_DATE);
    }
  }

}
