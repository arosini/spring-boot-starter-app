package ar.model;

import com.google.common.reflect.ClassPath;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.meanbean.test.BeanTester;

import java.lang.reflect.Modifier;

public class ModelTests {

  private static final String MODEL_PACKAGE = "ar.model";

  private BeanTester beanTester;

  @Before
  public void before() {
    beanTester = new BeanTester();
  }

  @Test
  public void testModels() throws Exception {
    final ClassLoader loader = Thread.currentThread().getContextClassLoader();

    // Loop through classes in the model package
    for (final ClassPath.ClassInfo info : ClassPath.from(loader).getTopLevelClassesRecursive(MODEL_PACKAGE)) {
      final Class<?> clazz = info.load();
      int modifiers = clazz.getModifiers();

      // Skip interfaces and non-model classes (such as this test class)
      if (Modifier.isInterface(modifiers) || !(clazz.isAssignableFrom(Model.class))) {
        continue;
      }

      // Test only the #equals and #hashCode methods for abstract classes
      if (Modifier.isAbstract(modifiers)) {
        EqualsVerifier.forClass(clazz).suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS).verify();
        continue;
      }

      // Test getters, setters and #toString
      beanTester.testBean(clazz);

      // Test #equals and #hashCode
      EqualsVerifier.forClass(clazz).withRedefinedSuperclass()
          .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS).verify();

      // Verify not equals with subclass (for code coverage with Lombok generated equals method)
      Assert.assertFalse(clazz.newInstance().equals(createDynamicSubClassInstance(clazz.getName())));
    }
  }

  private static Object createDynamicSubClassInstance(String superClassName)
      throws NotFoundException, CannotCompileException, InstantiationException, IllegalAccessException {
    ClassPool pool = ClassPool.getDefault();

    // Create the class.
    CtClass subClass = pool.makeClass(superClassName + "Extended");
    final CtClass superClass = pool.get(superClassName);
    subClass.setSuperclass(superClass);
    subClass.setModifiers(Modifier.PUBLIC);

    // Add a canEquals method
    final CtMethod ctmethod = CtNewMethod
        .make("public boolean canEqual(Object o) { return o instanceof " + superClassName + "Extended; }", subClass);
    subClass.addMethod(ctmethod);

    return subClass.toClass().newInstance();
  }

}
