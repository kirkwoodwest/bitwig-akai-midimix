package com.kirkwoodwest.openwoods.osc.adapters;

import com.bitwig.extension.api.opensoundcontrol.OscConnection;
import com.bitwig.extension.api.opensoundcontrol.OscMessage;
import com.bitwig.extension.controller.api.SettableIntegerValue;
import com.kirkwoodwest.openwoods.osc.OscController;
import com.kirkwoodwest.openwoods.osc.OscHost;

public class SettableIntegerValueOscAdapter extends IntegerValueOscAdapter {
  public SettableIntegerValueOscAdapter(SettableIntegerValue dataSource, OscController oscController, String oscTarget, String oscDescription) {
    super(dataSource, oscController, oscTarget, oscDescription);
    oscController.registerOscCallback(oscTarget,  ",i", oscDescription, this::oscValueChange);
  }

  private void oscValueChange(OscConnection oscConnection, OscMessage oscMessage) {
    int value = oscMessage.getInt(0);
    ((SettableIntegerValue) dataSource).set(value);
    setDirty(true);
  }
}
