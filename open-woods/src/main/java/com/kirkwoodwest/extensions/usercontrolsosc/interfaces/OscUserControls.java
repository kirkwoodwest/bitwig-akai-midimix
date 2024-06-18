package com.kirkwoodwest.extensions.usercontrolsosc.interfaces;

import com.bitwig.extension.controller.api.*;
import com.kirkwoodwest.extensions.usercontrolsosc.interfaces.led.LedOscUserParameter;
import com.kirkwoodwest.extensions.usercontrolsosc.interfaces.led.LedOscUserParameterBase;
import com.kirkwoodwest.extensions.usercontrolsosc.interfaces.led.LedOscUserParameterValuesOnly;
import com.kirkwoodwest.openwoods.osc.OscController;
import com.kirkwoodwest.openwoods.osc.OscHost;

import java.util.ArrayList;

import static com.kirkwoodwest.utils.StringUtil.padInt;

public class OscUserControls {
  public static final String LED_GROUP_ID = "user controls";

  private final ControllerHost host;
  private final ArrayList<LedOscUserParameterBase> leds = new ArrayList<>();
  private final boolean indexPaddingEnabled;
  private int indexPaddingCount;
  private OscHost oscHost;

  public OscUserControls(ControllerHost host, OscController oscController, int userControlsCount, String oscAddress, boolean indexPaddingEnabled, boolean valuesOnlyMode) {
    this.host = host;
    this.oscHost = oscController.getOscHost();
    this.indexPaddingEnabled = indexPaddingEnabled;

    if (indexPaddingEnabled) {
      this.indexPaddingCount = 4;
    }

    UserControlBank userControlBank = host.createUserControls(userControlsCount);

    for (int i = 0; i < userControlsCount; i++) {
      Parameter parameter = userControlBank.getControl(i);
      parameter.value().markInterested();
      String indexString = getIndexString(i + 1); //Use index 1
      String path = oscAddress + indexString;
      if (oscAddress.isEmpty()) continue;
      if (!valuesOnlyMode) {
        oscController.addParameter(LED_GROUP_ID, path, parameter, "User Control Parameter.");
      } else {
        //Values Only Mode
//        LedOscUserParameterBase led = new LedOscUserParameterValuesOnly(LedOscUserParameterValuesOnly.getSupplier(parameter), oscHost, oscAddress + indexString, "User Control " + indexString + " value");
//        oscController.addSettableIntegerValue(LED_GROUP_ID, parameter.value());
//        oscController.addParameter(LED_GROUP_ID, path, parameter, "User Control Parameter.");
//       
//        this.leds.add(led);
//        oscController.addLed(LED_GROUP_ID, led);
      }
    }
  }

  public String getIndexString(int index) {
    if (indexPaddingEnabled) return padInt(this.indexPaddingCount, index);
    return String.valueOf(index);
  }


  public void setSendValuesAfterReceived(boolean b) {
    this.leds.forEach(led -> {
      if (led != null) {
        led.sendValuesAfterReceived(b);
      }
    });
  }
}
