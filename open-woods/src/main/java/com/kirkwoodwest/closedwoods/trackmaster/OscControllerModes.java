package com.kirkwoodwest.closedwoods.trackmaster;

import com.kirkwoodwest.openwoods.osc.OscController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OscControllerModes {
  private final OscController oscController;
  private final String oscPath;

  private String currentMode = "------";
  private HashMap<String, List<String>> modeGroups = new HashMap<>();
  private String previousMode;

  public OscControllerModes(OscController oscController, String oscPath){
    this.oscController = oscController;
    this.oscPath = oscPath;
  }

  public void createMode(String name) {
    if(!modeGroups.containsKey(name)) {

      //Setup OSC Callback...
      this.oscController.registerOscCallback(oscPath + "/" + name,  "Set Mode (" + name + ")", (oscConnection, oscMessage) -> {
        setMode(name);
        oscController.requestFlush();
      });
      modeGroups.put(name, new ArrayList<>());
    }
  }

  public void setMode(String mode) {
    //disable current mode
    if(modeGroups.containsKey(currentMode)) {
      modeGroups.get(currentMode).forEach((group) -> {
        oscController.setGroupEnabled(group, false);
      });
    }

    //enable new mode
    modeGroups.get(mode).forEach((group) -> {
      oscController.setGroupEnabled(group, true);
    });

    //set current mode
    previousMode = currentMode;
    currentMode = mode;
  }

  public void disableAllModes() {
    modeGroups.forEach((mode, groups) -> {
      groups.forEach((group) -> {
        oscController.setGroupEnabled(group, false);
      });
    });
  }

  //Modes are exclusive and turn themselves off when switching to another mode.
  public void addGroup(String mode, String groupName) {
    if(modeGroups.containsKey(mode)) {
      modeGroups.get(mode).add(groupName);
    }
  }
}
