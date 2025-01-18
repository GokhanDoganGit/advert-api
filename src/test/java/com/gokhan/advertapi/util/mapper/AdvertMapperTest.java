package com.gokhan.advertapi.util.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.gokhan.advertapi.api.model.request.AdvertRequest;
import com.gokhan.advertapi.api.model.request.Category;
import com.gokhan.advertapi.api.model.request.Status;
import com.gokhan.advertapi.repository.entity.Advert;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AdvertMapperTest {

  private AdvertRequest advertRequest;

  @BeforeEach
  void setUp() {
    advertRequest = mock(AdvertRequest.class);
    when(advertRequest.getTitle()).thenReturn("Test for Advert Title");
    when(advertRequest.getDescription()).thenReturn("Test for Advert Description");
  }

  @Test
  void shouldReturnAdvertWithAktifStatusForAlisveris() {
    // When
    when(advertRequest.getCategory()).thenReturn(Category.ALISVERIS);
    Advert advert = AdvertMapper.toAdvert(advertRequest, false);

    // Then
    assertEquals(Status.AKTIF, advert.getStatus());
  }

  @Test
  void shouldReturnAdvertWithOnayBekliyorStatusForDiger() {
    // When
    when(advertRequest.getCategory()).thenReturn(Category.EMLAK);
    Advert advert = AdvertMapper.toAdvert(advertRequest, false);

    // Then
    assertEquals(Status.ONAY_BEKLIYOR, advert.getStatus());
  }

  @Test
  void shouldReturnAdvertWith8WeeksEndDateForAlisveris() {
    // When
    when(advertRequest.getCategory()).thenReturn(Category.ALISVERIS);
    LocalDateTime now = LocalDateTime.now();
    Advert advert = AdvertMapper.toAdvert(advertRequest, false);

    // Then
    assertEquals(now.plusWeeks(8).getDayOfMonth(), advert.getExpiryDate().getDayOfMonth());
  }

  @Test
  void shouldReturnAdvertWith4WeeksEndDateForEmlak() {
    // When
    when(advertRequest.getCategory()).thenReturn(Category.EMLAK);
    LocalDateTime now = LocalDateTime.now();
    Advert advert = AdvertMapper.toAdvert(advertRequest, false);

    // Then
    assertEquals(now.plusWeeks(4).getDayOfMonth(), advert.getExpiryDate().getDayOfMonth());
  }

  @Test
  void shouldReturnAdvertWith8WeeksEndDateForDiger() {
    // When
    when(advertRequest.getCategory()).thenReturn(Category.DIGER);
    LocalDateTime now = LocalDateTime.now();
    Advert advert = AdvertMapper.toAdvert(advertRequest, false);

    // Then
    assertEquals(now.plusWeeks(8).getDayOfMonth(), advert.getExpiryDate().getDayOfMonth());
  }

  @Test
  void shouldReturnAdvertWith3WeeksEndDateForVasita() {
    // When
    when(advertRequest.getCategory()).thenReturn(Category.VASITA);
    LocalDateTime now = LocalDateTime.now();
    Advert advert = AdvertMapper.toAdvert(advertRequest, false);

    // Then
    assertEquals(now.plusWeeks(3).getDayOfMonth(), advert.getExpiryDate().getDayOfMonth());
  }

  @Test
  void shouldReturnAdvertWithMukerrerStatusForDuplicateAdvert() {
    // When
    when(advertRequest.getCategory()).thenReturn(Category.ALISVERIS);
    Advert advert = AdvertMapper.toAdvert(advertRequest, true);

    // Then
    assertEquals(Status.MUKERRER, advert.getStatus());
  }
}
