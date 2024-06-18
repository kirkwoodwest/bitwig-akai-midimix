package com.kirkwoodwest.openwoods.osc.adapters;

import com.bitwig.extension.api.opensoundcontrol.OscConnection;
import com.bitwig.extension.api.opensoundcontrol.OscMessage;
import com.bitwig.extension.controller.api.SettableStringValue;
import com.kirkwoodwest.openwoods.osc.OscController;
import com.kirkwoodwest.openwoods.osc.OscHost;

public class SettableStringValueOscAdapter extends StringValueOscAdapter {
  private final SettableStringValue settableStringValue;

  public SettableStringValueOscAdapter(SettableStringValue dataSource, OscController oscController, String oscTarget, String oscDescription) {
    super(dataSource, oscController, oscTarget, oscDescription);
    settableStringValue = dataSource;
    oscController.registerOscCallback(oscTarget, ",s", oscDescription, this::oscValueChange);
  }

  public void oscValueChange(OscConnection oscConnection, OscMessage oscMessage) {
    String value = oscMessage.getString(0);
    settableStringValue.set(value);
    setDirty(true);
  }
}
