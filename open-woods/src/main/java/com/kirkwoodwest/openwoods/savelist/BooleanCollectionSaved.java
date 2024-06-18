package com.kirkwoodwest.openwoods.savelist;

import com.bitwig.extension.controller.api.ControllerHost;

import java.io.Flushable;
import java.util.ArrayList;
import java.util.function.BiConsumer;

public class BooleanCollectionSaved extends BooleanCollection implements Flushable {

  private final SaveListData<Boolean> saveData;

  //Create Consumer List for when values are changed
  private final ArrayList<BiConsumer<Integer, Boolean>> biConsumers = new ArrayList<>();


  public BooleanCollectionSaved(ControllerHost host, String name, int count) {
    super(count);

    saveData = new SaveListData<>(host, name, this::getValuesSerialized, this::setValuesSerialized, Boolean.class);
    saveData.hideSetting();
  }

  private void setValuesSerialized(ArrayList<Boolean> values) {
    setFromSerialized(values);
    saveData.saveNextFlush();
  }

  public ArrayList<Boolean> getValuesSerialized() {
    //build new colors from the list of doubles
    ArrayList<Boolean> values = new ArrayList<>();

    for (int i = 0; i < size(); i++) {
      values.add(getValue(i));
    }
    return values;
  }

  @Override
  public void setValue(int index, Boolean value) {
    super.setValue(index, value);
    biConsumers.forEach(biConsumer -> biConsumer.accept(index, value));
    saveData.saveNextFlush();
  }

  public void addValueObserver(BiConsumer<Integer, Boolean> biConsumer) {
    biConsumers.add(biConsumer);
  }


  @Override
  public void flush() {
    saveData.flush();
  }

}
