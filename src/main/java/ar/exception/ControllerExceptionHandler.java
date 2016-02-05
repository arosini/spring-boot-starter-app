package ar.exception;

import org.apache.commons.lang3.text.WordUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

/**
 * Catches exceptions thrown from controllers and converts them to an appropriate web response.
 * 
 * @author adam
 *
 */
@ControllerAdvice
public class ControllerExceptionHandler {

  /**
   * Links to the validation messages based on the current locale.
   */
  @Resource(name = "messageSource")
  private MessageSource messageSource;

  /**
   * Converts a 400 status code caused by a {@link HttpMessageNotReadableException} to a 415.
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseStatus(value = HttpStatus.UNSUPPORTED_MEDIA_TYPE)
  public void handleHttpMessageNotReadableException() {}

  /**
   * Handles unique constraint errors which throw a {@link DuplicateKeyException}.
   * 
   * @param duplicateKeyException The original exception which was thrown.
   * @return A {@link Map} structured identically to a Spring validation {@link Errors}.
   */
  @ExceptionHandler(DuplicateKeyException.class)
  @ResponseStatus(value = HttpStatus.CONFLICT)
  @ResponseBody
  public Map<String, List<Map<String, String>>> handleDuplicateKeyException(
      DuplicateKeyException duplicateKeyException) {
    String entity = null;
    String message = null;
    String invalidValue = null;
    String property = null;

    String errorMessage = duplicateKeyException.getMessage();

    Pattern pattern = Pattern.compile("\\.(.*?) index: (.*?) dup key: \\{ : \\\\\"(.*?)\\\\\"");
    Matcher matcher = pattern.matcher(errorMessage);
    if (matcher.find()) {
      entity = WordUtils.capitalize(matcher.group(1));
      property = matcher.group(2).replace("_", "");
      invalidValue = matcher.group(3);

      try {
        message = messageSource.getMessage(property + ".unique", new Object[0], LocaleContextHolder.getLocale());
      } catch (NoSuchMessageException noSuchMessageException) {
        message = StringUtils.capitalize(property) + " must be unique";
      }
    }

    Map<String, String> uniqueIndexViolation = new HashMap<>();
    uniqueIndexViolation.put("entity", entity);
    uniqueIndexViolation.put("message", message);
    uniqueIndexViolation.put("invalidValue", invalidValue);
    uniqueIndexViolation.put("property", property);

    List<Map<String, String>> errors = new ArrayList<Map<String, String>>();
    errors.add(uniqueIndexViolation);

    Map<String, List<Map<String, String>>> responseBody = new HashMap<>();
    responseBody.put("errors", errors);

    return responseBody;
  }

}
