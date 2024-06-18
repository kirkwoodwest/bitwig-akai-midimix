package com.kirkwoodwest.extensions.usercontrolsosc.interfaces.projectremotes;

import com.bitwig.extension.controller.api.CursorRemoteControlsPage;
import com.kirkwoodwest.extensions.usercontrolsosc.interfaces.led.LedOscPagedRemoteValuesOnly;
import com.kirkwoodwest.openwoods.osc.OscController;
import com.kirkwoodwest.openwoods.remotecontrol.OwRemoteControl;

public class OscProjectRemotes {
  public OscProjectRemotes(ProjectRemotes projectRemotes, OscController oscController, String oscPath, boolean valuesOnlyMode) {

    //TODO: Fix this...
//    //Create LEDs for osc controller based on the project remotes
//    int numRemotes = projectRemotes.getNumRemotes();
//    int numParameters = projectRemotes.getNumParameters();
//    for (int i = 0; i < numRemotes; i++) {
//      if(valuesOnlyMode) {
//        String pagePath = oscPath + "/" + (i + 1);
//        for (int j = 0; j < numParameters; j++) {
//          //create led for parameter
//          OwRemoteControl owRemoteControl = projectRemotes.getRemoteControl(i, j);
//          String remotePath = pagePath + "/" + (j + 1);
//          LedOscPagedRemoteValuesOnly led = new LedOscPagedRemoteValuesOnly(LedOscPagedRemoteValuesOnly.getSupplier(owRemoteControl), oscController.getOscHost(), remotePath, "User Control " + remotePath + " value");
//          oscController.addLed("PROJECT_REMOTES", led);
//        }
//      } else {
//        CursorRemoteControlsPage cursorRemotePage = projectRemotes.getCursorRemotePage(i);
//        cursorRemotePage.getName().markInterested();
//        String pagePath = oscPath + "/" + (i + 1);
//        oscController.addLedString("PROJECT_REMOTES", pagePath + "oscPath", cursorRemotePage.getName());
//
//        //create led for remote
//        for (int j = 0; j < numParameters; j++) {
//          //create led for parameter
//          OwRemoteControl remote = projectRemotes.getRemoteControl(i, j);
//          String remotePath = pagePath + "/" + (j + 1);
//          oscController.addLedOscPagedRemoteControl("PROJECT_REMOTES", remotePath, remote, "Project Remote " + i + " / " + j);
//        }
//      }
//    }
  }
}
