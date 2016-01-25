package ar.exception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.text.WordUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Catches exceptions thrown from controllers and converts them to an
 * appropriate web response.
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
  @ResponseStatus(value = HttpStatus.CONFLICT)
  @ResponseBody
  public Map<String, Object> handleDuplicateKeyException(DuplicateKeyException e) {
    String entity = null;
    String message = null;
    String invalidValue = null;
    String property = null;

    String errorMessage = e.getMessage();

    Pattern pattern = Pattern.compile("\\.(.*?) index: (.*?) dup key: \\{ : \\\\\"(.*?)\\\\\"");
    Matcher matcher = pattern.matcher(errorMessage);
    if (matcher.find()) {
      entity = WordUtils.capitalize(matcher.group(1));
      property = matcher.group(2);
      invalidValue = matcher.group(3);
    }

    message = WordUtils.capitalize(property) + " must be unique";

    Map<String, String> uniqueIndexViolation = new HashMap<>();
    uniqueIndexViolation.put("entity", entity);
    uniqueIndexViolation.put("message", message);
    uniqueIndexViolation.put("invalidValue", invalidValue);
    uniqueIndexViolation.put("property", property);

    List<Object> errors = new ArrayList<Object>();
    errors.add(uniqueIndexViolation);

    Map<String, Object> responseBody = new HashMap<>();
    responseBody.put("errors", errors);

    return responseBody;
  }

}
