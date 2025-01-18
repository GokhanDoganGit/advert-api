package com.gokhan.advertapi.common;

import static com.gokhan.advertapi.constant.TestConstant.ADVERT_ID;
import static com.gokhan.advertapi.constants.ApplicationConstant.AKTIF;
import static com.gokhan.advertapi.constants.ApplicationConstant.DEAKTIF;
import static com.gokhan.advertapi.constants.ApplicationConstant.ONAY_BEKLIYOR;

import com.gokhan.advertapi.api.model.request.AdvertRequest;
import com.gokhan.advertapi.api.model.request.AdvertStatusUpdateRequest;
import com.gokhan.advertapi.api.model.request.Category;
import com.gokhan.advertapi.api.model.request.Status;
import com.gokhan.advertapi.repository.entity.Advert;
import com.gokhan.advertapi.repository.entity.AdvertStatusChange;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AdvertUtil {

  public static AdvertRequest createAdvertRequest() {
    AdvertRequest request = new AdvertRequest();
    request.setTitle("Sample Title");
    request.setDescription("Sample Description of the Advert Request");
    request.setCategory(Category.VASITA);
    return request;
  }

  public static Advert createExistingAdvert() {
    return Advert.builder()
        .title("Sample Title")
        .description("Sample Description of the Advert Request")
        .category(Category.VASITA)
        .status(Status.MUKERRER)
        .build();
  }

  public static Advert createNewAdvert(Status status) {
    return Advert.builder()
        .id(ADVERT_ID)
        .status(status)
        .build();
  }

  public static AdvertStatusUpdateRequest createNewAdvertStatusUpdateRequest(Status status) {
    AdvertStatusUpdateRequest request = new AdvertStatusUpdateRequest();
    request.setAdvertId(ADVERT_ID);
    request.setNewStatus(status);
    return request;
  }

  public static List<Advert> getAdverts() {
    List<Advert> adverts = new ArrayList<>();
    adverts.add(createNewAdvert(Status.AKTIF));
    adverts.add(createNewAdvert(Status.DEAKTIF));
    adverts.add(createNewAdvert(Status.AKTIF));
    adverts.add(createNewAdvert(Status.ONAY_BEKLIYOR));
    adverts.add(createNewAdvert(Status.AKTIF));
    return adverts;
  }

  public static Map<String, Long> getStatistics() {
    Map<String, Long> statistics = new HashMap<>();
    statistics.put(AKTIF, 3L);
    statistics.put(DEAKTIF, 1L);
    statistics.put(ONAY_BEKLIYOR, 1L);
    return statistics;
  }

  public static AdvertStatusChange createAdvertStatusChange(Status oldStatus, Status newStatus) {
    AdvertStatusChange advertStatusChange = new AdvertStatusChange();
    advertStatusChange.setOldStatus(oldStatus);
    advertStatusChange.setNewStatus(newStatus);
    advertStatusChange.setChangeTime(LocalDateTime.now());
    return advertStatusChange;
  }

  public static Advert getAdvertWithStatusChange(List<AdvertStatusChange> advertStatusChanges) {
    return Advert.builder()
        .id(ADVERT_ID)
        .title("Sample Title")
        .description("Sample Description")
        .status(Status.AKTIF)
        .statusHistory(advertStatusChanges)
        .build();
  }
}
