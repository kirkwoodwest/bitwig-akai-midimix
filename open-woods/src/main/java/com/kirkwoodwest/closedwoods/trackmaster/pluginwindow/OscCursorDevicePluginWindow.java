package com.kirkwoodwest.closedwoods.trackmaster.pluginwindow;

import com.kirkwoodwest.openwoods.osc.OscController;

public class OscCursorDevicePluginWindow {
  public static final String ID = "CursorDevicePluginWindow";
  public static void create(OscController oscController, String path, CursorDevicePluginWindow cursorDevicePluginWindow) {
    oscController.addBooleanValue("CursorDevicePluginWindow", path + "/plugin_open", cursorDevicePluginWindow.isPluginWindowsOpen());
    oscController.addBooleanValue("CursorDevicePluginWindow", path + "/plugin_exists", cursorDevicePluginWindow.pluginExists());
    oscController.registerOscCallback(path + "/plugin_open","Opens the currently selected plugin window",
            (oscConnection, oscMessage) -> {
              cursorDevicePluginWindow.openPluginWindows(oscMessage.getTypeTag().equals(",T"));
            });
  }
}
