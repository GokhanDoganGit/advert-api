package com.gokhan.advertapi.intercepter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gokhan.advertapi.constant.TestConstant;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.MDC;

class CorrelationIdInterceptorTest {

  public static final String X_CORRELATION_ID = "X-Correlation-ID";
  public static final String CORRELATION_ID = "correlationId";

  @Mock
  private HttpServletRequest request;

  @Mock
  private HttpServletResponse response;

  @Mock
  private Object handler;

  @InjectMocks
  private CorrelationIdInterceptor correlationIdInterceptor;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void shouldSetCorrelationIdWhenHeaderIsPresent() {
    // When
    when(request.getHeader(X_CORRELATION_ID)).thenReturn(TestConstant.CORRELATION_ID);

    boolean result = correlationIdInterceptor.preHandle(request, response, handler);

    // Then
    assertTrue(result);
    verify(request, times(1)).getHeader(X_CORRELATION_ID);
    assertEquals(TestConstant.CORRELATION_ID, MDC.get(CORRELATION_ID));
  }

  @Test
  void shouldGenerateNewCorrelationIdWhenHeaderIsMissing() {
    // When
    when(request.getHeader(X_CORRELATION_ID)).thenReturn(null);

    boolean result = correlationIdInterceptor.preHandle(request, response, handler);

    // Then
    assertTrue(result);
    assertNotNull(MDC.get(CORRELATION_ID));
    assertInstanceOf(UUID.class, UUID.fromString(MDC.get(CORRELATION_ID)));
  }

  @Test
  void shouldClearMDCAfterRequest() {
    // When
    MDC.put(X_CORRELATION_ID, CORRELATION_ID);
    correlationIdInterceptor.afterCompletion(request, response, handler, null);

    // Then
    assertNull(MDC.get(X_CORRELATION_ID));
  }
}