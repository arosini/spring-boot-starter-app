package ar.exception;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.dao.DuplicateKeyException;

import java.util.Map;

/**
 * Tests the {@link ControllerExceptionHandler} class.
 * 
 * @author adam
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ControllerExceptionHandlerTests {

  @InjectMocks
  private ControllerExceptionHandler controllerExceptionHandler;

  @Mock
  private MessageSource messageSource;

  @Test
  public void handleHttpMessageNotReadableException() {
    controllerExceptionHandler.handleHttpMessageNotReadableException();
  }

  @Test
  public void handleDuplicateKeyException_messageCodeFound() {
    Mockito.when(messageSource.getMessage(Mockito.eq("dupField.unique"), Mockito.any(), Mockito.any()))
        .thenReturn("DupField must be unique");

    DuplicateKeyException duplicateKeyException = new DuplicateKeyException(
        "E11000 duplicate key error collection: db.entity index: dupField dup key: { : \\\"invalidValue\\\" }");

    Map<String, String> resultError = controllerExceptionHandler.handleDuplicateKeyException(duplicateKeyException)
        .get("errors").get(0);

    Assert.assertEquals("Entity", resultError.get("entity"));
    Assert.assertEquals("DupField must be unique", resultError.get("message"));
    Assert.assertEquals("invalidValue", resultError.get("invalidValue"));
    Assert.assertEquals("dupField", resultError.get("property"));
  }

  @Test
  public void handleDuplicateKeyException_messageCodeNotFound() {
    Mockito.when(messageSource.getMessage(Mockito.eq("dupField.unique"), Mockito.any(), Mockito.any()))
        .thenThrow(new NoSuchMessageException(null));

    DuplicateKeyException duplicateKeyException = new DuplicateKeyException(
        "E11000 duplicate key error collection: db.entity index: dupField dup key: { : \\\"invalidValue\\\" }");

    Map<String, String> resultError = controllerExceptionHandler.handleDuplicateKeyException(duplicateKeyException)
        .get("errors").get(0);

    Assert.assertEquals("Entity", resultError.get("entity"));
    Assert.assertEquals("DupField must be unique", resultError.get("message"));
    Assert.assertEquals("invalidValue", resultError.get("invalidValue"));
    Assert.assertEquals("dupField", resultError.get("property"));
  }

  @Test
  public void handleDuplicateKeyException_invalidExceptionMessage() {
    DuplicateKeyException duplicateKeyException = new DuplicateKeyException("");

    Map<String, String> resultError = controllerExceptionHandler.handleDuplicateKeyException(duplicateKeyException)
        .get("errors").get(0);

    Assert.assertNull(resultError.get("entity"));
    Assert.assertNull(resultError.get("message"));
    Assert.assertNull(resultError.get("invalidValue"));
    Assert.assertNull(resultError.get("property"));
  }

}
