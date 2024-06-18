package com.kirkwoodwest.extensions.oscprojectfader.fader;

import com.kirkwoodwest.extensions.oscprojectfader.randomizer.Randomizer;

import java.util.ArrayList;
import java.util.stream.IntStream;

/**
 * Container for fader values.
 * Negative values indicate that the remote control is not valid and should be ignored.
 */
public class FaderValues {
  private final Randomizer randomizer;
  ArrayList<Double> values = new ArrayList<>();
  private boolean exists = false;

  public FaderValues(Randomizer randomizer) {
    this.randomizer = randomizer;
    IntStream.range(0, randomizer.getItemCount()).forEach(i -> {
      this.values.add(-1.0);
    });

  }

  public void save() {
    IntStream.range(0, randomizer.getItemCount()).forEach(i -> {
      if(randomizer.itemIsValid(i).get()) {
        values.set(i, randomizer.itemValue(i).get());
      } else {
        values.set(i, -1.0);
      }
    });
    exists = true;
  }

  public void saveRandom() {
    IntStream.range(0, randomizer.getItemCount()).forEach(i -> {
      if(randomizer.itemIsValid(i).get()) {
        double randomValue = randomizer.getRandomValue(i);
        if(Double.compare(randomValue, -1.0) != 0) {
          //Use item Value
          values.set(i, randomValue);
        } else {
          //Use current value
          values.set(i, randomizer.itemValue(i).get());
        }

      } else {
        values.set(i, -1.0);
      }
    });
    exists = true;
  }

  public ArrayList<Double> getValues(){
    return values;
  }

  public boolean exists() {
    return exists;
  }

  public void clear() {
    values.forEach(value -> {
      value = -1.0;
    });
    exists = false;
  }
}
