package com.kirkwoodwest.settings;

import com.bitwig.extension.ExtensionDefinition;
import com.bitwig.extension.callback.NoArgsCallback;
import com.bitwig.extension.controller.api.*;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.kirkwoodwest.utils.FileUtil;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class PreferencesJson {
  private final Preferences preferences;
  private final ControllerHost host;
  private final Map<String, Value<?>> settings = new LinkedHashMap<>();

  //Singleton Constructor
  private static PreferencesJson instance = null;
  private final String version;
  private String fileAndPath;

  public PreferencesJson(ControllerHost host, ExtensionDefinition definition){
    preferences = host.getPreferences();
    this.host = host;

    version = host.getHostVersion();
  }

  public void createSaveLoadSettings(String fileName){
    //load settings
    this.fileAndPath = FileUtil.getPath(host, fileName);
    getSignalSetting("Save", "Settings", "Save", this::saveSettings);
    getSignalSetting("Load", "Settings", "Load", this::loadSettings);
  }

  public SettableBooleanValue getBooleanSetting(String name, String category, boolean defaultValue){
    SettableBooleanValue setting = preferences.getBooleanSetting(name, category, defaultValue);
    storeSetting(name, category, setting);
    return setting;
  }

  public SettableRangedValue getNumberSetting(String name, String category, double min, double max, double stepResolution, String unit, double intialValue){
    SettableRangedValue setting = preferences.getNumberSetting(name, category, min, max, stepResolution, unit, intialValue);
    storeSetting(name, category,  setting);
    return setting;
  }

  public SettableEnumValue getEnumSetting(String name, String category, String[] options, String initialValue){
    SettableEnumValue setting = preferences.getEnumSetting(name, category, options, initialValue);
    storeSetting(name, category,  setting);
    return setting;
  }

  public SettableStringValue getStringSetting(String name, String category, int numChars, String initialValue){
    SettableStringValue setting = preferences.getStringSetting(name, category, numChars, initialValue);
    storeSetting(name, category,  setting);
    return setting;
  }

  public Signal getSignalSetting(String name, String category, String action, NoArgsCallback callback) {
    Signal signalSetting = preferences.getSignalSetting(name, category, action);
    signalSetting.addSignalObserver(callback);
    return signalSetting;
  }

  private void storeSetting(String name, String category, Value<?> setting) {
    setting.markInterested();
    settings.put(category + "-" + name, setting);
  }




  public void saveSettings() {
    Map<String, Object> jsonMap = new HashMap<>();
    settings.forEach((name, setting) -> {
      if (setting instanceof SettableBooleanValue) {
        jsonMap.put(name, ((SettableBooleanValue) setting).get());
      } else if (setting instanceof SettableRangedValue) {
        jsonMap.put(name, ((SettableRangedValue) setting).get());
      } else if (setting instanceof SettableEnumValue) {
        jsonMap.put(name, ((SettableEnumValue) setting).get());
      } else if (setting instanceof SettableStringValue) {
        jsonMap.put(name, ((SettableStringValue) setting).get());
      }
    });

    String jsonString = JSON.toJSONString(jsonMap, true); // `true` enables pretty-print
    try (FileWriter writer = new FileWriter(fileAndPath)) {
      writer.write(jsonString);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void loadSettings() {
    try (FileReader reader = new FileReader(fileAndPath)) {
      JSONObject jsonObject = JSON.parseObject(reader, JSONObject.class);
      jsonObject.forEach((name, value) -> {
        Value<?> setting = settings.get(name);
        if (setting instanceof SettableBooleanValue) {
          ((SettableBooleanValue) setting).set((Boolean) value);
        } else if (setting instanceof SettableRangedValue) {
          ((SettableRangedValue) setting).set(((Number) value).doubleValue());
        } else if (setting instanceof SettableEnumValue) {
          ((SettableEnumValue) setting).set((String) value);
        } else if (setting instanceof SettableStringValue) {
          ((SettableStringValue) setting).set((String) value);
        }
      });
      host.restart();
    } catch (IOException e) {
      e.printStackTrace();
    }
    // Notify listeners
    // listeners.forEach((listener) -> listener.onSettingsJsonChanged(this));
  }

  public void addListener(PreferencesJsonListener listener){
    //add listener
    //listeners.add(listener);
  }


}
