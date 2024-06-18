package com.kirkwoodwest.openwoods.osc.adapters;

import com.bitwig.extension.controller.api.EnumValue;
import com.kirkwoodwest.openwoods.osc.OscController;
import com.kirkwoodwest.openwoods.osc.OscHost;

public class EnumValueOscAdapter extends OscAdapter<EnumValue> {

  public EnumValueOscAdapter(EnumValue enumValue, OscController oscController, String oscTarget, String oscDescription) {
    super(enumValue, oscController, oscTarget, oscDescription);
    enumValue.addValueObserver(this::updateValue);
    getAdapterInfo().add(oscPath, oscDescription + "", ",i");
  }

  private void updateValue(String s) {
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
