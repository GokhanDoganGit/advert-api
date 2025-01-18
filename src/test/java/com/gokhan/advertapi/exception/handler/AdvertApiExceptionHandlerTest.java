package com.gokhan.advertapi.exception.handler;

import static com.gokhan.advertapi.constants.ErrorMessageConstants.BAD_WORD_DETECTED;
import static com.gokhan.advertapi.constants.ErrorMessageConstants.ILLEGAL_STATUS;
import static com.gokhan.advertapi.constants.ErrorMessageConstants.NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.gokhan.advertapi.api.model.response.AdvertErrorResponse;
import com.gokhan.advertapi.exception.BadWordsException;
import com.gokhan.advertapi.exception.NotFoundException;
import com.gokhan.advertapi.exception.ReadException;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

class AdvertApiExceptionHandlerTest {

  private AdvertApiExceptionHandler exceptionHandler;

  @BeforeEach
  public void setUp() {
    exceptionHandler = new AdvertApiExceptionHandler();
  }

  @Test
  public void testHandleBadWordsException() {
    // When
    BadWordsException ex = new BadWordsException("Bad word detected");

    ResponseEntity<AdvertErrorResponse> response = exceptionHandler.handleBadWordsException(ex);

    // Then
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals(BAD_WORD_DETECTED, Objects.requireNonNull(response.getBody()).getDetails().get(0).getErrorName());
    assertEquals("Bad word detected", response.getBody().getDetails().get(0).getErrorReason());
  }

  @Test
  public void testHandleReadException() {
    // When
    ReadException ex = new ReadException("Resource not found");

    ResponseEntity<AdvertErrorResponse> response = exceptionHandler.handleReadException(ex);

    // Then
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals(NOT_FOUND, Objects.requireNonNull(response.getBody()).getDetails().get(0).getErrorName());
    assertEquals("Resource not found", response.getBody().getDetails().get(0).getErrorReason());
  }

  @Test
  public void testHandleMethodArgumentNotValidException() {
    // When
    BindingResult bindingResult = mock(BindingResult.class);
    FieldError fieldError = new FieldError("advertRequest", "title", "must not be null");
    when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));

    MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

    ResponseEntity<AdvertErrorResponse> response = exceptionHandler.handleMethodArgumentNotValidException(
        ex);

    // Then
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("title", Objects.requireNonNull(response.getBody()).getDetails().get(0).getErrorName());
    assertEquals("must not be null", response.getBody().getDetails().get(0).getErrorReason());
  }

  @Test
  public void testHandleIllegalStateException() {
    // When
    IllegalStateException ex = new IllegalStateException("Invalid state");

    ResponseEntity<AdvertErrorResponse> response = exceptionHandler.handleIllegalStateException(ex);

    // Then
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals(ILLEGAL_STATUS, Objects.requireNonNull(response.getBody()).getDetails().get(0).getErrorName());
    assertEquals("Invalid state", response.getBody().getDetails().get(0).getErrorReason());
  }

  @Test
  public void testHandleNotFoundException() {
    // When
    NotFoundException ex = new NotFoundException("Resource is missing");

    ResponseEntity<AdvertErrorResponse> response = exceptionHandler.handleNotFoundException(ex);

    // Then
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals(NOT_FOUND, Objects.requireNonNull(response.getBody()).getDetails().get(0).getErrorName());
    assertEquals("Resource is missing", response.getBody().getDetails().get(0).getErrorReason());
  }

  @Test
  public void testHandleConstraintViolationException() {
    // When
    ConstraintViolation<?> violation = mock(ConstraintViolation.class);
    Path mockPath = mock(Path.class);
    when(mockPath.toString()).thenReturn("title");
    when(violation.getPropertyPath()).thenReturn(mockPath);
    when(violation.getMessage()).thenReturn("must be valid");

    Set<ConstraintViolation<?>> violations = Collections.singleton(violation);
    ConstraintViolationException ex = new ConstraintViolationException(violations);

    ResponseEntity<AdvertErrorResponse> response = exceptionHandler.handleConstraintViolationException(
        ex);

    // Then
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("title",
        Objects.requireNonNull(response.getBody()).getDetails().get(0).getErrorName());
    assertEquals("must be valid", response.getBody().getDetails().get(0).getErrorReason());
  }

  @Test
  public void testHandleHttpMessageNotReadableException() {
    // When
    String errorMessage = "Invalid value for enum.";
    HttpMessageNotReadableException exception = new HttpMessageNotReadableException(errorMessage,
        (HttpInputMessage) null);

    ResponseEntity<AdvertErrorResponse> response = exceptionHandler.handleHttpMessageNotReadableException(
        exception);

    // Then
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("Invalid Enum Value", Objects.requireNonNull(response.getBody()).getMessage());
    assertEquals("Invalid value for enum.",
        response.getBody().getDetails().get(0).getErrorReason());
  }

}