package org.ar.controller;

import org.ar.model.DemoModel;
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

  @RequestMapping("/{pathVariable}")
  public DemoModel demoControllerMethod(@PathVariable String pathVariable) {
    DemoModel demoModel = new DemoModel();
    demoModel.setDemoField(pathVariable);

    return demoModel;
  }

}
