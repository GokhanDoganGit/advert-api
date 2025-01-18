package com.gokhan.advertapi.intercepter;

import static com.gokhan.advertapi.constants.ApplicationConstant.X_CORRELATION_ID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class LogExecutionTimeInterceptor implements HandlerInterceptor {

  private static final String START_TIME = "startTime";
  private static final long EXECUTION_THRESHOLD = 5000;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
      Object handler) {
    request.setAttribute(START_TIME, System.currentTimeMillis());
    return true;
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, Exception ex) {
    Long startTime = (Long) request.getAttribute(START_TIME);
    if (startTime != null) {
      long executionTime = System.currentTimeMillis() - startTime;
      if (executionTime > EXECUTION_THRESHOLD) {
        log.warn("{} request with correlation id {} took {} ms", request.getRequestURI(),
            request.getHeader(X_CORRELATION_ID), executionTime);
      }
    }
  }
}
