package com.kirkwoodwest.openwoods.oscthings;

import com.bitwig.extension.controller.api.Application;
import com.bitwig.extension.controller.api.ControllerHost;
import com.kirkwoodwest.openwoods.osc.OscController;

public class OscApplication {
  public String ID ="";
  //TODO: Add Project Value Observers
  public OscApplication(ControllerHost host, OscController oscController, String oscPath) {
    ID = oscPath;
    Application application = host.createApplication();
    application.projectName().addValueObserver(projectName -> {

    });
    oscController.addStringValue(oscPath, oscPath + "/project_name", application.projectName());
    oscController.addBooleanValue(oscPath, oscPath + "/has_active_engine", application.hasActiveEngine());

    //Activate Engine
    oscController.registerOscCallback(oscPath + "/activate_engine", "Activate Engine", (oscConnection, oscMessage) -> {
      application.activateEngine();
    });

    //Deactivate Engine
    oscController.registerOscCallback(oscPath + "/deactivate_engine", "Deactivate Engine", (oscConnection, oscMessage) -> {
      application.deactivateEngine();
    });
  }
}
