package com.kirkwoodwest.openwoods.savelist;

import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.StringValue;

import java.io.Flushable;
import java.util.ArrayList;

public class StringValueCollectionSaved extends StringValueCollection implements Flushable {
  private final SaveListData<String> saveData;

  public StringValueCollectionSaved(ControllerHost host, String name, StringValue stringValue, int size) {
    super(stringValue, size);
    saveData = new SaveListData<>(host, name, this::getSerialized, this::setSerialized, String.class);
    //saveData.hideSetting();
  }

  private void setSerialized(ArrayList<String> strings) {
    setStrings(strings);
    saveData.saveNextFlush();
  }

  public ArrayList<String> getSerialized() {
    return getStrings();
  }

  @Override
  public void setString(int index, String s) {
    super.setString(index, s);
    saveData.saveNextFlush();
  }

  @Override
  public void flush() {
    saveData.flush();
  }
}
