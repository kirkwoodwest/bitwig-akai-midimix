package com.kirkwoodwest.openwoods.osc.adapters;

import com.bitwig.extension.controller.api.IntegerValue;
import com.kirkwoodwest.openwoods.osc.OscController;
import com.kirkwoodwest.openwoods.osc.OscHost;

public class IntegerValueOscAdapter extends OscAdapter<IntegerValue> {
  public IntegerValueOscAdapter(IntegerValue integerValue, OscController oscController, String oscTarget, String oscDescription) {
    super(integerValue, oscController, oscTarget, oscDescription);
    integerValue.addValueObserver(this::valueChanged);
    getAdapterInfo().add(oscPath, oscDescription + "", ",i");
  }

  private void valueChanged(int i) {
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
