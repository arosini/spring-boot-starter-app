package ar.exception;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Catches exceptions thrown from controllers and converts them to an appropriate web response.
 * 
 * @author adam
 *
 */
@ControllerAdvice
public class ControllerExceptionHandler {

  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseStatus(value = HttpStatus.UNSUPPORTED_MEDIA_TYPE)
  public void handleUnsupportedMediaType() {}

  @ExceptionHandler(DuplicateKeyException.class)
  public void handleDuplicateKeyException(DuplicateKeyException e) {
    System.out.println("YO!!!!!!");
    System.out.println(e.getMessage());
    String model, field, value;

    String message = e.getMessage();

    Pattern pattern = Pattern.compile("\\.(.*?) index: (.*?) dup key: \\{ : \"(.*?)\" \\}\"");
    Matcher matcher = pattern.matcher(message);
    if (matcher.find()) {
      model = matcher.group(1);
      field = matcher.group(2);
      value = matcher.group(3);
    }

  }

}
