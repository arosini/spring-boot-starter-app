package ar.model;

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
  private String createdBy;

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

}
