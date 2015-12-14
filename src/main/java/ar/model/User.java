package ar.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * Represents a user of the system.
 * 
 * @author adam
 * 
 */
@Data
public final class User {

  /**
   * The User's unique identifier.
   */
  @Id
  private String id;

  /**
   * The User's first name.
   */
  private String firstName;

  /**
   * The User's last name.
   */
  private String lastName;

}
