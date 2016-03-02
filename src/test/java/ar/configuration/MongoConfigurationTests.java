package ar.configuration;

import ar.entity.validation.UserValidator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;

/**
 * Tests for the MongoDB configuration.
 *
 * @author adam
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class MongoConfigurationTests {

  private MongoConfiguration mongoConfiguration;

  @Before
  public void before() {
    mongoConfiguration = new MongoConfiguration();
  }

  @Test
  public void auditorAware() {
    AuditorAware<String> auditorAware = mongoConfiguration.auditorAware();
    Assert.assertNotNull(auditorAware);
  }

  @Test
  public void auditorAwareGetCurentAuditor() {
    AuditorAware<String> auditorAware = mongoConfiguration.auditorAware();
    String currentAuditor = auditorAware.getCurrentAuditor();
    Assert.assertNull(currentAuditor);
  }

  @Test
  public void configureValidatingRepositoryEventListener() {
    ValidatingRepositoryEventListener validatingRepositoryEventListener = Mockito
        .mock(ValidatingRepositoryEventListener.class);

    mongoConfiguration.configureValidatingRepositoryEventListener(validatingRepositoryEventListener);

    Mockito.verify(validatingRepositoryEventListener).addValidator(Mockito.eq("beforeCreate"),
        Mockito.any(UserValidator.class));
    Mockito.verify(validatingRepositoryEventListener).addValidator(Mockito.eq("beforeSave"),
        Mockito.any(UserValidator.class));
  }

}
