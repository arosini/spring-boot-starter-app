package ar.configuration;

import ar.model.validation.UserValidator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

@Configuration
@EnableMongoAuditing
public class MongoConfiguration extends RepositoryRestMvcConfiguration {

  /**
   * s {@link AuditorAware} implementation which MongoDB uses to populate fields such as @CreatedBy and @LastModifiedBy.
   * 
   * @return The auditing object for MongoDB documents.
   */
  @Bean
  public AuditorAware<String> auditor() {
    return new AuditorAware<String>() {
      @Override
      public String getCurrentAuditor() {
        return null;
      }
    };
  }

  @Override
  protected void configureValidatingRepositoryEventListener(ValidatingRepositoryEventListener validatingListener) {
    validatingListener.addValidator("beforeCreate", new UserValidator());
    validatingListener.addValidator("beforeSave", new UserValidator());
  }

}
