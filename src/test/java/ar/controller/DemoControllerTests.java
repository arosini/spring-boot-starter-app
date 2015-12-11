package ar.controller;

import ar.model.DemoModel;

import org.junit.Before;
import org.junit.Test;
import org.unitils.reflectionassert.ReflectionAssert;

public class DemoControllerTests {

  private DemoController controller;

  private String pathParameter;

  @Before
  public void before() {
    controller = new DemoController();

    pathParameter = "pathParameter";
  }

  @Test
  public void demoControllerMethod() {
    DemoModel expected = new DemoModel();
    expected.setDemoField(pathParameter);

    DemoModel actual = controller.demoControllerMethod(pathParameter);

    ReflectionAssert.assertReflectionEquals(expected, actual);
  }

}
