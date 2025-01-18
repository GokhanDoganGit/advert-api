package com.gokhan.advertapi.exception;

import java.io.IOException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReadException extends RuntimeException {

  public ReadException(String message) {
    super(message);
  }

  public ReadException(String message, IOException e) {
    super(message, e);
  }
}