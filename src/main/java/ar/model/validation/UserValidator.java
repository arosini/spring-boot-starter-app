package ar.model.validation;

import ar.model.User;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

  @Override
  public boolean supports(Class<?> clazz) {
    System.out.println("SUPPORTS: " + clazz + ": " + User.class.equals(clazz));
    return User.class.equals(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    System.out.println("VALIDATING!");
    ValidationUtils.rejectIfEmpty(errors, "firstName", "firstName.empty");
    ValidationUtils.rejectIfEmpty(errors, "lastName", "lastName.empty");
  }

}
