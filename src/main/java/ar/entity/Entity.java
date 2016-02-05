package ar.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

import java.util.Date;

/**
 * Defines properties found in all entities.
 * 
 * @author adam
 *
 */
@Data
@EqualsAndHashCode
@ToString
public abstract class Entity {

  ////////////////
  // Properties //
  ////////////////

  /** The entity's unique identifier. */
  @Id
  private String id;

  /** The version of this entity. */
  @Version
  private Long version;

  /** Who created the entity. */
  @CreatedBy
  private String createdBy;

  /** When the entity was created. */
  @CreatedDate
  private Date createdDate;

  /** Who last modified the entity. */
  @LastModifiedBy
  private String lastModifiedBy;

  /** When the entity was last modified. */
  @LastModifiedDate
  private Date lastModifiedDate;

  /////////////
  // Methods //
  /////////////

  protected static Date cloneDate(Date date) {
    Date clone = null;

    if (date != null) {
      clone = (Date) date.clone();
    }

    return clone;
  }

  ///////////////////////////////
  // Getter / Setter Overrides //
  ///////////////////////////////

  public Date getCreatedDate() {
    return cloneDate(createdDate);
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = cloneDate(createdDate);
  }

  public Date getLastModifiedDate() {
    return cloneDate(lastModifiedDate);
  }

  public void setLastModifiedDate(Date lastModifiedDate) {
    this.lastModifiedDate = cloneDate(lastModifiedDate);
  }

}
