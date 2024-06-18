package com.kirkwoodwest.openwoods.settings;

import com.bitwig.extension.controller.api.Action;
import com.bitwig.extension.controller.api.Application;
import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.DocumentState;

public class ShowScriptConsole {
  public static void create(ControllerHost host, DocumentState documentState ) {
    Action openControllerConsole = createAction(host);
    documentState.getSignalSetting("Console", "Console", "Open Controller Console").addSignalObserver(openControllerConsole::invoke);
  }
  public static Action createAction(ControllerHost host) {
    Application application = host.createApplication();
    Action openControllerConsole = application.getAction("show_controller_script_console");
    return openControllerConsole;
  }
}

