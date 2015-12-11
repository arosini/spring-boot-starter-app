package ar.model;

import com.google.common.reflect.ClassPath;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Before;
import org.junit.Test;
import org.meanbean.test.BeanTester;

import java.io.IOException;
import java.lang.reflect.Modifier;

public class ModelTests {

  private static final String MODEL_PACKAGE = "ar.model";

  private BeanTester beanTester;

  @Before
  public void before() {
    beanTester = new BeanTester();
  }

  @Test
  public void testModels() throws IOException {
    // Loop through classes in the model package
    final ClassLoader loader = Thread.currentThread().getContextClassLoader();
    for (final ClassPath.ClassInfo info : ClassPath.from(loader).getTopLevelClassesRecursive(MODEL_PACKAGE)) {
      final Class<?> clazz = info.load();

      // Skip abstract classes, interfaces and this class.
      int modifiers = clazz.getModifiers();
      if (Modifier.isAbstract(modifiers) || Modifier.isInterface(modifiers) || clazz.equals(this.getClass())) {
        continue;
      }

      // Test getters, setters and #toString
      beanTester.testBean(clazz);

      // Test #equals and #hashCode
      EqualsVerifier.forClass(clazz).suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS).verify();
    }
  }

}
