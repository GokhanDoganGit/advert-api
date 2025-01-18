package com.gokhan.advertapi.config;

import com.gokhan.advertapi.intercepter.CorrelationIdInterceptor;
import com.gokhan.advertapi.intercepter.LogExecutionTimeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new LogExecutionTimeInterceptor());
    registry.addInterceptor(new CorrelationIdInterceptor());
  }
}
