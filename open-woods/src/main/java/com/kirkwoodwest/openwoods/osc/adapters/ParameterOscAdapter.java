package com.kirkwoodwest.openwoods.osc.adapters;

import com.bitwig.extension.api.opensoundcontrol.OscConnection;
import com.bitwig.extension.api.opensoundcontrol.OscMessage;
import com.bitwig.extension.controller.api.Parameter;
import com.kirkwoodwest.openwoods.osc.OscController;
import com.kirkwoodwest.openwoods.osc.OscHost;

public class ParameterOscAdapter extends OscAdapter<Parameter> {
  private final Parameter parameter;
  private final RemoteControlPaths oscTargets;

  private final ObserverData<Boolean> exists = new ObserverData<>(false, () -> setDirty(true));
  private final ObserverData<String> name = new ObserverData<>("573051265", () -> setDirty(true));
  private final ObserverData<Double> value = new ObserverData<>(-1.0, () -> setDirty(true));
  private final ObserverData<Double> modulatedValue = new ObserverData<>(-1.0, () -> setDirty(true));
  private final ObserverData<String> displayedValue = new ObserverData<>("573051265", () -> setDirty(true));
  private final ObserverData<Boolean> isBeingMapped = new ObserverData<>(false, () -> setDirty(true));
  private volatile boolean useModulatedValues = false;

  // Another constructor with a different set of parameters
  public ParameterOscAdapter(Parameter parameter, OscController oscController, String oscTarget, String oscDescription) {
    super(parameter, oscController, oscTarget, oscDescription);
    this.parameter = parameter;
    oscTargets = new RemoteControlPaths(oscTarget);

    parameter.exists().addValueObserver(exists::set);
    parameter.name().addValueObserver(name::set);
    parameter.displayedValue().addValueObserver(displayedValue::set);
    parameter.value().addValueObserver(value::set);
    parameter.modulatedValue().addValueObserver(value::set);

    getAdapterInfo().add(oscTargets.exists, oscDescription + " Exists", "bool");
    getAdapterInfo().add(oscTargets.name, oscDescription + " Name", ",s");
    getAdapterInfo().add(oscTargets.value, oscDescription + " Value", ",f");
    getAdapterInfo().add(oscTargets.displayedValue, oscDescription + " Displayed Value", ",s");
    getAdapterInfo().add(oscTargets.touch, oscDescription + " Is Touched", "bool");
    getAdapterInfo().add(oscTargets.modulatedValue, oscDescription + " Modulated Value (Must Be enabled)", ",f");

    oscController.registerOscCallback(oscTargets.value, ",f", oscDescription + " Value", this::oscUpdateValue);
    oscController.registerOscCallback(oscTargets.touch, "*", oscDescription + " Is Touched", (oscConnection, oscMessage) -> {
      if (oscMessage.getTypeTag().equals(",T")) {
        oscUpdateTouch(true);
      } else {
        oscUpdateTouch(false);
      }
    });
  }

  class ObserverData<T> {
    private volatile T value;
    private final Runnable setParentDirty;
    private volatile boolean dirty;

    public ObserverData(T value, Runnable setParentDirty) {
      this.value = value;
      this.setParentDirty = setParentDirty;
      this.dirty = false;
    }

    public T get() {
      return value;
    }

    public void set(T value) {
      this.value = value;
      dirty = true;
      setParentDirty.run();
    }

    public boolean isDirty() {
      return dirty;
    }

    public void setDirty(boolean dirty) {
      this.dirty = dirty;
    }
  }

  public void setUseModulatedValues(boolean b) {
    useModulatedValues = b;
  }

  /**
   * Updates value cvia OSC
   *
   * @param oscConnection
   * @param oscMessage    (int)
   */
  private void oscUpdateValue(OscConnection oscConnection, OscMessage oscMessage) {
    if (parameter != null) {
      if (parameter.exists().get()) {
        if (parameter != null) {
          float value = oscMessage.getFloat(0);
          if (value >= 0 && value <= 1.0) {
            parameter.setImmediately(value);
            //Allow value to get sent out again...
            this.value.set((double) value);
            dirty = true;
          }
        }
      }
    }
  }

  /**
   * Updates touch via OSC
   */
  private void oscUpdateTouch(boolean touch) {
    if (parameter.exists().get()) {
      parameter.touch(touch);
    }
  }

  @Override
  public void flush() {
    if (dirty || forceFlush) {
      //TODO: Refactor this so its the actual exists...
      if (exists.isDirty() || forceFlush) {
        exists.setDirty(false);
        oscController.addMessageToQueue(oscTargets.exists, exists.get());
      }
      if (exists.value) {
        if (name.isDirty() || forceFlush) {
          name.setDirty(false);
          oscController.addMessageToQueue(oscTargets.name, name.get());
        }
        if (value.isDirty() || forceFlush) {
          value.setDirty(false);
          oscController.addMessageToQueue(oscTargets.value, value.get().floatValue());
        }
        if (displayedValue.isDirty() || forceFlush) {
          displayedValue.setDirty(false);
          oscController.addMessageToQueue(oscTargets.displayedValue, displayedValue.get());
        }
        if (useModulatedValues) {
          if (modulatedValue.isDirty() || forceFlush) {
            modulatedValue.setDirty(false);
            oscController.addMessageToQueue(oscTargets.modulatedValue, modulatedValue.get());
          }
        }
      }
      dirty = false;
      forceFlush = false;
    }
  }
}
