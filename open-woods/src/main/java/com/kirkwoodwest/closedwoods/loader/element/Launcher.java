package com.kirkwoodwest.closedwoods.loader.element;

import com.bitwig.extension.controller.api.ControllerHost;
import com.kirkwoodwest.closedwoods.launcher.OscClipLauncher;
import com.kirkwoodwest.openwoods.osc.OscController;
import com.kirkwoodwest.openwoods.trackbank.CursorTrackBank;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Launcher {
  public static Map<String, ILoaderElement> createElement(ControllerHost host, OscController oscController, Map<String, Object> selector, CursorTrackBank cursorTrackBank, String oscPath) {
    List<Map<String, Object>> launcher = (List<Map<String, Object>>) selector.get("selector.launcher");
    HashMap<String, ILoaderElement> elements = new HashMap<>();
    if (launcher != null) {
      for (Map<String, Object> mixer : launcher) {
        String launcherOscPath = (String) mixer.get("osc_path");
        launcherOscPath = oscPath + "/" + launcherOscPath;
        OscClipLauncher oscClipLauncher = new OscClipLauncher(host, cursorTrackBank, oscController, launcherOscPath);
        elements.put(launcherOscPath, oscClipLauncher);
      }
    }
    return elements;
  }
  public static int getNumScenes(Map<String, Object> selector) {
    int numScenes = 0;
    List<Map<String, Object>> launchers = (List<Map<String, Object>>) selector.get("selector.launcher");
    if (launchers != null) {
      for (Map<String, Object> mixer : launchers) {
        int count = (Integer) mixer.get("num_scenes");
        numScenes = Math.max(numScenes, count);
      }
    }
    return numScenes;
  }
}
