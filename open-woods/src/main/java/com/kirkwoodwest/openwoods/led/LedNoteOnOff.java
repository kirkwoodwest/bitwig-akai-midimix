package com.kirkwoodwest.openwoods.led;

import com.bitwig.extension.api.util.midi.ShortMidiMessage;
import com.bitwig.extension.controller.api.MidiOut;

import java.util.function.Supplier;

public class LedNoteOnOff extends Led<Boolean> {
  private final int brightness;
  private final int offBrightness;

  public LedNoteOnOff(MidiOut midiOut, int channel, int note, Supplier<Boolean> supplier, int brightness, int offBrightness){
    super(midiOut, channel, ShortMidiMessage.NOTE_ON,  note, supplier);
    this.brightness = brightness;
    this.offBrightness = offBrightness;
  }

  @Override
  public void sendData(Boolean value) {
    if(value==null) return;
    if(value == true) {
      midiOut.sendMidi(status, data2, brightness);
    } else {
      midiOut.sendMidi(status, data2, offBrightness);
    }
  }
}
