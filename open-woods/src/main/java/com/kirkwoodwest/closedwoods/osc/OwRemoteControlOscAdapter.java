package com.kirkwoodwest.closedwoods.osc;

import com.bitwig.extension.api.opensoundcontrol.OscConnection;
import com.bitwig.extension.api.opensoundcontrol.OscMessage;
import com.bitwig.extension.controller.api.Parameter;
import com.bitwig.extension.controller.api.RemoteControl;
import com.kirkwoodwest.openwoods.led.IsTouchedListener;
import com.kirkwoodwest.openwoods.osc.OscController;
import com.kirkwoodwest.openwoods.osc.adapters.OscAdapter;
import com.kirkwoodwest.openwoods.osc.adapters.RemoteControlPaths;
import com.kirkwoodwest.openwoods.remotecontrol.OwRemoteControl;
import com.kirkwoodwest.utils.LogUtil;

public class OwRemoteControlOscAdapter extends OscAdapter<OwRemoteControl> {
  private IsTouchedListener isTouchedListener = null;
  private final OwRemoteControl owRemoteControl;
  private final RemoteControlPaths oscTargets;

  private ObserverData<Boolean> exists = new ObserverData(false, () -> setDirty(true));
  private ObserverData<String> name = new ObserverData("573051265", () -> setDirty(true));
  private ObserverData<Double> value = new ObserverData(-1.0, () -> setDirty(true));
  private ObserverData<Double> modulatedValue = new ObserverData(-1.0, () -> setDirty(true));
  private ObserverData<String> displayedValue = new ObserverData("573051265", () -> setDirty(true));
  private ObserverData<Boolean> isBeingMapped = new ObserverData(false, () -> setDirty(true));
  private volatile boolean useModulatedValues = false;

  // Another constructor with a different set of parameters
  public OwRemoteControlOscAdapter(OwRemoteControl owRemoteControl, OscController oscController, String oscTarget, String oscDescription) {
    super(owRemoteControl, oscController, oscTarget, oscDescription);
    this.owRemoteControl = owRemoteControl;
    oscTargets = new RemoteControlPaths(oscTarget);
    owRemoteControl.addExistsConsumer(exists::set);
    RemoteControl remoteControl = owRemoteControl.getRemoteControl();

    remoteControl.name().addValueObserver(name::set);
    remoteControl.displayedValue().addValueObserver(displayedValue::set);
    remoteControl.value().addValueObserver(value::set);
    remoteControl.isBeingMapped().addValueObserver(isBeingMapped::set);

    oscController.registerOscCallback(oscTargets.value, ",f", "value change", this::oscUpdateValue);
    oscController.registerOscCallback(oscTargets.touch, "Is Touched", this::oscUpdateTouch);

    oscController.registerOscCallback(oscTargets.isBeingMapped, "Is it being mapped? ohrly?", this::oscUpdateBeingMapped);
    oscController.registerOscCallback(oscTargets.clearMapping, "Clear the mapping", this::oscUpdateClearMapping);
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

    synchronized T get() {
      return value;
    }

    synchronized public void set(T value) {
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

  public void addIsTouchedListener(IsTouchedListener listener) {
    //Only get one of these...
    this.isTouchedListener = listener;
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
    if (dataSource != null) {
      if (dataSource.exists()) {
        Parameter parameter = dataSource.getRemoteControl();
        if (parameter != null) {
          double value = (double) oscMessage.getFloat(0);
          if (value < 0 || value > 1.0) {
            LogUtil.println("WARNING OSC PARAMETER VALUE OUT OF RANGE: [ " + oscPath + " ] : " + value);
          } else {
            parameter.setImmediately(value);
            //Allow value to get sent out again...
            this.value.set(value);
            this.setDirty(true);
          }
        }
      }
    }
  }

  /**
   * Updates touch status
   *
   * @param oscConnection
   * @param oscMessage    (bool)
   */
  private void oscUpdateTouch(OscConnection oscConnection, OscMessage oscMessage) {
    boolean touch = oscMessage.getTypeTag().equals(",T");
    if (dataSource.exists()) {
      final RemoteControl remoteControl = dataSource.getRemoteControl();
      if (remoteControl != null) {
        if (touch) {
          remoteControl.touch(true);
          if (this.isTouchedListener != null) {
            isTouchedListener.isTouched(remoteControl);
          }
        } else {
          remoteControl.touch(false);
        }
      }
    }
  }

  private void oscUpdateBeingMapped(OscConnection oscConnection, OscMessage oscMessage) {
    boolean b = oscMessage.getTypeTag().equals(",T");
    RemoteControl remoteControl = owRemoteControl.getRemoteControl();
    remoteControl.isBeingMapped().set(b);
  }

  private void oscUpdateClearMapping(OscConnection oscConnection, OscMessage oscMessage) {
    final RemoteControl remoteControl = owRemoteControl.getRemoteControl();
    remoteControl.deleteObject();
  }

  @Override
  public void flush() {
    if (getDirty() || forceFlush) {
      final RemoteControl remoteControl = dataSource.getRemoteControl();

      //TODO: Refactor this so its the actual exists...
      if (exists.isDirty() || forceFlush) {
        exists.setDirty(false);
        oscController.addMessageToQueue(oscTargets.exists, exists.get());
        if (exists.get() == false) {
          oscController.addMessageToQueue(oscTargets.name, "");
          oscController.addMessageToQueue(oscTargets.displayedValue, "");
          oscController.addMessageToQueue(oscTargets.isBeingMapped, false);
          //oscHost.addMessageToQueue(oscTargets.value, value.get());
        }
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
            oscController.addMessageToQueue(oscTargets.modulatedValue, modulatedValue.get().floatValue());
          }
        }
        if (isBeingMapped.isDirty() || forceFlush) {
          isBeingMapped.setDirty(false);
          oscController.addMessageToQueue(oscTargets.isBeingMapped, isBeingMapped.get());
        }
      }
      setDirty(false);
      forceFlush = false;
    }

  }
}
