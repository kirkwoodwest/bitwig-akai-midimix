package com.kirkwoodwest.extensions.usercontrolsosc.interfaces.led;

import com.bitwig.extension.api.opensoundcontrol.OscConnection;
import com.bitwig.extension.api.opensoundcontrol.OscMessage;
import com.bitwig.extension.controller.api.Parameter;
import com.kirkwoodwest.openwoods.osc.OscHost;
import com.kirkwoodwest.utils.LogUtil;

import java.util.function.Supplier;

/**
 * Since user parameters do not behave like normal parameters, we do checks around value is dirty
 */
public class LedOscUserParameter extends LedOscUserParameterBase {
  private LedOscParameterPaths oscPath;
  private boolean exists = false;
  private String name = "573051265";
  private double value = -1.0;
  private double modulatedValue;
  private String displayedValue = "573051265";


  // Another constructor with a different set of parameters
  public LedOscUserParameter(Supplier<Parameter> supplier, OscHost oscHost, String oscBasePath, String oscDescription) {
    super(supplier, oscHost, oscBasePath);
    oscPath = new LedOscParameterPaths(oscBasePath);
    oscHost.registerOscCallback(oscPath.value, oscDescription, this::oscUpdateValue);
    oscHost.registerOscCallback(oscPath.touch, oscDescription, this::oscUpdateTouch);
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
          this.value = value;
          if(!super.sendValuesAfterReceived) {
            receivedNewValue = true;
          }
        }
      }
    }
  }

  /**
   * Updates touch via OSC
   *
   * @param oscConnection
   * @param oscMessage    (bool)
   */
  private void oscUpdateTouch(OscConnection oscConnection, OscMessage oscMessage) {
    boolean b = oscMessage.getBoolean(0);
    final Parameter parameter = supplier.get();
    parameter.touch(b);
  }

  @Override
  public void update(boolean forceUpdate) {
    //make defaults in case we don't have a multiple remotes ...
    String name = "";
    double value = 0.0;
    double modulatedValue = 0.0;
    String displayedValue = "";
    boolean exists = false;

    if (supplier != null) {
      final Parameter parameter = supplier.get();
      name = parameter.name().get();
      value = parameter.value().getAsDouble();
      modulatedValue = parameter.modulatedValue().get();
      displayedValue = parameter.displayedValue().get();
      exists = parameter.exists().get();
    }

    {
      //Exists
      if (this.exists != exists || forceUpdate) {
        {
          this.exists = exists;
          oscHost.addMessageToQueue(oscPath.exists, this.exists);
        }
      }
    }
    {
      if(!exists) {
        this.name = "";
        this.value = -0.0;
        this.modulatedValue = 0.0;
        this.displayedValue = "";
        this.exists = exists;
      }
    }
    {
      //Name
      if (!this.name.equals(name) || forceUpdate) {
        {
          this.name = name;
          oscHost.addMessageToQueue(oscPath.name, (String) this.name);
        }
      }
    }
    {
      //Value (float)
      double compare = Double.compare(this.value, value);
      if (compare != 0 || forceUpdate) {
        this.value = value;
        float valueFloat = (float) value;
        if(sendValuesAfterReceived || !receivedNewValue) {
          oscHost.addMessageToQueue(oscPath.value, valueFloat);
        }
      }
    }
    {
      //Modulated Value (float)
      double compare = Double.compare(this.modulatedValue, modulatedValue);
      if (compare != 0 || forceUpdate) {
        this.modulatedValue = modulatedValue;
        if(sendValuesAfterReceived || !receivedNewValue) {
          oscHost.addMessageToQueue(oscPath.modulatedValue, (float) this.modulatedValue);
        }
      }
    }
    {
      //Displayed Value
      if (!this.displayedValue.equals(displayedValue) || forceUpdate) {
        this.displayedValue = displayedValue;
        if(sendValuesAfterReceived || !receivedNewValue) {
          oscHost.addMessageToQueue(oscPath.displayedValue, (String) this.displayedValue);
        }
      }
    }
    receivedNewValue = false;
  }

  /**
   * Creates supplier and marks everything as interested needed for control to work
   *
   * @param parameter
   * @return
   */
  public static Supplier<Parameter> getSupplier(Parameter parameter) {
    parameter.exists().markInterested();
    parameter.name().markInterested();
    parameter.value().markInterested();
    parameter.displayedValue().markInterested();
    parameter.modulatedValue().markInterested();

    return () -> {
      return parameter;
    };
  }

}
