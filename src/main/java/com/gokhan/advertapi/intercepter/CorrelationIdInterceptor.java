package com.gokhan.advertapi.intercepter;

import static com.gokhan.advertapi.constants.ApplicationConstant.CORRELATION_ID;
import static com.gokhan.advertapi.constants.ApplicationConstant.X_CORRELATION_ID;

import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class CorrelationIdInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(
      final HttpServletRequest request, @Nullable final HttpServletResponse response,
      @Nullable final Object handler) {
    String correlationId = request.getHeader(X_CORRELATION_ID);
    if (StringUtils.isBlank(correlationId)) {
      correlationId = UUID.randomUUID().toString();
      log.warn("CorrelationID is not present, setting unique value: {}", correlationId);
    }
    MDC.put(CORRELATION_ID, correlationId);
    return true;
  }

  @Override
  public void afterCompletion(
      @Nullable HttpServletRequest request,
      @Nullable HttpServletResponse response,
      @Nullable Object handler,
      @Nullable Exception ex) {
    MDC.clear();
  }
}
