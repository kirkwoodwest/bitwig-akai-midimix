package com.kirkwoodwest.openwoods.osc.adapters;

import com.bitwig.extension.controller.api.StringValue;
import com.kirkwoodwest.openwoods.osc.OscController;
import com.kirkwoodwest.openwoods.osc.OscHost;

public class StringValueOscAdapter extends OscAdapter<StringValue> {

  public StringValueOscAdapter(StringValue dataSource, OscController oscController, String oscTarget, String oscDescription) {
    super(dataSource, oscController, oscTarget, oscDescription);
    dataSource.addValueObserver(this::onValueChanged);

    getAdapterInfo().add(oscPath, oscDescription + "", ",s");
  }

  private void onValueChanged(String s) {
    setDirty(true);
  }

  @Override
  public void flush() {
    if (getDirty() || forceFlush) {
      oscController.addMessageToQueue(oscPath, dataSource.get());
      setDirty(false);
      forceFlush = false;
    }
  }
}
