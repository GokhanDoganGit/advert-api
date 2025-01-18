package com.gokhan.advertapi.exception.handler;

import static com.gokhan.advertapi.constants.ErrorMessageConstants.BAD_REQUEST_MESSAGE;
import static com.gokhan.advertapi.constants.ErrorMessageConstants.BAD_WORD_DETECTED;
import static com.gokhan.advertapi.constants.ErrorMessageConstants.ILLEGAL_STATUS;
import static com.gokhan.advertapi.constants.ErrorMessageConstants.NOT_FOUND;

import com.gokhan.advertapi.api.model.response.AdvertApiErrorDetailResponse;
import com.gokhan.advertapi.api.model.response.AdvertErrorResponse;
import com.gokhan.advertapi.exception.BadWordsException;
import com.gokhan.advertapi.exception.NotFoundException;
import com.gokhan.advertapi.exception.ReadException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AdvertApiExceptionHandler {

  @ExceptionHandler(BadWordsException.class)
  public ResponseEntity<AdvertErrorResponse> handleBadWordsException(BadWordsException ex) {
    return buildErrorResponse(BAD_WORD_DETECTED, ex.getMessage());
  }

  @ExceptionHandler(ReadException.class)
  public ResponseEntity<AdvertErrorResponse> handleReadException(ReadException ex) {
    return buildErrorResponse(NOT_FOUND, ex.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<AdvertErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
    return buildErrorResponseFromBindingResult(ex.getBindingResult());
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<AdvertErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
    return buildErrorResponseFromViolations(ex.getConstraintViolations());
  }

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<AdvertErrorResponse> handleIllegalStateException(IllegalStateException ex) {
    return buildErrorResponse(ILLEGAL_STATUS, ex.getMessage());
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<AdvertErrorResponse> handleNotFoundException(NotFoundException ex) {
    return buildNotFoundErrorResponse(ex.getMessage());
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<AdvertErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
    return buildHttpMessageNotReadableErrorResponse(ex.getMessage());
  }

  private ResponseEntity<AdvertErrorResponse> buildHttpMessageNotReadableErrorResponse(String errorReason) {
    AdvertApiErrorDetailResponse errorDetail = new AdvertApiErrorDetailResponse(BAD_REQUEST_MESSAGE, errorReason);
    AdvertErrorResponse errorResponse = AdvertErrorResponse.builder()
        .message("Invalid Enum Value")
        .details(Collections.singletonList(errorDetail))
        .build();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  private ResponseEntity<AdvertErrorResponse> buildNotFoundErrorResponse(String errorReason) {
    AdvertApiErrorDetailResponse errorDetail = new AdvertApiErrorDetailResponse(NOT_FOUND, errorReason);
    AdvertErrorResponse errorResponse = AdvertErrorResponse.builder()
        .message("Resource is not found")
        .details(Collections.singletonList(errorDetail))
        .build();
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }

  private ResponseEntity<AdvertErrorResponse> buildErrorResponse(String errorName, String errorReason) {
    AdvertApiErrorDetailResponse errorDetail = new AdvertApiErrorDetailResponse(errorName, errorReason);
    AdvertErrorResponse errorResponse = AdvertErrorResponse.builder()
        .message(BAD_REQUEST_MESSAGE)
        .details(Collections.singletonList(errorDetail))
        .build();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  private ResponseEntity<AdvertErrorResponse> buildErrorResponseFromBindingResult(BindingResult bindingResult) {
    List<AdvertApiErrorDetailResponse> errorDetails = bindingResult.getFieldErrors().stream()
        .map(fieldError -> new AdvertApiErrorDetailResponse(fieldError.getField(), fieldError.getDefaultMessage()))
        .collect(Collectors.toList());
    AdvertErrorResponse errorResponse = AdvertErrorResponse.builder()
        .message(BAD_REQUEST_MESSAGE)
        .details(errorDetails)
        .build();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  private ResponseEntity<AdvertErrorResponse> buildErrorResponseFromViolations(Set<ConstraintViolation<?>> violations) {
    List<AdvertApiErrorDetailResponse> errorDetails = violations.stream()
        .map(violation -> new AdvertApiErrorDetailResponse(violation.getPropertyPath().toString(), violation.getMessage()))
        .collect(Collectors.toList());
    AdvertErrorResponse errorResponse = AdvertErrorResponse.builder()
        .message(BAD_REQUEST_MESSAGE)
        .details(errorDetails)
        .build();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }
}