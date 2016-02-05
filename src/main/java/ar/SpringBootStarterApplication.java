package ar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * A Spring Boot application for demonstration purposes.
 * 
 * @author adam
 * 
 */
@SpringBootApplication
public class SpringBootStarterApplication {

  /**
   * Runs the {@link SpringBootApplication} this class represents.
   * 
   * @param args Command line arguments.
   */
  public static void main(String[] args) {
    SpringApplication.run(SpringBootStarterApplication.class, args);
  }

}
