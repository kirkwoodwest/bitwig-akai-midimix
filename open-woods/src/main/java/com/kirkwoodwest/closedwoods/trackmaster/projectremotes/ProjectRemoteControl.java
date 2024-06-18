package com.kirkwoodwest.closedwoods.trackmaster.projectremotes;

import com.bitwig.extension.api.Color;
import com.bitwig.extension.controller.api.RemoteControl;


public class ProjectRemoteControl {

  private final RemoteControl remote;
  private Color color;

  public ProjectRemoteControl(RemoteControl remote, Color color) {
    remote.exists().markInterested();
    remote.name().markInterested();
    remote.value().markInterested();
    remote.displayedValue().markInterested();
    remote.isBeingMapped().markInterested();

    remote.setIndication(true);
    this.remote = remote;
    this.color = color;
  }


}
