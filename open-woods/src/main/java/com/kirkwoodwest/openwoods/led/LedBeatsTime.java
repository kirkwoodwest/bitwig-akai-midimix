package com.kirkwoodwest.openwoods.led;

import com.bitwig.extension.controller.api.SettableBeatTimeValue;
import com.kirkwoodwest.openwoods.osc.OscHost;

import java.util.function.Supplier;

public class LedBeatsTime extends Led<SettableBeatTimeValue> {

  public LedBeatsTime(OscHost oscHost, String oscTarget, Supplier<SettableBeatTimeValue> supplier){
    super(oscHost, oscTarget, supplier);
  }

  @Override
  public boolean compare(SettableBeatTimeValue s){
    if(Double.compare(s.get(), super.localValue.get()) == 0) {
      return true;
    }
    return false;
  }

  @Override
  public void sendData(SettableBeatTimeValue value) {
    super.localValue = value;
    String beatTimeString = value.getFormatted();
    String[] beatTimeArray = beatTimeString.split(":");
    int bars = Integer.parseInt(beatTimeArray[0]);
    int beats = Integer.parseInt(beatTimeArray[1]);
    int subBeats = Integer.parseInt(beatTimeArray[2]);
    int ticks = Integer.parseInt(beatTimeArray[3]);
    super.oscHost.addMessageToQueue(oscTarget, bars, beats, subBeats, ticks);
  }

  public static Supplier<SettableBeatTimeValue> createSupplier(SettableBeatTimeValue value) {
    value.markInterested();
    return ()->{  return value;
    };
  }
}
