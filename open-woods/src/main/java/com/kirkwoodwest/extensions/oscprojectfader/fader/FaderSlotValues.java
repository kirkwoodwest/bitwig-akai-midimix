package com.kirkwoodwest.extensions.oscprojectfader.fader;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores values for a fader slot.
 */
public class FaderSlotValues {
  ArrayList<Double> values = new ArrayList<>();

  public FaderSlotValues(int numValues) {
    for (int i = 0; i < numValues; i++) {
      values.add(-1.0);
    }
  }

  public ArrayList<Double> get(){
    return values;
  }

  public void set(ArrayList<Double> values) {
    this.values = values;
  }
}
