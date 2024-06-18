package com.kirkwoodwest.openwoods.settings;

import com.bitwig.extension.callback.IntegerValueChangedCallback;
import com.bitwig.extension.controller.api.*;


import java.util.ArrayList;

/**
 * Wraps a Bitwig number setting so that it can be observed as an integer with min and max.
 * There is probably a better way to do this...
 */
public class NumberSetting implements SettableIntegerValue {
  private final SettableRangedValue setting;
  private final int min;
  private final int max;
  private final ArrayList<IntegerValueChangedCallback> observers = new ArrayList<>();

  public NumberSetting(DocumentState documentState, String name, String category, int min, int max, int increment, String units, int defaultValue){
    this.min = min;
    this.max = max;
    setting = documentState.getNumberSetting(name, category, min, max, increment, units, defaultValue);
    setting.markInterested();
  }

  public NumberSetting(Preferences preferences, String name, String category, int min, int max, int increment, String units, int defaultValue){
    this.min = min;
    this.max = max;
    setting = preferences.getNumberSetting(name, category, min, max, increment, units, defaultValue);
  }

  @Override
  public int get(){
    return min + (int) Math.round((setting.getAsDouble() * (max - min)));
  }

  @Override
  public void addValueObserver(IntegerValueChangedCallback callback, int valueWhenUnassigned) {
    addValueObserver(callback);
  }

  @Override
  public void set(int value){
    setting.set((float) (value - min) / (max - min));
  }

  @Override
  public void inc(int amount) {

  }

  @Override
  public RelativeHardwareControlBinding addBindingWithSensitivity(RelativeHardwareControl hardwareControl, double sensitivity) {
    return null;
  }

  @Override
  public void markInterested() {
    setting.markInterested();
  }

  @Override
  public void addValueObserver(IntegerValueChangedCallback callback) {
    if(observers.size() == 0) {
      setting.addValueObserver((value) -> {
        int num = min + (int) Math.floor((value * (max - min)));
        observers.forEach((o) -> o.valueChanged(num));
      });
    }
    this.observers.add(callback);
  }

  @Override
  public boolean isSubscribed() {
    return false;
  }

  @Override
  public void setIsSubscribed(boolean value) {

  }

  @Override
  public void subscribe() {

  }

  @Override
  public void unsubscribe() {

  }

  public Setting getSetting() {
    return (Setting) setting;
  }
}
