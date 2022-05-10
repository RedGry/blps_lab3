package se.ifmo.blps.lab3.controllers;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import se.ifmo.blps.lab3.dtos.RestErrorResponse;
import se.ifmo.blps.lab3.exceptions.IllegalPropertyUpdateException;
import se.ifmo.blps.lab3.exceptions.ResourceAlreadyExistsException;
import se.ifmo.blps.lab3.exceptions.ResourceNotFoundException;
import se.ifmo.blps.lab3.exceptions.UnauthorizedException;
import se.ifmo.blps.lab3.utils.HttpUtil;

@org.springframework.web.bind.annotation.RestControllerAdvice
@Slf4j
public class RestControllerAdvice {

  @ExceptionHandler(ResourceAlreadyExistsException.class)
  @ResponseStatus(CONFLICT)
  public RestErrorResponse handleResourceAlreadyExistsException(
      final ResourceAlreadyExistsException exception, final HttpServletRequest httpServletRequest) {
    log.warn("Handled ResourceAlreadyExistsException with message {}", exception.getMessage());
    return HttpUtil.getRestErrorResponse(CONFLICT, exception.getMessage(), httpServletRequest);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  @ResponseStatus(NOT_FOUND)
  public RestErrorResponse handleResourceNotFoundException(
      final ResourceNotFoundException exception, final HttpServletRequest httpServletRequest) {
    log.warn("Handled ResourceNotFoundException with message {}", exception.getMessage());
    return HttpUtil.getRestErrorResponse(NOT_FOUND, exception.getMessage(), httpServletRequest);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(BAD_REQUEST)
  public RestErrorResponse handleMethodArgumentNotValidException(
      final MethodArgumentNotValidException exception,
      final HttpServletRequest httpServletRequest) {
    final var errorMessageBuilder = new StringBuilder("Failed field validation with messages: ");
    for (final var fieldError : exception.getBindingResult().getFieldErrors()) {
      errorMessageBuilder
          .append("wrong value for field ")
          .append(fieldError.getField())
          .append(": ")
          .append(fieldError.getDefaultMessage())
          .append(", ");
    }
    errorMessageBuilder.delete(errorMessageBuilder.lastIndexOf(", "), errorMessageBuilder.length());

    final var errorMessage = errorMessageBuilder.toString();
    log.warn("Handled MethodArgumentNotValidException with message {}", errorMessage);
    return HttpUtil.getRestErrorResponse(BAD_REQUEST, errorMessage, httpServletRequest);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseStatus(BAD_REQUEST)
  public RestErrorResponse handleHttpMessageNotReadableException(
      final HttpMessageNotReadableException exception,
      final HttpServletRequest httpServletRequest) {
    log.warn("Handled HttpMessageNotReadableException with message {}", exception.getMessage());
    return HttpUtil.getRestErrorResponse(BAD_REQUEST, exception.getMessage(), httpServletRequest);
  }

  @ExceptionHandler(IllegalPropertyUpdateException.class)
  @ResponseStatus(BAD_REQUEST)
  public RestErrorResponse handleHttpMessageNotReadableException(
      final IllegalPropertyUpdateException exception, final HttpServletRequest httpServletRequest) {
    log.warn("Handled IllegalPropertyUpdateException with message {}", exception.getMessage());
    return HttpUtil.getRestErrorResponse(BAD_REQUEST, exception.getMessage(), httpServletRequest);
  }

  @ExceptionHandler(UnauthorizedException.class)
  @ResponseStatus(UNAUTHORIZED)
  public RestErrorResponse handleUnauthorizedException(
      final UnauthorizedException exception, final HttpServletRequest httpServletRequest) {
    log.warn("Handled UnauthorizedException with message {}", exception.getMessage());
    return HttpUtil.getRestErrorResponse(UNAUTHORIZED, exception.getMessage(), httpServletRequest);
  }
}
