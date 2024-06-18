package com.kirkwoodwest.extensions.oscprojectfader.fader;

import com.bitwig.extension.controller.api.ControllerHost;
import com.kirkwoodwest.extensions.oscprojectfader.randomizer.Randomizer;
import com.kirkwoodwest.extensions.usercontrolsosc.interfaces.projectremotes.ProjectRemotes;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class FaderAB extends FaderBase{
  private final int MORPH_SLOTS = 2;
  private final FaderSlotValues[] morphSlots = new FaderSlotValues[MORPH_SLOTS];
  private final int numRemotes;
  private double morphValue = 0.0;
  private final int[] selectedPresetIndex = new int[MORPH_SLOTS];

  public enum FADER_SIDE {
    A,
    B
  }

  /**
   * Creates a new OscFader
   * @param host  ControllerHost
   * @param randomizer Randomizer
   */
  public FaderAB(ControllerHost host, Randomizer randomizer) {
    super(host, randomizer);
    numRemotes = randomizer.getItemCount();
    morphSlots[0] = new FaderSlotValues(numRemotes);
    morphSlots[1] = new FaderSlotValues(numRemotes);
    selectedPresetIndex[0] = -1;  //No preset selected
    selectedPresetIndex[1] = -1;  //No preset selected
  }

  public void loadPreset(int index, FaderAB.FADER_SIDE FADER_SIDE){
    int morphSlot = FADER_SIDE.ordinal();
    FaderValues faderValues = getPreset(index);
    if(faderValues.exists()) {
      if (index < NUM_PRESETS) {
        ArrayList<Double> values = faderValues.getValues();
        this.morphSlots[morphSlot].set(values);
        updateFaderValues = true;
      }
      host.requestFlush();
      selectedPresetIndex[morphSlot] = index;
    }
  }

  public void setMorphValue(double v){
    morphValue = v;
    updateFaderValues = true;
    host.requestFlush();
  }

  public double getMorphValue(){
    return morphValue;
  }

  public int getSelectedPresetIndex(FADER_SIDE faderSide) {
    int morphSlot = faderSide.ordinal();
    return selectedPresetIndex[morphSlot];
  }

  //This module does nothing unless this is called.
  public void flush(){
    if(updateFaderValues) {
      //Translate A and B values based on a morph value
      ArrayList<Double> valuesA = morphSlots[0].get();
      ArrayList<Double> valuesB = morphSlots[1].get();
      IntStream.range(0, randomizer.getItemCount())
              .filter(i -> valuesA != null && valuesB != null)
              .forEach(i -> {
        Double valueA = valuesA.get(i);
        Double valueB = valuesB.get(i);
        double morphedValue = interpolate(valueA, valueB, morphValue);
        randomizer.setItemValue(i, morphedValue);
      });
      updateFaderValues = false;
    }
  }

  public double interpolate(double valueA, double valueB, double morphValue) {
    return valueA + (valueB - valueA) * morphValue;
  }
}
