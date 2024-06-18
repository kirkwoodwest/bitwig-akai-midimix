package com.kirkwoodwest.extensions.oscprojectfader.fader;

import com.bitwig.extension.api.opensoundcontrol.OscConnection;
import com.bitwig.extension.api.opensoundcontrol.OscMessage;
import com.bitwig.extension.controller.api.Parameter;
import com.kirkwoodwest.openwoods.led.Led;
import com.kirkwoodwest.openwoods.osc.OscHost;
import com.kirkwoodwest.utils.LogUtil;

import java.util.function.Supplier;

public class LedFaderParameter extends Led<Parameter> {
  private String oscPath;
  private final String oscPathValue;
  private final String oscPathExists;

  private double value = -1.0;
  private boolean exists = false;

  public LedFaderParameter(Supplier<Parameter> supplier, OscHost oscHost, String oscPath, String oscDescription) {
    super(oscHost, oscPath, supplier);
    this.oscPath = oscPath;
    this.oscPathValue = oscPath + "/value";
    this.oscPathExists = oscPath + "/value";
    oscHost.registerOscCallback(oscPath + "/value", oscDescription, this::oscUpdateValue);
  }

  /**
   * Updates value cvia OSC
   *
   * @param oscConnection
   * @param oscMessage    (int)
   */
  private void oscUpdateValue(OscConnection oscConnection, OscMessage oscMessage) {
    if (supplier != null) {
      double value = 0;

      if(oscMessage.getTypeTag().equals(",f")) {
        value = (double) oscMessage.getFloat(0);
      }

      Parameter parameter = supplier.get();
      if (parameter != null && parameter.exists().get()) {
        if(value < 0 || value > 1.0) {
          LogUtil.println("WARNING OSC PARAMETER VALUE OUT OF RANGE: [ " + oscTarget + " ] : " + value);
        } else {
          parameter.setImmediately(value);
        }
      }
    }
  }
  public void update(boolean forceUpdate) {
    //make defaults in case we don't have a multiple remotes ...
    double value = 0.0;
    boolean exists = false;

    if (supplier != null) {
      final Parameter parameter = supplier.get();
      value = parameter.value().getAsDouble();
      exists = parameter.exists().get();
    }
    {
      //Value (float)
      double compare = Double.compare(this.value, value);
      if (compare != 0 || forceUpdate) {
        this.value = value;
        float valueFloat = (float) value;
        oscHost.addMessageToQueue(oscPath + "/value", valueFloat);
      }
    }
    {
      //Exists (bool)
      boolean compare = this.exists != exists;
      if (compare || forceUpdate) {
        this.exists = exists;
        oscHost.addMessageToQueue(oscPath + "/exists", exists);
      }
    }
  }

  /**
   * Creates supplier and marks everything as interested needed for control to work
   *
   * @param parameter
   * @return
   */
  public static Supplier<Parameter> getSupplier(Parameter parameter) {
    parameter.value().markInterested();
    parameter.exists().markInterested();
    return () -> {
      return parameter;
    };
  }
}
