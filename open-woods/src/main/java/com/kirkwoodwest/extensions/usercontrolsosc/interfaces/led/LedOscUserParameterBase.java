package com.kirkwoodwest.extensions.usercontrolsosc.interfaces.led;

import com.bitwig.extension.controller.api.Parameter;
import com.kirkwoodwest.openwoods.led.Led;
import com.kirkwoodwest.openwoods.osc.OscHost;

import java.util.function.Supplier;

public class LedOscUserParameterBase extends Led<Parameter> {
  boolean sendValuesAfterReceived;
  boolean receivedNewValue = false;

  public LedOscUserParameterBase(Supplier<Parameter> supplier, OscHost oscHost, String oscBasePath) {
    super(oscHost, oscBasePath, supplier);
  }

  public void sendValuesAfterReceived(boolean sendValuesAfterReceived) {
    this.sendValuesAfterReceived = sendValuesAfterReceived;
  }
}
