package ar.model;

import com.google.common.reflect.ClassPath;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.meanbean.test.BeanTestException;
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
  public void testAbstractModels() throws IllegalArgumentException, BeanTestException, InstantiationException,
      IllegalAccessException, IOException, AssertionError, NotFoundException, CannotCompileException {
    // Loop through classes in the model package
    final ClassLoader loader = Thread.currentThread().getContextClassLoader();
    for (final ClassPath.ClassInfo info : ClassPath.from(loader).getTopLevelClassesRecursive(MODEL_PACKAGE)) {
      final Class<?> clazz = info.load();

      // Only test abstract classes
      if (Modifier.isAbstract(clazz.getModifiers())) {
        // Test #equals and #hashCode
        EqualsVerifier.forClass(clazz).suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS).verify();
      }
    }
  }

  @Test
  public void testConcreteModels()
      throws IOException, InstantiationException, IllegalAccessException, NotFoundException, CannotCompileException {
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
      EqualsVerifier.forClass(clazz).withRedefinedSuperclass()
          .suppress(Warning.STRICT_INHERITANCE, Warning.NONFINAL_FIELDS).verify();

      // Verify not equals with subclass (for code coverage with Lombok)
      Assert.assertFalse(clazz.newInstance().equals(createSubClassInstance(clazz.getName())));
    }
  }

  static Object createSubClassInstance(String superClassName)
      throws NotFoundException, CannotCompileException, InstantiationException, IllegalAccessException {
    ClassPool pool = ClassPool.getDefault();

    // Create the class.
    CtClass subClass = pool.makeClass(superClassName + "Extended");
    final CtClass superClass = pool.get(superClassName);
    subClass.setSuperclass(superClass);
    subClass.setModifiers(Modifier.PUBLIC);

    // Add a constructor which will call super( ... );
    CtClass[] params = new CtClass[] {};
    final CtConstructor ctor = CtNewConstructor.make(params, null, CtNewConstructor.PASS_PARAMS, null, null, subClass);
    subClass.addConstructor(ctor);

    // Add a canEquals method
    final CtMethod ctmethod = CtNewMethod
        .make("public boolean canEqual(Object o) { return o instanceof " + superClassName + "Extended; }", subClass);
    subClass.addMethod(ctmethod);

    return subClass.toClass().newInstance();
  }

}
