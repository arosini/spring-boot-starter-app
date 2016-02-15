package ar.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a user of the system.
 * 
 * @author adam
 * 
 */
@Document
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class User extends Entity {

  ////////////
  // Fields //
  ////////////

  /** The user's first name. */
  private String firstName;

  /** The user's last name. */
  private String lastName;

  /** The user's alias. */
  @Indexed(unique = true)
  private String username;

}
