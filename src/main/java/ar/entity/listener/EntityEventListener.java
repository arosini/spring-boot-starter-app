package ar.entity.listener;

import ar.entity.Entity;

import lombok.extern.java.Log;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * Handles repository events for any entity.
 * 
 * @author adam
 *
 */
@Component
@RepositoryEventHandler(Entity.class)
@Log
public class EntityEventListener {

  /**
   * Triggers before an entity is created.
   * 
   * @param entity
   *          The entity being created.
   */
  @HandleBeforeCreate
  public void onBeforeCreate(Entity entity) {
    trimStrings(entity);
  }

  /**
   * Triggered before an entity is saved.
   * 
   * @param entity
   *          The entity being saved.
   */
  @HandleBeforeSave
  public void onBeforeSave(Entity entity) {
    trimStrings(entity);
  }

  /**
   * Trims all String values in the provided entity.
   * 
   * @param entity
   *          The entity to trim string values from.
   */
  private void trimStrings(Entity entity) {
    Class<?> clazz = entity.getClass();

    try {
      while (!clazz.equals(Object.class)) {
        for (Field field : clazz.getDeclaredFields()) {
          field.setAccessible(true);
          Object value = field.get(entity);
          if (field.getType().equals(String.class) && value != null) {
            field.set(entity, value.toString().trim());
          }
        }

        clazz = clazz.getSuperclass();
      }
    } catch (IllegalAccessException e) {
      log.severe("IllegalAccessException occurred while trimming String value before save for class " + clazz + ": "
          + e.getLocalizedMessage());
    }
  }

}
