package ar.entity.listener;

import ar.entity.User;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the {@link EntityEventListener} class.
 * 
 * @author adam
 *
 */
public class EntityEventListenerTests {

  /**
   * The {@link EntityEventListener} being tested.
   */
  private EntityEventListener entityEventListener;

  /**
   * {@link Entity} used to test that Strings are trimmed before create and save.
   */
  private User entityWithWhitespace;

  @Before
  public void before() {
    entityEventListener = new EntityEventListener();

    entityWithWhitespace = new User();
    entityWithWhitespace.setLastName("lastName");
    entityWithWhitespace.setUsername("   username  ");
    entityWithWhitespace.setCreatedBy("  createdBy  ");
  }

  @Test
  public void onBeforeCreate_entityWithWhitespace() {
    entityEventListener.onBeforeCreate(entityWithWhitespace);

    String lastName = entityWithWhitespace.getLastName();
    String username = entityWithWhitespace.getUsername();
    String createdBy = entityWithWhitespace.getCreatedBy();

    Assert.assertEquals(null, entityWithWhitespace.getFirstName());
    Assert.assertEquals(lastName.trim(), lastName);
    Assert.assertEquals(username.trim(), username);
    Assert.assertEquals(createdBy.trim(), createdBy);
  }

  @Test
  public void onBeforeSave_entityWithWhitespace() {
    entityEventListener.onBeforeSave(entityWithWhitespace);

    String lastName = entityWithWhitespace.getLastName();
    String username = entityWithWhitespace.getUsername();
    String createdBy = entityWithWhitespace.getCreatedBy();

    Assert.assertEquals(null, entityWithWhitespace.getFirstName());
    Assert.assertEquals(lastName.trim(), lastName);
    Assert.assertEquals(username.trim(), username);
    Assert.assertEquals(createdBy.trim(), createdBy);
  }

}
