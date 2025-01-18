package com.gokhan.advertapi.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BadWordsException extends RuntimeException {

  public BadWordsException(String message) {
    super(message);
  }
}