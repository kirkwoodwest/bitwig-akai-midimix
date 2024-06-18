package com.kirkwoodwest.extensions.usercontrolsosc.interfaces.led;

import com.bitwig.extension.api.opensoundcontrol.OscConnection;
import com.bitwig.extension.api.opensoundcontrol.OscMessage;
import com.bitwig.extension.controller.api.Parameter;
import com.kirkwoodwest.openwoods.osc.OscHost;
import com.kirkwoodwest.utils.LogUtil;

import java.util.function.Supplier;

public class LedOscUserParameterValuesOnly extends LedOscUserParameterBase {
  private String oscPath;
  private double value = -1.0;

  //Legacy reports parameter value ONLY!
  public LedOscUserParameterValuesOnly(Supplier<Parameter> supplier, OscHost oscHost, String oscBasePath, String oscDescription) {
    super(supplier, oscHost, oscBasePath);

    //Legacy juse t
    oscPath = oscBasePath;
    oscHost.registerOscCallback(oscPath, oscDescription, this::oscUpdateValue);
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
      if (parameter != null) {
        if(value < 0 || value > 1.0) {
          LogUtil.println("WARNING OSC PARAMETER VALUE OUT OF RANGE: [ " + oscTarget + " ] : " + value);
        } else {
          parameter.setImmediately(value);
          super.receivedNewValue = true;
        }
      }
    }
  }
  public void update(boolean forceUpdate) {
    //make defaults in case we don't have a multiple remotes ...
    double value = 0.0;
    if (supplier != null) {
      final Parameter parameter = supplier.get();
      value = parameter.value().getAsDouble();
    }
    {
      //Value (float)
      double compare = Double.compare(this.value, value);
      if (compare != 0 || forceUpdate) {
        this.value = value;
        float valueFloat = (float) value;

        boolean sendData = false;

        if(super.sendValuesAfterReceived) {
          //Always send out data after its received
          sendData = true;
        } else if (!sendValuesAfterReceived && !super.receivedNewValue) {
          //Only send out values if we havn't received a new value...
          sendData = true;
        }

        if(sendData) oscHost.addMessageToQueue(oscPath, valueFloat);
      }
    }
    super.receivedNewValue = false;
  }

  /**
   * Creates supplier and marks everything as interested needed for control to work
   *
   * @param parameter
   * @return
   */
  public static Supplier<Parameter> getSupplier(Parameter parameter) {
    parameter.value().markInterested();
    return () -> {
      return parameter;
    };
  }
}
