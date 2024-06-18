package com.kirkwoodwest.extensions.usercontrolsosc.interfaces.led;

import com.bitwig.extension.api.opensoundcontrol.OscConnection;
import com.bitwig.extension.api.opensoundcontrol.OscMessage;
import com.bitwig.extension.controller.api.Parameter;
import com.kirkwoodwest.openwoods.led.Led;
import com.kirkwoodwest.openwoods.osc.OscHost;
import com.kirkwoodwest.openwoods.remotecontrol.OwRemoteControl;
import com.kirkwoodwest.utils.LogUtil;

import java.util.function.Supplier;

public class LedOscPagedRemoteValuesOnly extends Led<OwRemoteControl> {
  private final String oscPath;
  private double value = -1.0;

  private boolean sendValuesAfterReceived;
  private boolean valueIsDirty = false;
  private boolean exists;

  public void sendValuesAfterReceived(boolean sendValuesAfterReceived) {
    this.sendValuesAfterReceived = sendValuesAfterReceived;
  }

  //Legacy reports parameter value ONLY!
  public LedOscPagedRemoteValuesOnly(Supplier<OwRemoteControl> supplier, OscHost oscHost, String oscBasePath, String oscDescription) {
    super(oscHost, oscBasePath, supplier);
    oscPath = oscBasePath;
    oscHost.registerOscCallback(oscPath, oscDescription, this::oscUpdateValue);
  }

  /**
   * Updates value cvia OSC
   *
   * @param oscConnection (OscConnection)
   * @param oscMessage    (int)
   */
  private void oscUpdateValue(OscConnection oscConnection, OscMessage oscMessage) {
    if (supplier != null) {
      double value = 0;

      if(oscMessage.getTypeTag().equals(",f")) {
        value = (double) oscMessage.getFloat(0);
      }

      Parameter parameter = supplier.get().getRemoteControl();
      if (parameter != null) {
        if(value < 0 || value > 1.0) {
          LogUtil.println("WARNING OSC PARAMETER VALUE OUT OF RANGE: [ " + oscTarget + " ] : " + value);
        } else {
          parameter.setImmediately(value);
          this.value = value;
          if(!sendValuesAfterReceived) {
            valueIsDirty = true;
          }
        }
      }
    }
  }

  public void update(boolean forceUpdate) {
    //make defaults in case we don't have a multiple remotes ...
    double value = 0.0;
    if (supplier != null) {
      OwRemoteControl owRemoteControl = supplier.get();
      final Parameter parameter = supplier.get().getRemoteControl();
      value = parameter.value().getAsDouble();
      exists = owRemoteControl.exists();
    }
    {
      //Value (float)
      double compare = Double.compare(this.value, value);
      if (exists && (compare != 0 || forceUpdate)) {
        this.value = value;
        float valueFloat = (float) value;
        if(sendValuesAfterReceived && !valueIsDirty) {
          oscHost.addMessageToQueue(oscPath, valueFloat);
        }
      }
    }
    valueIsDirty = false;
  }

  /**
   * Creates supplier and marks everything as interested needed for control to work
   *
   * @param owRemoteControl  (PagedRemoteControl)
   * @return supplier to get remote control
   */
  public static Supplier<OwRemoteControl> getSupplier(OwRemoteControl owRemoteControl) {
    owRemoteControl.getRemoteControl().value().markInterested();
    return () -> owRemoteControl;
  }
}
