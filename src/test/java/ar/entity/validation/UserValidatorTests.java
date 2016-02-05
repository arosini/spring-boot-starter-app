package ar.entity.validation;

import ar.entity.Entity;
import ar.entity.User;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

/**
 * Tests for the {@link UserValidator} class.
 * 
 * @author adam
 *
 */
public class UserValidatorTests {

  private UserValidator userValidator;
  private User user;

  @Before
  public void before() {
    userValidator = new UserValidator();
    user = new User();
  }

  @Test
  public void supports_userClass() {
    Assert.assertTrue(userValidator.supports(User.class));
  }

  @Test
  public void supports_otherClass() {
    Assert.assertFalse(userValidator.supports(Entity.class));
  }

  @Test
  public void validate_validUser() {
    user.setUsername("username");
    Errors errors = new BeanPropertyBindingResult(user, "user");

    userValidator.validate(user, errors);

    Assert.assertFalse(errors.hasErrors());
  }

  @Test
  public void validate_nullUsername() {
    user.setUsername(null);
    Errors errors = new BeanPropertyBindingResult(user, "user");

    userValidator.validate(user, errors);

    FieldError usernameError = errors.getFieldError("username");
    Assert.assertEquals(1, errors.getErrorCount());
    Assert.assertEquals("username", usernameError.getField());
    Assert.assertEquals("username.empty", usernameError.getCode());
    Assert.assertEquals(null, usernameError.getRejectedValue());
  }

  @Test
  public void validate_blankUsername() {
    user.setUsername("");
    Errors errors = new BeanPropertyBindingResult(user, "user");

    userValidator.validate(user, errors);

    FieldError usernameError = errors.getFieldError("username");
    Assert.assertEquals(1, errors.getErrorCount());
    Assert.assertEquals("username", usernameError.getField());
    Assert.assertEquals("username.empty", usernameError.getCode());
    Assert.assertEquals("", usernameError.getRejectedValue());
  }

  @Test
  public void validate_whitespaceUsername() {
    user.setUsername("  ");
    Errors errors = new BeanPropertyBindingResult(user, "user");

    userValidator.validate(user, errors);

    FieldError usernameError = errors.getFieldError("username");
    Assert.assertEquals(1, errors.getErrorCount());
    Assert.assertEquals("username", usernameError.getField());
    Assert.assertEquals("username.empty", usernameError.getCode());
    Assert.assertEquals("  ", usernameError.getRejectedValue());
  }

}
