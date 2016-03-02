package ar.configuration;

import ar.entity.User;
import ar.entity.validation.UserValidator;
import ar.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Configuration for MongoDB repositories.
 * 
 * @author adam
 *
 */
@Configuration
@EnableMongoAuditing
public class MongoConfiguration extends RepositoryRestConfigurerAdapter {

  /**
   * Manipulates {@link User} entities.
   */
  @Autowired
  private UserRepository userRepository;

  /**
   * {@link AuditorAware} implementation which MongoDB uses to populate @CreatedBy and @LastModifiedBy properties.
   * 
   * @return The auditing object for MongoDB documents.
   */
  @Bean
  public AuditorAware<String> auditorAware() {
    return new AuditorAware<String>() {
      @Override
      public String getCurrentAuditor() {
        return null;
      }
    };
  }

  /**
   * Configures event listeners used for validation before saving to the database.
   */
  @Override
  public void configureValidatingRepositoryEventListener(ValidatingRepositoryEventListener validatingListener) {
    validatingListener.addValidator("beforeCreate", new UserValidator());
    validatingListener.addValidator("beforeSave", new UserValidator());
  }

  /**
   * Clears the database at 4:00 AM at the start of every month.
   */
  @Scheduled(cron = "0 0 4 1 * *")
  public void clearAndSeedDatabase() {
    userRepository.deleteAll();
    createUsers();
  }

  private void createUsers() {
    createUser("1", "Charles", "Xavier", "Professor X");
    createUser("2", "Scott", "Summers", "Cyclops");
    createUser("3", "Alex", "Summers", "Havok");
  }

  private void createUser(String id, String firstName, String lastName, String username) {
    User user = new User();
    user.setId(id);
    user.setFirstName(firstName);
    user.setLastName(lastName);
    user.setUsername(username);
    userRepository.save(user);
  }

}
