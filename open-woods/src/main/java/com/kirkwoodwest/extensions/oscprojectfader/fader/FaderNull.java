package com.kirkwoodwest.extensions.oscprojectfader.fader;

import com.bitwig.extension.controller.api.ControllerHost;
import com.kirkwoodwest.extensions.oscprojectfader.randomizer.Randomizer;

public class FaderNull extends FaderBase {
  private final int NUM_PRESETS = 8;
  private final int MORPH_SLOTS = 2;
  private final int[] selectedPreset = new int[MORPH_SLOTS];

  public enum FADER_SIDE {
    A,
    B
  }

  /**
   * Creates a new OscFader
   * @param host
   * @param randomizer
   */
  public FaderNull(ControllerHost host, Randomizer randomizer) {
    super(host, randomizer);
  }

  public int getNumPresets() {
    return NUM_PRESETS;
  }

  public int getSelectedPreset(FADER_SIDE faderSide) {
    int morphSlot = faderSide.ordinal();
    return selectedPreset[morphSlot];
  }

  //This module does nothing unless this is called.
  public void flush(){
//    if(Double.compare(morphValue, previousMorphValue) != 0 || updateFaderValues) {
//      //Translate A and B values based on a morph value
//      List<Double> valuesA = morphSlots[0].get();
//      List<Double> valuesB = morphSlots[1].get();
//      if(valuesA != null || valuesB != null) {
//        for (int i = 0; i < numRemotes; i++) {
//          //for each remote
//          RemoteControl remoteControl = remoteControls.get(i);
//
//          Double valueA = valuesA.get(i);
//          Double valueB = valuesB.get(i);
//          double morphedValue = valueA + (valueB - valueA) * morphValue;
//          remoteControl.value().set(morphedValue);
//        }
//        previousMorphValue = morphValue;
//        updateFaderValues = false;
//      }
//    }
  }
}
