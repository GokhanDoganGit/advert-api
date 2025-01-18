package com.gokhan.advertapi.service;

import static com.gokhan.advertapi.common.AdvertUtil.createAdvertRequest;
import static com.gokhan.advertapi.common.AdvertUtil.createAdvertStatusChange;
import static com.gokhan.advertapi.common.AdvertUtil.createExistingAdvert;
import static com.gokhan.advertapi.common.AdvertUtil.createNewAdvert;
import static com.gokhan.advertapi.common.AdvertUtil.createNewAdvertStatusUpdateRequest;
import static com.gokhan.advertapi.common.AdvertUtil.getAdvertWithStatusChange;
import static com.gokhan.advertapi.common.AdvertUtil.getAdverts;
import static com.gokhan.advertapi.common.AdvertUtil.getStatistics;
import static com.gokhan.advertapi.constant.TestConstant.ADVERT_ID;
import static com.gokhan.advertapi.constant.TestConstant.CORRELATION_ID;
import static com.gokhan.advertapi.constants.ApplicationConstant.AKTIF;
import static com.gokhan.advertapi.constants.ApplicationConstant.X_CORRELATION_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gokhan.advertapi.api.model.request.AdvertRequest;
import com.gokhan.advertapi.api.model.request.AdvertStatusUpdateRequest;
import com.gokhan.advertapi.api.model.request.Status;
import com.gokhan.advertapi.api.model.response.StatusChange;
import com.gokhan.advertapi.exception.BadWordsException;
import com.gokhan.advertapi.exception.NotFoundException;
import com.gokhan.advertapi.repository.AdvertRepository;
import com.gokhan.advertapi.repository.entity.Advert;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class AdvertServiceTest {

  @MockBean
  private AdvertRepository advertRepository;

  @MockBean
  private BadWordsService badWordsService;

  @Autowired
  private AdvertService advertService;

  private AdvertRequest validAdvertRequest;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    validAdvertRequest = createAdvertRequest();
  }

  @Test
  void shouldCreateAdvertSuccessfully() {
    //When
    when(badWordsService.isBadWordListed(validAdvertRequest.getTitle())).thenReturn(Collections.emptyList());
    when(advertRepository.findAllByTitleAndDescriptionAndCategory(
        validAdvertRequest.getTitle(),
        validAdvertRequest.getDescription(),
        validAdvertRequest.getCategory()
    )).thenReturn(List.of());

    advertService.createAdvert(CORRELATION_ID, validAdvertRequest);

    // Then
    verify(advertRepository, times(1)).save(any(Advert.class));
  }

  @Test
  void shouldThrowBadWordsExceptionWhenTitleContainsBadWords() {
    // When
    when(badWordsService.isBadWordListed(validAdvertRequest.getTitle())).thenReturn(List.of("opsiyonlu"));

    // Then
    BadWordsException exception = assertThrows(
        BadWordsException.class,
        () -> advertService.createAdvert(CORRELATION_ID, validAdvertRequest)
    );
    assertEquals("Title has bad words: opsiyonlu. Cannot create advert.", exception.getMessage());
  }

  @Test
  void shouldCreateAdvertSuccessfullyWhenDuplicateExistsButStatusIsMukerrer() {
    // When
    Advert existingAdvert = createExistingAdvert();

    when(badWordsService.isBadWordListed(validAdvertRequest.getTitle())).thenReturn(Collections.emptyList());
    when(advertRepository.findAllByTitleAndDescriptionAndCategory(
        validAdvertRequest.getTitle(),
        validAdvertRequest.getDescription(),
        validAdvertRequest.getCategory()
    )).thenReturn(List.of(existingAdvert));

    advertService.createAdvert(CORRELATION_ID, validAdvertRequest);

    // Then
    verify(advertRepository, times(1)).save(any(Advert.class));
    assertEquals(Status.MUKERRER, existingAdvert.getStatus());
  }

  @Test
  void shouldAddStatusHistoryWhenSavingAdvert() {
    // when
    when(badWordsService.isBadWordListed(validAdvertRequest.getTitle())).thenReturn(Collections.emptyList());
    when(advertRepository.findAllByTitleAndDescriptionAndCategory(
        validAdvertRequest.getTitle(),
        validAdvertRequest.getDescription(),
        validAdvertRequest.getCategory()
    )).thenReturn(List.of());

    advertService.createAdvert(CORRELATION_ID, validAdvertRequest);

    // Then
    ArgumentCaptor<Advert> advertCaptor = ArgumentCaptor.forClass(Advert.class);
    verify(advertRepository).save(advertCaptor.capture());
  }

  @Test
  void shouldUpdateStatus() {
    // When
    AdvertStatusUpdateRequest request = createNewAdvertStatusUpdateRequest(Status.DEAKTIF);
    Mockito.when(advertRepository.findById(ADVERT_ID))
        .thenReturn(Optional.of(createNewAdvert(Status.AKTIF)));

    advertService.updateStatus(X_CORRELATION_ID, request);

    // Then
    Mockito.verify(advertRepository).save(Mockito.any(Advert.class));
  }

  @Test
  void shouldThrowIllegalStateExceptionWhenSameStatus() {
    // When
    AdvertStatusUpdateRequest request = createNewAdvertStatusUpdateRequest(Status.AKTIF);
    Mockito.when(advertRepository.findById(ADVERT_ID))
        .thenReturn(Optional.of(createNewAdvert(Status.AKTIF)));

    // Then
    IllegalStateException exception = Assertions.assertThrows(IllegalStateException.class,
        () -> advertService.updateStatus(X_CORRELATION_ID, request));
    Assertions.assertEquals("The status to be updated is the same as the current status.",
        exception.getMessage());
  }

  @Test
  void shouldUpdateStatusFromPendingApprovalToActive() {
    // When
    AdvertStatusUpdateRequest request = createNewAdvertStatusUpdateRequest(Status.AKTIF);
    Mockito.when(advertRepository.findById(ADVERT_ID))
        .thenReturn(Optional.of(createNewAdvert(Status.ONAY_BEKLIYOR)));

    advertService.updateStatus(X_CORRELATION_ID, request);

    // Then
    Mockito.verify(advertRepository).save(Mockito.any(Advert.class));
  }

  @Test
  void shouldThrowIllegalStateExceptionWhenInvalidTransition() {
    // When
    AdvertStatusUpdateRequest request = createNewAdvertStatusUpdateRequest(Status.AKTIF);
    Mockito.when(advertRepository.findById(ADVERT_ID))
        .thenReturn(Optional.of(createNewAdvert(Status.DEAKTIF)));

    // Then
    IllegalStateException exception = Assertions.assertThrows(IllegalStateException.class,
        () -> advertService.updateStatus(X_CORRELATION_ID, request));
    Assertions.assertEquals(
        "Switching to Active Status can only be done from the Pending Approval status.",
        exception.getMessage());
  }

  @Test
  void shouldThrowIllegalStateExceptionWhenMukerrerStatus() {
    // when
    AdvertStatusUpdateRequest request = createNewAdvertStatusUpdateRequest(Status.AKTIF);
    Mockito.when(advertRepository.findById(ADVERT_ID))
        .thenReturn(Optional.of(createNewAdvert(Status.MUKERRER)));

    // Then
    IllegalStateException exception = Assertions.assertThrows(IllegalStateException.class,
        () -> advertService.updateStatus(X_CORRELATION_ID, request));
    Assertions.assertEquals("The status of duplicate adverts cannot be updated.",
        exception.getMessage());
  }

  @Test
  void shouldThrowNotFoundExceptionWhenNoAdvert() {
    // When
    AdvertStatusUpdateRequest request = createNewAdvertStatusUpdateRequest(Status.AKTIF);

    Mockito.when(advertRepository.findById(ADVERT_ID)).thenReturn(Optional.empty());

    // Then
    NotFoundException exception = Assertions.assertThrows(NotFoundException.class,
        () -> advertService.updateStatus(X_CORRELATION_ID, request));
    Assertions.assertEquals("No advert found with ID " + ADVERT_ID, exception.getMessage());
  }

  @Test
  public void shouldGetStatusStatistics() {
    // When
    when(advertRepository.findAll()).thenReturn(getAdverts());

    Map<String, Long> statistics = advertService.getStatusStatistics(X_CORRELATION_ID);

    // Then
    assertEquals(getStatistics(), statistics);
  }

  @Test
  public void shouldGetStatusStatisticsEmptyList() {
    // When
    when(advertRepository.findAll()).thenReturn(Collections.emptyList());

    Map<String, Long> statistics = advertService.getStatusStatistics(X_CORRELATION_ID);
    Map<String, Long> expected = Collections.emptyMap();

    // Then
    assertEquals(expected, statistics);
  }

  @Test
  public void testGetStatusStatisticsSingleStatus() {
    // When
    when(advertRepository.findAll()).thenReturn(List.of(createNewAdvert(Status.AKTIF)));
    Map<String, Long> expected = new HashMap<>();
    expected.put(AKTIF, 1L);

    Map<String, Long> statistics = advertService.getStatusStatistics(X_CORRELATION_ID);

    // Then
    assertEquals(expected, statistics);
  }

  @Test
  public void testGetStatusStatisticsMultipleSameStatuses() {
    // When
    Advert a1 = createNewAdvert(Status.AKTIF);
    Advert a2 = createNewAdvert(Status.AKTIF);
    Advert a3 = createNewAdvert(Status.AKTIF);
    List<Advert> advertList = Arrays.asList(a1, a2, a3);

    when(advertRepository.findAll()).thenReturn(advertList);

    Map<String, Long> statistics = advertService.getStatusStatistics(X_CORRELATION_ID);

    // Then
    Map<String, Long> expected = new HashMap<>();
    expected.put(AKTIF, 3L);

    assertEquals(expected, statistics);
  }

  @Test
  public void testGetStatusChanges() {
    // When
    when(advertRepository.findById(ADVERT_ID)).thenReturn(
        Optional.of(getAdvertWithStatusChange(
            List.of(createAdvertStatusChange(Status.AKTIF, Status.ONAY_BEKLIYOR),
                createAdvertStatusChange(Status.ONAY_BEKLIYOR,
                    Status.DEAKTIF)))));

    List<StatusChange> statusChanges = advertService.getStatusChanges(X_CORRELATION_ID, ADVERT_ID);

    // Then
    assertNotNull(statusChanges);
    assertEquals(2, statusChanges.size());

    StatusChange change1 = statusChanges.get(0);
    assertEquals(Status.AKTIF.toString(), change1.getOldStatus());
    assertEquals(Status.ONAY_BEKLIYOR.toString(), change1.getNewStatus());
    assertNotNull(change1.getChangeDateTime());

    StatusChange change2 = statusChanges.get(1);
    assertEquals(Status.ONAY_BEKLIYOR.toString(), change2.getOldStatus());
    assertEquals(Status.DEAKTIF.toString(), change2.getNewStatus());
    assertNotNull(change2.getChangeDateTime());

    verify(advertRepository, times(1)).findById(ADVERT_ID);
  }

  @Test
  public void testGetStatusChanges_AdvertNotFound() {
    // When
    when(advertRepository.findById(ADVERT_ID)).thenReturn(Optional.empty());

    // Then
    assertThrows(NotFoundException.class,
        () -> advertService.getStatusChanges(X_CORRELATION_ID, ADVERT_ID));
  }

}