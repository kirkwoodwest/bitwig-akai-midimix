package com.kirkwoodwest.closedwoods.trackmaster.projectremotes;

import com.bitwig.extension.controller.api.CursorRemoteControlsPage;
import com.kirkwoodwest.closedwoods.osc.OwRemoteControlOscAdapter;
import com.kirkwoodwest.openwoods.osc.OscController;
import com.kirkwoodwest.openwoods.remotecontrol.OwRemoteControl;

public class OscProjectRemotes {

  public static final String ID = "PROJECT_REMOTES";

  public static void create(ProjectRemotes projectRemotes, OscController oscController, String oscPath) {
    //create leds for osc controller based on the project remotes
    int numRemotes = projectRemotes.getRemoteCount();
    int numParameters = projectRemotes.getNumParameters();
    for (int i = 0; i < numRemotes; i++) {
      CursorRemoteControlsPage cursorRemotePage = projectRemotes.getCursorRemotePage(i);
      cursorRemotePage.getName().markInterested();
      String pagePath = oscPath + "/" + (i+1);
      oscController.addStringValue("PROJECT_REMOTES", pagePath + "/name", cursorRemotePage.getName());

      //create led for remote
      for (int j = 0; j < numParameters; j++) {
        //create led for parameter
        OwRemoteControl owRemoteControl = projectRemotes.getRemoteControl(i, j);
        String remotePath = pagePath + "/" + (j+1);
        OwRemoteControlOscAdapter adapter = new OwRemoteControlOscAdapter(owRemoteControl, oscController, remotePath, "Project Remote " + i + " / " + j);
        oscController.addAdapter("PROJECT_REMOTES", adapter);
      }
    }
  }
}
