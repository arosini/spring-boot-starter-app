package ar.test.util;

import org.springframework.scheduling.TriggerContext;

import java.util.Date;

/**
 * Contains helper methods for unit tests.
 * 
 * @author adam
 *
 */
public class TestUtils {

  /**
   * Creates a {@link TriggerContext} with all values set to the provided {@link Date}.
   * 
   * @param lastExecutionTime When the trigger was last executed.
   * @return A {@link TriggerContext} with all values set to the provided {@link Date}.
   */
  public static TriggerContext createTriggerContext(Date lastExecutionTime) {
    return new TriggerContext() {
      @Override
      public Date lastScheduledExecutionTime() {
        return lastExecutionTime;
      }

      @Override
      public Date lastActualExecutionTime() {
        return lastExecutionTime;
      }

      @Override
      public Date lastCompletionTime() {
        return lastExecutionTime;
      }
    };
  }

}
