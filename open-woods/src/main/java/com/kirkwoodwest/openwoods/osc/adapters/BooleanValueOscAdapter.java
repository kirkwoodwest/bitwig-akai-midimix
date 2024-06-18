package com.kirkwoodwest.openwoods.osc.adapters;

import com.bitwig.extension.controller.api.BooleanValue;
import com.kirkwoodwest.openwoods.osc.OscController;
import com.kirkwoodwest.openwoods.osc.OscHost;

public class BooleanValueOscAdapter extends OscAdapter<BooleanValue> {

  public BooleanValueOscAdapter(BooleanValue booleanValue, OscController oscController, String oscTarget, String oscDescription) {
    super(booleanValue, oscController, oscTarget, oscDescription);
    booleanValue.addValueObserver(this::valueChanged);

    getAdapterInfo().add(oscPath, oscDescription + "", "T/F");
  }

  private void valueChanged(boolean b) {
    setDirty(true);
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
