package com.kirkwoodwest.openwoods.osc.adapters;

import com.bitwig.extension.api.opensoundcontrol.OscConnection;
import com.bitwig.extension.api.opensoundcontrol.OscMessage;
import com.bitwig.extension.controller.api.SettableBooleanValue;
import com.kirkwoodwest.openwoods.osc.OscController;
import com.kirkwoodwest.openwoods.osc.OscHost;

public class SettableBooleanValueOscAdapter extends BooleanValueOscAdapter {
  private final SettableBooleanValue settableBooleanValue;

  public SettableBooleanValueOscAdapter(SettableBooleanValue dataSource, OscController oscController, String oscTarget, String oscDescription) {
    super(dataSource, oscController, oscTarget, oscDescription);
    oscController.registerOscCallback(oscTarget, "*", oscDescription, this::setValueOsc);
    settableBooleanValue = dataSource;
  }

  private void setValueOsc(OscConnection oscConnection, OscMessage oscMessage) {
    String typeTag = oscMessage.getTypeTag();
    if (typeTag.equals(",T")) {
      settableBooleanValue.set(true);
    } else if (typeTag.equals(",F")) {
      settableBooleanValue.set(false);
    }
  }

  @Override
  public void flush() {
    if (dirty || forceFlush) {
      oscController.addMessageToQueue(oscPath, dataSource.get());
      dirty = false;
      forceFlush = false;
    }
  }
}
