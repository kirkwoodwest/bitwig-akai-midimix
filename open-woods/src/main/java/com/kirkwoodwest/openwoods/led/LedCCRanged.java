package com.kirkwoodwest.openwoods.led;

import com.bitwig.extension.controller.api.MidiOut;
import java.util.function.Supplier;

public class LedCCRanged extends Led<Integer> {
  public LedCCRanged(MidiOut midiOut, int channel, int status, int cc, Supplier<Integer> supplier){
    super(midiOut, channel, status, cc, supplier);
  }

  public Integer getEmptyData(){
    return 0;
  }
}
