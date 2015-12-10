package ar.controller;

import ar.model.DemoModel;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A controller for demonstration purposes.
 * 
 * @author adam
 *
 */
@RestController
@RequestMapping("/demo")
public class DemoController {

  /**
   * A controller method for demonstration purposes.
   * 
   * @param pathVariable
   *          A path variable for demonstration purposes.
   * 
   * @return The provided path variable as a value in a {@link DemoModel}
   */
  @RequestMapping("/{pathVariable}")
  public DemoModel demoControllerMethod(@PathVariable String pathVariable) {
    DemoModel demoModel = new DemoModel();
    demoModel.setDemoField(pathVariable);

    return demoModel;
  }

}
