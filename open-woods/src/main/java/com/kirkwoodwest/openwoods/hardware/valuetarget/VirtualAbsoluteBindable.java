package com.kirkwoodwest.openwoods.hardware.valuetarget;


import com.bitwig.extension.controller.api.AbsoluteHardwarControlBindable;
import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.RelativeHardwarControlBindable;

import java.util.function.Consumer;

//Creates an observable Bindable Double
public class VirtualAbsoluteBindable {
  private final AbsoluteHardwarControlBindable bindable;
  private final AbsoluteValueTarget target;

  public VirtualAbsoluteBindable(ControllerHost host, Consumer<Double> observer) {
    target = new AbsoluteValueTarget();
    target.addValueObserver(observer);
    bindable = host.createAbsoluteHardwareControlAdjustmentTarget(target);
  }

  public AbsoluteHardwarControlBindable get() {
    return bindable;
  }
  public Double getValue() {
    return target.get();
  }
}
