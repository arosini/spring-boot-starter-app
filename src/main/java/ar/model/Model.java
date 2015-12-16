package ar.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Defines fields found in all models.
 * 
 * @author adam
 *
 */
@Data
@Document
@EqualsAndHashCode
@ToString
public abstract class Model {

  /**
   * The model's unique identifier.
   */
  @Id
  private String id;

  /**
   * Who created the model instance.
   */
  @CreatedBy
  protected String createdBy;

  /**
   * When the model instance was created.
   */
  @CreatedDate
  private Date createdDate;

  /**
   * Who last modified the model instance.
   */
  @LastModifiedBy
  private String lastModifiedBy;

  /**
   * When the model instance was last modified.
   */
  @LastModifiedDate
  private Date lastModifiedDate;

  // Override Date getters / setters

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

  // Utility methods

  protected Date cloneDate(Date date) {
    Date clone = null;

    if (date != null) {
      clone = (Date) date.clone();
    }

    return clone;
  }

}
