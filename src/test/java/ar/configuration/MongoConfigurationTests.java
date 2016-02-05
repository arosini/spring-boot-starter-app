package ar.configuration;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.AuditorAware;

/**
 * Tests for the MongoDB configuration.
 * 
 * @author adam
 *
 */
public class MongoConfigurationTests {

  private MongoConfiguration mongoConfiguration;

  @Before
  public void before() {
    mongoConfiguration = new MongoConfiguration();
  }

  @Test
  public void auditor() {
    AuditorAware<String> auditor = mongoConfiguration.auditor();
    Assert.assertNull(auditor.getCurrentAuditor());

  }

}
