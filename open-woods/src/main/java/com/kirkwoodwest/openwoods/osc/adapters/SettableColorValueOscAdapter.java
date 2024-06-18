package com.kirkwoodwest.openwoods.osc.adapters;

import com.bitwig.extension.api.opensoundcontrol.OscConnection;
import com.bitwig.extension.api.opensoundcontrol.OscMessage;
import com.bitwig.extension.controller.api.SettableColorValue;
import com.kirkwoodwest.openwoods.osc.OscController;
import com.kirkwoodwest.openwoods.osc.OscHost;

public class SettableColorValueOscAdapter extends ColorValueOscAdapter {

  public SettableColorValueOscAdapter(SettableColorValue settableColorValue, OscController oscController, String oscTarget, String oscDescription) {
    super(settableColorValue, oscController, oscTarget, oscDescription);
    oscController.registerOscCallback(oscTarget, ",f,f,f", oscDescription, this::oscValueChange);
  }

  private void oscValueChange(OscConnection oscConnection, OscMessage oscMessage) {
    int red = oscMessage.getInt(0);
    int green = oscMessage.getInt(1);
    int blue = oscMessage.getInt(2);
    int alpha = oscMessage.getInt(3);
    ((SettableColorValue) dataSource).set(red, green, blue, alpha);
  }
}
