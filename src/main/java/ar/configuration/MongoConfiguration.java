package ar.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

@Configuration
@EnableMongoAuditing
public class MongoConfiguration extends RepositoryRestMvcConfiguration {

  /**
   * {@link AuditorAware} implementation which MongoDB uses to populate fields such as @CreatedBy and @LastModifiedBy.
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

}
