package ar.configuration;

import ar.entity.User;
import ar.entity.validation.UserValidator;
import ar.repository.UserRepository;
import ar.test.util.TestUtils;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.scheduling.support.CronTrigger;

import java.util.Calendar;
import java.util.Date;

/**
 * Tests for the MongoDB configuration.
 *
 * @author adam
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class MongoConfigurationTests {

  @InjectMocks
  private MongoConfiguration mongoConfiguration;

  @Mock
  private UserRepository userRepository;

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

  @Test
  public void clearAndSeedDatabase() {
    mongoConfiguration.clearAndSeedDatabase();
    Mockito.verify(userRepository).deleteAll();
    Mockito.verify(userRepository, Mockito.times(3)).save(Mockito.any(User.class));
  }

  @Test
  public void clearDatabase_cronExpression() {
    CronTrigger cronTrigger = new CronTrigger("0 0 4 1 * *");

    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.YEAR, 2016);
    calendar.set(Calendar.MONTH, Calendar.MARCH);
    calendar.set(Calendar.DATE, 1);
    calendar.set(Calendar.HOUR_OF_DAY, 4);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);

    Date nextExecutionTime = cronTrigger.nextExecutionTime(TestUtils.createTriggerContext(calendar.getTime()));
    calendar.set(Calendar.MONTH, Calendar.APRIL);
    Assert.assertEquals(calendar.getTime(), nextExecutionTime);

    nextExecutionTime = cronTrigger.nextExecutionTime(TestUtils.createTriggerContext(calendar.getTime()));
    calendar.set(Calendar.MONTH, Calendar.MAY);
    Assert.assertEquals(calendar.getTime(), nextExecutionTime);
  }

}
