// Written by Kirkwood West - kirkwoodwest.com
// (c) 2020
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt


package com.kirkwoodwest.extensions.commander;

import com.bitwig.extension.controller.ControllerExtension;
import com.bitwig.extension.controller.api.*;
import com.kirkwoodwest.openwoods.osc.OscHost;
import com.kirkwoodwest.openwoods.superbank.SuperBank;
import com.kirkwoodwest.utils.LogUtil;

import java.util.HashMap;

import static com.kirkwoodwest.utils.LogUtil.reportExtensionStatus;

public class CommanderExtension extends ControllerExtension {
  //Class Variables
  private ControllerHost host;
  private HashMap<String, Action> selectActions = new HashMap<>();
  private SuperBank superBank;
  private SceneBank sceneBank;

  protected CommanderExtension(final CommanderExtensionDefinition definition, final ControllerHost host) {
    super(definition, host);
  }

  @Override
  public void init() {
    host = getHost();
    LogUtil.init(host);

    Preferences preferences = host.getPreferences();

    Commander commander = new Commander(host);
    /*
    Application application = host.createApplication();
    Action[] actions = application.getActions();
    for (Action action : actions) {
      host.println(action.getId() + " : " + action.getName());
    }
    this.selectActions.put("Select Track 1", application.getAction("select_track1"));
    this.selectActions.put("Select Track 2", application.getAction("select_track2"));
    this.selectActions.put("Select Track 3", application.getAction("select_track3"));
    this.selectActions.put("Select Track 4", application.getAction("select_track4"));
    this.selectActions.put("Select Track 5", application.getAction("select_track5"));
    this.selectActions.put("Select Track 6", application.getAction("select_track6"));
    this.selectActions.put("Select Track 7", application.getAction("select_track7"));
    this.selectActions.put("Select Track 8", application.getAction("select_track8"));
    this.selectActions.put("Select Track 9", application.getAction("select_track9"));
*/
    OscHost oscHost = new OscHost(host, "/commander");

    //Callback for all commands
    oscHost.registerOscCallback("/commander", "Commander executes String received", (oscConnection, oscMessage)->{
      String typeTag = oscMessage.getTypeTag();
      if (typeTag.equals(",s")) {
        String commandString = oscMessage.getString(0);
        commander.doCommand(commandString);

      }
    });

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
  }
}
