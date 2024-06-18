package com.kirkwoodwest.closedwoods.loader;

import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.Preferences;
import com.bitwig.extension.controller.api.SettableStringValue;
import com.kirkwoodwest.closedwoods.loader.element.*;
import com.kirkwoodwest.openwoods.osc.OscController;
import com.kirkwoodwest.openwoods.trackbank.CursorTrackBank;
import com.kirkwoodwest.openwoods.trackbank.CursorTrackHelper;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Loader {

  private final SettableStringValue stringSetting;
  private final ControllerHost host;
  private final OscController oscController;
  private final CursorTrackHelper cursorTrackHelper;
  private final Map<String, ILoaderElement> elements;

  //Loader has a list of classes it can use in combination of a toml file
  //to load the classes and set them up
  //The loader will also have a list of classes that are loaded
  public Loader(ControllerHost host, OscController oscController, CursorTrackHelper cursorTrackHelper) {
    this.host = host;
    this.oscController = oscController;
    this.cursorTrackHelper = cursorTrackHelper;
    this.elements = new HashMap<>();
    Preferences preferences = host.getPreferences();
    stringSetting = preferences.getStringSetting("Flow OSC Configuration", "Flow OSC", 32, "/FlowOsc/FlowOSC.toml");
    preferences.getSignalSetting("Reload Flow OSC Configuration", "Flow OSC", "Reload Flow OSC Configuration").addSignalObserver(this::load);
  }

  public void load() {
//
//    Toml toml = new Toml().read(stringSetting.get());
//
//    // Since the selectors are in an array of tables, fetch them as a List of Maps
//    List<Map<String, Object>> selectors = toml.getList("selector");
//
//    //Selector Based Elements
//    for (Map<String, Object> selector : selectors) {
//      String id = (String) selector.get("id");
//      String oscPath = (String) selector.get("osc_path");
//      int numTracks = (Integer) selector.get("num_tracks");
//      String selection = (String) selector.get("selection");
//
//      //Get number of sends required for the mixer
//      int numSends = Mixer.getNumSends(selector);
//
//      //Get Number of Scenes required for the launcher
//      int numScenes = Launcher.getNumScenes(selector);
//
//      //Build TrackBank
//      CursorTrackBank cursorTrackBank = new CursorTrackBank(host, cursorTrackHelper, numTracks, numSends, numScenes, id);
//
//      //TODO: Setup configuration module for this based on oscPath & Selection
//
//      //Features
//      //Mixer
//      Map<String, ILoaderElement> mixerElement = Mixer.createElement(host, oscController, selector, cursorTrackBank, oscPath);
//      elements.putAll(mixerElement);
//
//      //Launcher
//      Map<String, ILoaderElement> launcherElement = Launcher.createElement(host, oscController, selector, cursorTrackBank, oscPath);
//      elements.putAll(launcherElement);
//
//      //Device
//      Map<String, ILoaderElement> deviceElement = Device.createElement(host, oscController, selector, cursorTrackBank, oscPath);
//
//
//      //VU
//     // VU.createElement(host, oscController, oscPath);
//      //Global Remotes
//
//    }
//
//    //Transport (only one element)
//    Toml transportData = toml.getTable("transport");
//    Transport.createElement(host, oscController, transportData);
  }

}
