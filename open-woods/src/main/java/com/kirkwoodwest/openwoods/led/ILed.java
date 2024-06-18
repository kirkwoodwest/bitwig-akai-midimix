package com.kirkwoodwest.openwoods.led;

import java.util.function.Supplier;

public interface ILed<T> {
  public void update(boolean forceUpdate);
  public void sendData(T value);
  public void setSupplier(Supplier<T> supplier);
  public void clearResultsSupplier();
}
