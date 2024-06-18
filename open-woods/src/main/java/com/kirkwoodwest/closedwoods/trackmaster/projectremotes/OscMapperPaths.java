package com.kirkwoodwest.closedwoods.trackmaster.projectremotes;

public class OscMapperPaths {
  private final String path;
  public final String MAP;
  public String SELECTED_NAME;
  public String SELECTED_TRACK_NAME;
  public String SELECTED_INDEX;

  public OscMapperPaths(String path) {
    this.path = path;
    MAP = path + "/map";
    SELECTED_NAME = path + "/selected_name";
    SELECTED_INDEX = path + "/selected_index";
    SELECTED_TRACK_NAME = path + "/selected_track_name";
  }

  public String getSelected(int index) {
    return path + "/" + index + "/selected/";
  }

  public String getColor(int index) {
    return path + "/" + index + "/color";
  }

  public String getExists(int index) {
    return path + "/" + index + "/exists";
  }

  public String getSelectedName(int index) {
    return path + "/" + index + "/selected_name";
  }

  public String getTrackName(int oscIndex) {
    return path + "/" + oscIndex + "/track_name";
  }
  public String getDeviceName(int oscIndex) {
    return path + "/" + oscIndex + "/device_name";
  }

  public String getRangeMin(int oscIndex) {
    return path + "/" + oscIndex + "/range/min";
  }

  public String getRangeMax(int oscIndex) {
    return path + "/" + oscIndex + "/range/max";
  }

  public String getRemote(int oscIndex) {
    return path + "/" + oscIndex;
  }
}
