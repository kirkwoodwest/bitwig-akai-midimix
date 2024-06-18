package com.kirkwoodwest.openwoods.hardware.valuetarget;

import com.kirkwoodwest.utils.MathUtil;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

/**
 * Pretty sure this is an absolute value target...
 */
public class RelativeValueTarget implements DoubleConsumer {

  private final boolean updateOnAllChanges;
  public ArrayList<Consumer<Double>> observers = new ArrayList<>();
  public double internal_value = -1;
  public RelativeValueTarget(boolean updateOnAllChanges) {
    this.updateOnAllChanges = updateOnAllChanges;
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

  private void setValueInternal(double value){
    value = MathUtil.valueLimit(value,0,1);
    if(Double.compare(value, internal_value) == 0 &&  !this.updateOnAllChanges) return;
    internal_value = value;
    updateObservers();
  }

  @Override
  public void accept(double value) {
    //Apply value
    value = internal_value + value;
    setValueInternal(value);
  }

  //Sets the value to the given value
  public void setValue(double value) {
    setValueInternal(value);
  }
}
