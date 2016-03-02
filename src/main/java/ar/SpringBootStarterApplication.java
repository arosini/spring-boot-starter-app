package ar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * A Spring Boot application for demonstration purposes.
 * 
 * @author adam
 * 
 */
@SpringBootApplication
@EnableConfigurationProperties
public class SpringBootStarterApplication {

  /**
   * Runs the {@link SpringBootApplication} class.
   * 
   * @param args Command line arguments.
   */
  public static void main(String[] args) {
    SpringApplication.run(SpringBootStarterApplication.class, args);
  }

}
