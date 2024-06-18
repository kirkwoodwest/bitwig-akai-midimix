package com.kirkwoodwest.extensions.oscprojectfader.fader;

import com.bitwig.extension.controller.api.ControllerHost;
import com.kirkwoodwest.extensions.oscprojectfader.randomizer.Randomizer;

import java.util.ArrayList;
import java.util.List;

class FaderBase {
  final int NUM_PRESETS = 8;
  private final List<FaderValues> presetSlots = new ArrayList<>();

  final ControllerHost host;
  final Randomizer randomizer;
  public boolean updateFaderValues;
  /**
   * Creates a new OscFader
   * @param host
   * @param randomizer
   */
  FaderBase(ControllerHost host, Randomizer randomizer){
    //TODO: Add functionality to change from Two Morph Slots, Four Morph Slots, & allow remote management of the slots.
    this.host = host;
    this.randomizer = randomizer;
    for (int i = 0; i < NUM_PRESETS; i++) {
      presetSlots.add(new FaderValues(randomizer));
    }
  }

  public int getNumPresets() {
    return NUM_PRESETS;
  }

  FaderValues getPreset(int index){
    return presetSlots.get(index);
  }

  public void savePreset(int index){

    presetSlots.get(index).save();
    updateFaderValues = true;
    host.requestFlush();
  }

  public void deletePreset(int i) {
    presetSlots.get(i).clear();
    host.requestFlush();
  }

  public boolean getPresetExists(int index) {
    return presetSlots.get(index).exists();
  }

  public void randomizePreset(int index) {
    presetSlots.get(index).saveRandom();
    updateFaderValues = true;
    host.requestFlush();
  }
}
