package com.kirkwoodwest.extensions.hardware.MF3D;

import com.bitwig.extension.api.util.midi.ShortMidiMessage;
import com.bitwig.extension.controller.api.MidiOut;
import com.kirkwoodwest.openwoods.led.Led;

import java.util.function.Supplier;

import static com.kirkwoodwest.extensions.hardware.MF3D.LedMFArcadeData.AnimationState.BRIGHTNESS_LEVEL;
import static com.kirkwoodwest.extensions.hardware.MF3D.LedMFArcadeData.AnimationState.GATE;

public class LedMFArcade extends Led<LedMFArcadeData> {
  private LedMFArcadeData localValue = LedMFArcadeData.blackColor();

  public LedMFArcade(MidiOut midiOut, int note, Supplier<LedMFArcadeData> supplier){
    super(midiOut,0, ShortMidiMessage.NOTE_ON, note , supplier);
  }

  @Override
  public boolean compare(LedMFArcadeData value) {
    return LedMFArcadeData.compare(this.localValue, value);
  }

  @Override
  public void sendData(LedMFArcadeData value) {
    if(value == null) return;
    this.localValue = value;
    LedMFArcadeData.AnimationState state = this.localValue.getAnimationState();
    if(state == GATE) {
      //Send Color
      midiOut.sendMidi(status|HardwareMF3D.MIDI_CHANNEL, data2, value.getMfColor());
      //Send Animation state
      midiOut.sendMidi(status|HardwareMF3D.MIDI_CHANNEL_ANIMATIONS, data2, value.getGate());
    } else if(this.localValue.getAnimationState() == BRIGHTNESS_LEVEL){
      //Send Color
      midiOut.sendMidi(status|HardwareMF3D.MIDI_CHANNEL, data2, value.getMfColor());
      //Send Animation state
      midiOut.sendMidi(status|1, data2, value.getBrightness());

    }
  }
}

