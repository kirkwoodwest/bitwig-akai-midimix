package com.kirkwoodwest.settings;

import com.bitwig.extension.controller.api.SettableEnumValue;
import com.kirkwoodwest.openwoods.settings.SettingsHelper;

import java.util.ArrayList;

public class ControlSettings {
  private final ArrayList<SettableEnumValue> settings = new ArrayList<>();
  private final SettableEnumValue globalSetting;

  //Handles creation of settings per row and per control
  public ControlSettings(PreferencesJson preferences, String category, String itemName, String[] globalModes, String[] modes, String initialMode){
    globalSetting = preferences.getEnumSetting("All", category, globalModes, initialMode);
    for (int i = 0; i < 8; i++) {
      final int index = i;
      SettableEnumValue setting = preferences.getEnumSetting(itemName + " " + (index+1), category, modes, initialMode);
      setting.markInterested();
      settings.add(setting);
    }

    //Observer for global setting which bashes the settings with the global setting
    globalSetting.addValueObserver((v)-> {

      boolean settingVisible = true;
      if (SettingsModes.globalProjectRemotesMap.containsKey(v)) {
        //If a project remote map override all settings with the project remote map
        String[] projectRemotesMap = SettingsModes.globalProjectRemotesMap.get(v);
        for (int i = 0; i < 8; i++) {
          settings.get(i).set(projectRemotesMap[i]);
        }
        settingVisible = false;
      } else if (!v.equals(SettingsModes.GLOBAL_CUSTOM) ) {
       //If Not Custom,  custom override all settings with the global setting
        for (int i = 0; i < 8; i++) {
          settings.get(i).set(v);
        }
        settingVisible = false;
      }

      for (int i = 0; i < 8; i++) {
        SettingsHelper.setVisible(settings.get(i), settingVisible);
      }
    });
  }

  public ArrayList<SettableEnumValue> getSettings() {
    return settings;
  }


}
