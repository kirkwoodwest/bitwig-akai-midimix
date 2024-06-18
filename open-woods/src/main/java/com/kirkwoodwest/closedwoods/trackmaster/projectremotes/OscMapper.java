package com.kirkwoodwest.closedwoods.trackmaster.projectremotes;

import com.bitwig.extension.api.Color;
import com.bitwig.extension.controller.api.RemoteControl;
import com.kirkwoodwest.openwoods.savelist.ColorCollection;
import com.kirkwoodwest.openwoods.savelist.StringCollection;
import com.kirkwoodwest.openwoods.remotecontrol.OwRemoteControl;

import java.util.ArrayList;

public abstract class OscMapper {
  private final OscMappers oscMappers;
  final OscMapperPaths oscPaths;
  private int selectedIndex = -1;

  private final ArrayList<OwRemoteControl> owRemoteControls;
  private final ColorCollection colors;
  private final StringCollection trackNames;
  private final StringCollection deviceNames;
  private RemoteControl selectedRemoteControl;

  public OscMapper(OscMappers oscMappers, String oscPath, ArrayList<OwRemoteControl> remoteControls, ColorCollection colors, StringCollection trackNames, StringCollection deviceNames) {
    this.oscMappers = oscMappers;
    this.oscPaths = new OscMapperPaths(oscPath);
    this.owRemoteControls = remoteControls;
    this.colors = colors;
    this.trackNames = trackNames;
    this.deviceNames = deviceNames;
  }

  public void setActiveMapper() {
    oscMappers.setActiveMapper(this);
  }

  public void setSelectedIndex(int i) {
    this.selectedIndex = i;
    if(selectedIndex != -1) {
      selectedRemoteControl = owRemoteControls.get(i).getRemoteControl();
      setActiveMapper();
    }
  }

  public int getSelectedIndex() {
    return selectedIndex;
  }

  public ColorCollection getColors() {
    return colors;
  }

  public void setColor(Color color) {
    colors.setColor(selectedIndex, color);
  }

  public String getSelectedName() {
    if(selectedIndex == -1) return "";
    if(selectedRemoteControl == null) return "";
    return selectedRemoteControl.name().get();
  }
  public String getSelectedTrackName() {
    if(selectedIndex == -1) return "";
    return trackNames.getString(selectedIndex);
  }
  public String getSelectedDeviceName() {
    if(selectedIndex == -1) return "";
    return deviceNames.getString(selectedIndex);
  }
  public void setActiveColor() {
    setColor(colors.getTempColor());
  }
  public void setActiveTrackName() {
    trackNames.setString(selectedIndex, trackNames.getTempString());
  }
  public void setActiveDeviceName() {
    deviceNames.setString(selectedIndex, deviceNames.getTempString());
  }
  public RemoteControl getActiveRemote() {
    return selectedRemoteControl;
  }
  public Color getSelectedColor() {
    if(selectedIndex == -1) return Color.blackColor();
    return colors.getColor(selectedIndex);
  }
}
