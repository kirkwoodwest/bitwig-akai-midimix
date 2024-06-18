package com.kirkwoodwest.openwoods.savelist;

import com.bitwig.extension.controller.api.ControllerHost;

import java.io.Flushable;
import java.util.ArrayList;

public class DoubleRangeCollectionSaved extends DoubleRangeCollection implements Flushable {

  private final SaveListData<Double> saveData;

  public DoubleRangeCollectionSaved(ControllerHost host, String name, int count) {
    super(count);

    saveData = new SaveListData<>(host, name, this::getValuesSerialized, this::setValuesSerialized, Double.class);
    saveData.hideSetting();
  }

  private void setValuesSerialized(ArrayList<Double> values) {
    setFromSerialized(values);
    saveData.saveNextFlush();
  }

  public ArrayList<Double> getValuesSerialized() {
    //build new colors from the list of doubles
    ArrayList<Double> values = new ArrayList<>();

    for (int i = 0; i < size(); i++) {
      values.add(getMin(i));
      values.add(getMax(i));
    }
    return values;
  }

  @Override
  public void setMin(int index, Double min) {
    super.setMin(index, min);
    saveData.saveNextFlush();
  }

  @Override
  public void setMax(int index, Double max) {
    super.setMax(index, max);
    saveData.saveNextFlush();
  }

  @Override
  public void flush() {
    saveData.flush();
  }

}
