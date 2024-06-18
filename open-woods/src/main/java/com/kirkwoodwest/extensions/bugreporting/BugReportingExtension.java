// Written by Kirkwood West - kirkwoodwest.com
// (c) 2020
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt


package com.kirkwoodwest.extensions.bugreporting;

import com.bitwig.extension.controller.ControllerExtension;
import com.bitwig.extension.controller.api.Action;
import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.Preferences;
import com.bitwig.extension.controller.api.SceneBank;
import com.kirkwoodwest.openwoods.osc.OscController;
import com.kirkwoodwest.openwoods.osc.OscHost;
import com.kirkwoodwest.openwoods.oscthings.browser.OscBrowser;
import com.kirkwoodwest.openwoods.superbank.SuperBank;
import com.kirkwoodwest.utils.LogUtil;

import java.util.HashMap;

import static com.kirkwoodwest.utils.LogUtil.reportExtensionStatus;

public class BugReportingExtension extends ControllerExtension {
  //Class Variables
  private ControllerHost host;
  private HashMap<String, Action> selectActions = new HashMap<>();
  private SuperBank superBank;
  private SceneBank sceneBank;
  private OscHost oscHost;
  private OscController oscController;

  protected BugReportingExtension(final BugReportingExtensionDefinition definition, final ControllerHost host) {
    super(definition, host);
  }

  @Override
  public void init() {
    host = getHost();
    LogUtil.init(host);

    Preferences preferences = host.getPreferences();

    oscHost = new OscHost(host,  "/bugreporting");
    oscController = new OscController(host, oscHost, "/bugreporting");
    OscBrowser oscBrowser = new OscBrowser(host, oscController, "/bugreporting/browser");

    //If your reading this... I hope you say hello to a loved one today. <3
    reportExtensionStatus(this, "Initialized");
  }



  @Override
  public void exit() {
    // TODO: Perform any cleanup once the driver exits
    // For now just show a popup notification for verification that it is no longer running.
    reportExtensionStatus(this, "Exited");
  }

  @Override
  public void flush() {
    oscController.flush();
  }
}
