package com.kirkwoodwest.openwoods.osc.adapters;

import com.bitwig.extension.controller.api.DoubleValue;
import com.bitwig.extension.controller.api.RangedValue;
import com.kirkwoodwest.openwoods.osc.OscController;

public class RangedValueOscAdapter extends OscAdapter<RangedValue> {

  private final boolean useFloats;

  public RangedValueOscAdapter(RangedValue rangedValue, OscController oscController, String oscTarget, String oscDescription, boolean useFloats) {
    super(rangedValue, oscController, oscTarget, oscDescription);
    this.useFloats = useFloats;
    rangedValue.addValueObserver(this::valueChanged);
    getAdapterInfo().add(oscPath, oscDescription + "", ",f");
  }

  private void valueChanged(double i) {
    setDirty(true);
  }

  @Override
  public void flush() {
    if (dirty || forceFlush) {
      if (this.useFloats) {
        oscController.addMessageToQueue(oscPath, (float) dataSource.get());
      } else {
        oscController.addMessageToQueue(oscPath, dataSource.get());
      }
      dirty = false;
      forceFlush = false;
    }
  }
}
