package com.kirkwoodwest.closedwoods.status;

import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.DocumentState;
import com.bitwig.extension.controller.api.Signal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigStatus {

  private final ControllerHost host;
  //List of all the config functions
  Map<String, ConfigState> states = new HashMap<>();
  List<String> stateOrder = new ArrayList<>();

  public ConfigStatus(ControllerHost host, String path) {
    this.host = host;
    DocumentState documentState = host.getDocumentState();
    //create string to store config status


    //Create button to reset config.
    Signal signalSetting = documentState.getSignalSetting("Do Config", "Config Status", "Do Config");
    signalSetting.addSignalObserver(this::reset);
  }

  public void addConfig(String id, Runnable sequence, int delay) {
    states.put(id, new ConfigState(sequence, delay));
    stateOrder.add(id);
  }


  private void reset() {
    //loop through state order and get config states and then schedule tasks
    for (String state : stateOrder) {
      ConfigState configState = states.get(state);
      host.scheduleTask(configState.configFunction(), configState.delay());
    }
  }
}
