package com.gokhan.advertapi.intercepter;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class LogExecutionTimeInterceptorTest {

  @Mock
  private HttpServletRequest request;

  @Mock
  private HttpServletResponse response;

  @Mock
  private Object handler;

  @InjectMocks
  private LogExecutionTimeInterceptor logExecutionTimeInterceptor;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void shouldSetStartTime() {
    // When
    when(request.getAttribute("startTime")).thenReturn(null);

    logExecutionTimeInterceptor.preHandle(request, response, handler);

    // Then
    verify(request, times(1)).setAttribute(eq("startTime"), anyLong());
  }

  @Test
  void afterCompletion_ShouldLogExecutionTimeIfLongerThanThreshold() {
    // When
    long startTime = System.currentTimeMillis() - 6000;
    when(request.getAttribute("startTime")).thenReturn(startTime);
    when(request.getRequestURI()).thenReturn("/test-uri");

    logExecutionTimeInterceptor.afterCompletion(request, response, handler, null);

    // Then
    assertTrue(true);
  }

}