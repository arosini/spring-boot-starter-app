package ar.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Represents a user of the system.
 * 
 * @author adam
 * 
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class User extends Model {

  /**
   * The User's first name.
   */
  private String firstName;

  /**
   * The User's last name.
   */
  private String lastName;

}
