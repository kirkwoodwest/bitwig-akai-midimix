package com.kirkwoodwest.extensions.oscprojectfader.fader;

import com.bitwig.extension.controller.api.ControllerHost;
import com.kirkwoodwest.extensions.oscprojectfader.randomizer.Randomizer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class FaderXY extends FaderBase{
  private final int MORPH_SLOTS = 4;
  private final FaderSlotValues[] morphSlots = new FaderSlotValues[MORPH_SLOTS];
  private double[] morphValues = new double[MORPH_SLOTS];
  private double[] previousMorphValues = new double[MORPH_SLOTS];
  private double previousMorphValue = -1;
  private boolean updateFaderValues;

  private final int[] selectedPreset = new int[MORPH_SLOTS];

  public enum FADER_SIDE {
    X1,
    X2,
    Y1,
    Y2
  }

  /**
   * Creates a new OscFader
   * @param host
   * @param remoteControls
   */
  public FaderXY(ControllerHost host, Randomizer randomizer) {
    super(host, randomizer);
    morphSlots[0] = new FaderSlotValues(randomizer.getItemCount());
    morphSlots[1] = new FaderSlotValues(randomizer.getItemCount());
  }

  public void loadPreset(int index, FaderXY.FADER_SIDE FADER_SIDE){
    int morphSlot = FADER_SIDE.ordinal();
    FaderValues faderValues = getPreset(index);
    if(faderValues.exists()) {
      if (index < NUM_PRESETS) {
        ArrayList<Double> values = faderValues.getValues();
        this.morphSlots[morphSlot].set(values);
        updateFaderValues = true;
      }
      selectedPreset[morphSlot] = index;
      host.requestFlush();
    }
  }

  public void setMorphValues(double x, double y){
    morphValues[0] = x;
    morphValues[1] = y;
    updateFaderValues = true;
    host.requestFlush();
  }

  public double[] getMorphValues(){
    return morphValues;
  }

  public int getSelectedPreset(FADER_SIDE faderSide) {
    int morphSlot = faderSide.ordinal();
    return selectedPreset[morphSlot];
  }

  //This module does nothing unless this is called.
  public void flush(){
    double morphValueX = morphValues[0];
    double morphValueY = morphValues[1];
    if(updateFaderValues) {
      //Translate A and B values based on a morph value
      List<Double> valuesX1 = morphSlots[0].get();
      List<Double> valuesX2 = morphSlots[1].get();
      List<Double> valuesY1 = morphSlots[2].get();
      List<Double> valuesY2 = morphSlots[3].get();
      IntStream.range(0, randomizer.getItemCount())
              .filter(i -> valuesX1 != null && valuesX2 != null && valuesY1 != null && valuesY2 != null)
              .forEach(i -> {
                Double valueX1 = valuesX1.get(i);
                Double valueX2 = valuesX2.get(i);
                Double valueY1 = valuesY1.get(i);
                Double valueY2 = valuesY2.get(i);
                double v = interpolate(morphValueX, morphValueY, valueX1, valueX2, valueY1, valueY2);
                randomizer.setItemValue(i, v);
              });
      updateFaderValues = false;
    }
  }

  public double interpolate(double morphValueX, double morphValueY, double valueX1, double valueX2, double valueY1, double valueY2) {
    // Interpolate along X axis
    double interpolatedX1 = valueX1 + (valueX2 - valueX1) * morphValueX;
    double interpolatedX2 = valueY1 + (valueY2 - valueY1) * morphValueX;

    // Final interpolation along Y axis
    double finalInterpolatedValue = interpolatedX1 + (interpolatedX2 - interpolatedX1) * morphValueY;
    return finalInterpolatedValue;
  }
}
