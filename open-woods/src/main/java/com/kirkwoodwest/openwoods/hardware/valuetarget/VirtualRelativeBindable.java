package com.kirkwoodwest.openwoods.hardware.valuetarget;


import com.bitwig.extension.controller.api.AbsoluteHardwarControlBindable;
import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.RelativeHardwarControlBindable;

import java.util.function.Consumer;

//Creates a bindable object with callback
public class VirtualRelativeBindable {
  private final RelativeHardwarControlBindable bindable;
  private final RelativeValueTarget target;

  public VirtualRelativeBindable(ControllerHost host, Consumer<Double> observer) {
    target = new RelativeValueTarget(true);
    target.addValueObserver(observer);
    bindable = host.createRelativeHardwareControlAdjustmentTarget(target);
  }

  public RelativeHardwarControlBindable get() {
    return bindable;
  }

  public void setValue(double value) {
    target.setValue(value);
  }

  public Double getValue() {
    return target.get();
  }
}
