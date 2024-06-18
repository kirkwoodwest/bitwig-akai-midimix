package com.kirkwoodwest.openwoods.savelist;

import java.util.ArrayList;

public class BooleanCollection {
  private final ArrayList<Boolean> values;
  private final int count;

  public BooleanCollection(int count) {
    this.count = count;
    values = new ArrayList<>(count);
    for (int i = 0; i < count; i++) {
      values.add(false);
    }
  }

  public int size() {
    return count;
  }

  public void setFromSerialized(ArrayList<Boolean> values) {
    //Every 2 Indexes is a color
    for (int i = 0; i < values.size(); i++) {
      values.set(i, values.get(i));
    }
  }

  public Boolean getValue(int index) {
    return values.get(index);
  }

  public void setValue(int index, Boolean value) {
    values.set(index, value);
  }
}
