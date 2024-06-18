package com.kirkwoodwest.closedwoods.trackmaster.projectremotes;

public class OscMappersPaths {
  public final String MAP;
  public final String SELECTED_NAME;
  public final String SELECTED_TRACK_NAME;
  public final String SELECTED_DEVICE_NAME;

  public final String SELECTED_COLOR;
  public final String SELECTED_DISPLAY_VALUE;
  public final String SELECTED_VALUE;

  public OscMappersPaths(String oscPath) {
    MAP = oscPath + "/map";
    SELECTED_NAME = oscPath + "/selected_name";
    SELECTED_TRACK_NAME = oscPath + "/selected_track_name";
    SELECTED_DEVICE_NAME = oscPath + "/selected_device_name";
    SELECTED_COLOR = oscPath + "/selected_color";
    SELECTED_DISPLAY_VALUE = oscPath + "/selected_display_value";
    SELECTED_VALUE = oscPath + "/selected_value";
  }
}
