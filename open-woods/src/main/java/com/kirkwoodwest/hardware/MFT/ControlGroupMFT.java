package com.kirkwoodwest.hardware.MFT;

import com.bitwig.extension.controller.api.HardwareActionBindable;
import com.bitwig.extension.controller.api.HardwareBindable;
import com.kirkwoodwest.openwoods.hardware.HardwareActionCollection;
import com.kirkwoodwest.openwoods.hardware.HardwareBindableCollection;
import com.kirkwoodwest.openwoods.hardware.layer.ControlGroup;
import com.kirkwoodwest.openwoods.led.Led;
import com.kirkwoodwest.openwoods.led.LedCCRanged;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ControlGroupMFT extends ControlGroup<HardwareMFT> {
  private final HardwareBindableCollection encoderBindings = new HardwareBindableCollection();
  private final HardwareActionCollection encoderPressedActions = new HardwareActionCollection();
  private final HardwareActionCollection encoderReleasedActions = new HardwareActionCollection();

  //Make hashmap for encoder ring and color leds
  private final HashMap<LEDS, Integer> ledCounts = new HashMap<>();
  private final HashMap<LEDS, HashMap<Integer, Led<?>>> leds = new HashMap<>();
  private final HardwareMFT hardware;

  private boolean variableSpeedEncoder;

  public enum LEDS {
    RINGS,
    COLORS
  }

  public ControlGroupMFT(HardwareMFT hardware, int id) {
    super(hardware, id);
    this.hardware = hardware;

    //Set up led groups and data
    int encoderCount = hardware.getEncoderCount();
    ledCounts.put(LEDS.RINGS, encoderCount);
    ledCounts.put(LEDS.COLORS, encoderCount);

    //Create Empty hashmaps for future LEds
    leds.put(LEDS.RINGS, new HashMap<>());
    leds.put(LEDS.COLORS, new HashMap<>());
  }

  public void addEncoderBinding(int index, HardwareBindable encoderBinding){
    encoderBindings.add(index, encoderBinding);
  }
  public void addEncoderPressedAction(int index, HardwareActionBindable action){
    encoderPressedActions.add(index, action);
  }

  public void addEncoderReleasedAction(int index, HardwareActionBindable action){
    encoderReleasedActions.add(index, action);
  }

  public void addEncoderRingLed(int index, Supplier<Integer> ledSupplier) {
    LedCCRanged led = hardware.getLedRing(index);
    led.setSupplier(ledSupplier);
    leds.get(LEDS.RINGS).put(index,  led);
  }

  public void addEncoderColorLed(int index, Supplier<Integer> ledSupplier) {
    LedCCRanged led = hardware.getLedColor(index);
    led.setSupplier(ledSupplier);
    leds.get(LEDS.COLORS).put(index, led);
  }

  public void setUseVariableSpeedEncoder(boolean b){
    this.variableSpeedEncoder = b;
  }

  public void setInactive() {

  }

  public void setActive() {

    //Clear all bindings
    hardware.clearBindings();

    //Add variable speed encoder actions
    if (variableSpeedEncoder) {
      for (int index = 0; index < getEncoderCount(); index++) {
        hardware.getEncoderButton(index).pressedAction().addBinding(hardware.getActionEncoderFastSpeed(index));
        hardware.getEncoderButton(index).releasedAction().addBinding(hardware.getActionEncoderSlowSpeed(index));
      }
    }

    //Rebind
    encoderBindings.getAll().forEach((index, bindings) -> {
      bindings.forEach(binding -> hardware.getEncoder(index).addBinding(binding));
    });

    encoderPressedActions.getAll().forEach((index, actions) -> {
      actions.forEach(action -> hardware.getEncoderButton(index).pressedAction().addBinding(action));
    });

    encoderReleasedActions.getAll().forEach((index, actions) -> {
      actions.forEach(action -> hardware.getEncoderButton(index).pressedAction().addBinding(action));
    });
  }

  public int getEncoderCount() {
    return hardware.getEncoderCount();
  }

  @Override
  public void clearBindings() {
    hardware.clearBindings();
  }

  @Override
  public List<Integer> getLedGroupIds() {
    return Arrays.stream(LEDS.values())
            .map(Enum::ordinal)
            .collect(Collectors.toList());
  }
  @Override
  public int getLedCount(int ledGroup) {
    return ledCounts.get(LEDS.values()[ledGroup]);
  }

  @Override
  public Led<?> getLed(int ledGroupId, int index) {
    return leds.get(LEDS.values()[ledGroupId]).get(index);
  }

}
