package com.kirkwoodwest.openwoods.osc.adapters;

import com.bitwig.extension.api.opensoundcontrol.OscConnection;
import com.bitwig.extension.api.opensoundcontrol.OscMessage;
import com.bitwig.extension.controller.api.RangedValue;
import com.bitwig.extension.controller.api.SettableIntegerValue;
import com.bitwig.extension.controller.api.SettableRangedValue;
import com.kirkwoodwest.openwoods.osc.OscController;

public class SettableRangedValueOscAdapter extends RangedValueOscAdapter {
  SettableRangedValue dataSource;
  public SettableRangedValueOscAdapter(SettableRangedValue rangedValue, OscController oscController, String oscTarget, String oscDescription, boolean useFloats) {
    super(rangedValue, oscController, oscTarget, oscDescription, useFloats);
    oscController.registerOscCallback(oscTarget,  ",i", oscDescription, this::oscValueChange);
  }

  private void oscValueChange(OscConnection oscConnection, OscMessage oscMessage) {
    Double value = oscMessage.getDouble(0);
    dataSource.set(value);
    setDirty(true);
  }
}
