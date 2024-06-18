package com.kirkwoodwest.openwoods.osc.adapters;

import com.bitwig.extension.controller.api.ColorValue;
import com.kirkwoodwest.openwoods.osc.OscController;
import com.kirkwoodwest.openwoods.osc.OscHost;

public class ColorValueOscAdapter extends OscAdapter<ColorValue> {
  public ColorValueOscAdapter(ColorValue colorValue, OscController oscController, String oscTarget, String oscDescription) {
    super(colorValue, oscController, oscTarget, oscDescription);
    colorValue.addValueObserver(this::valueChanged);
    getAdapterInfo().add(oscPath, oscDescription + " Color RGBA", ",f,f,f,f");
  }

  private void valueChanged(float r, float g, float b) {
    setDirty(true);
  }

  @Override
  public void flush() {
    if (dirty || forceFlush) {
      oscController.addMessageToQueue(oscPath, (float) dataSource.get().getRed(), (float) dataSource.get().getGreen(), (float) dataSource.get().getBlue(), (float) dataSource.get().getAlpha());
      dirty = false;
      forceFlush = false;
    }
  }
}
