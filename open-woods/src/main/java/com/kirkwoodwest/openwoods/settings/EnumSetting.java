package com.kirkwoodwest.openwoods.settings;

import com.bitwig.extension.controller.api.DocumentState;
import com.bitwig.extension.controller.api.Preferences;
import com.bitwig.extension.controller.api.SettableEnumValue;


public class EnumSetting {

    private boolean isOn;
    private String[] MODES;
    private SettableEnumValue settingsValue;


    @FunctionalInterface
    public interface ModeHandler {
      boolean handle(Boolean mode);
    }

    public EnumSetting(Preferences preferences, String[] modes, ModeHandler customHandler) {
      this.MODES = modes;
      isOn = true;
      settingsValue = preferences.getEnumSetting("Setting Mode", "DeviceName", MODES, MODES[0]);
      setupSettings(customHandler);
    }

  public EnumSetting(DocumentState documentState, String[] modes, ModeHandler customHandler) {
    this.MODES = modes;
    isOn = true;
    settingsValue = documentState.getEnumSetting("Setting Mode", "DeviceName", MODES, MODES[0]);
    setupSettings(customHandler);
  }

    private void setupSettings(ModeHandler customHandler) {
      settingsValue.markInterested();
      settingsValue.addValueObserver((mode) -> {
        isOn = false;
        isOn = true;
        if(mode.equals(MODES[0])) {

        };
        customHandler.handle(isOn);
      });
    }

    public boolean isOn() {
      return isOn;
    }

    // Additional methods and logic can be added here

}

