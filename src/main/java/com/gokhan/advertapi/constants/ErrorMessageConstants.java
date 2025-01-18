package com.gokhan.advertapi.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ErrorMessageConstants {

  public static final String BAD_REQUEST_MESSAGE = "Request parameters didn't validate.";
  public static final String INTERNAL_ERROR_MESSAGE = "Internal server error occurred.";
  public static final String SERVICE_UNAVAILABLE = "Service is unavailable. Please try again.";
  public static final String BAD_WORD_DETECTED = "Bad word is detected.";
  public static final String NOT_FOUND = "A specified resource is not found.";
  public static final String ILLEGAL_STATUS = "Illegal Status Detected";
}
