package ar.exception;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ControllerExceptionHandler {

  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseStatus(value = HttpStatus.UNSUPPORTED_MEDIA_TYPE)
  public void handleUnsupportedMediaType() {}

  @ExceptionHandler(DuplicateKeyException.class)
  public void handleDuplicateKeyException(DuplicateKeyException e) {
    System.out.println("YO!!!!!!");
    System.out.println(e.getMostSpecificCause());
    System.out.println(e.getRootCause());

  }

}
