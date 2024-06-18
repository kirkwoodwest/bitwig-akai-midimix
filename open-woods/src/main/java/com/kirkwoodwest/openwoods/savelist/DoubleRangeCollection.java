package com.kirkwoodwest.openwoods.savelist;

import java.util.ArrayList;

public class DoubleRangeCollection {
  private final ArrayList<Double> min;
  private final ArrayList<Double> max;
  private final int count;

  public DoubleRangeCollection(int count) {
    this.count = count;
    min = new ArrayList<Double>(count);
    max = new ArrayList<Double>(count);
    for (int i = 0; i < count; i++) {
        min.add(0.0);
        max.add(1.0);
    }
  }

  public int size() {
    return count;
  }

  public void setFromSerialized(ArrayList<Double> values) {
    //Every 2 Indexes is a color
    for (int i = 0; i < values.size(); i += 2) {
      //get doubles and make new color
      min.set(i, values.get(i));
      max.set(i, values.get(i + 1));
    }
  }

  public Double getMin(int index) {
    return min.get(index);
  }

  public Double getMax(int index) {
    return max.get(index);
  }

  public void setMin(int index, Double min) {
    this.min.set(index, min);
  }

  public void setMax(int index, Double max) {
    this.max.set(index, max);
  }
}
