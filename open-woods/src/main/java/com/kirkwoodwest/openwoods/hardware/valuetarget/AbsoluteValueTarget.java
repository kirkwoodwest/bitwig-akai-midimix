package com.kirkwoodwest.openwoods.hardware.valuetarget;

import com.kirkwoodwest.utils.MathUtil;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

public class AbsoluteValueTarget implements DoubleConsumer {

  public ArrayList<Consumer<Double>> observers = new ArrayList<>();
  public double internal_value = -1;
  public AbsoluteValueTarget() {
  }

  /**
   * Adds a listener for when the knob value has changed.
   * @param observer lambda that listens for the events.
   * @return  True if a listener was actually added.
   */
  public void addValueObserver(Consumer<Double> observer) {
    boolean added_listener = false;
    if (!observers.contains(observer)) {
      observers.add(observer);
    }
  }

  private void updateObservers() {
      observers.forEach(observer -> observer.accept(internal_value));
  }

  public double get() {
    return internal_value;
  }

  @Override
  public void accept(double value) {
    value = MathUtil.valueLimit(value,0,1);
    if (value == internal_value) return;
    internal_value = value;
    updateObservers();
  }
}
