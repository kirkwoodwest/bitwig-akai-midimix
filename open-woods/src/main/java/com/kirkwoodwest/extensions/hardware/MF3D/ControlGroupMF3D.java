package com.kirkwoodwest.extensions.hardware.MF3D;

import com.bitwig.extension.controller.api.HardwareActionBindable;
import com.kirkwoodwest.openwoods.hardware.layer.ControlGroup;
import com.kirkwoodwest.openwoods.led.Led;
import com.kirkwoodwest.openwoods.led.LedNoteOnOff;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ControlGroupMF3D extends ControlGroup<HardwareMF3D> {

  private final HashMap<Integer, HardwareActionBindable> arcadeButtonPressedActions = new HashMap<>();
  private final HashMap<Integer, HardwareActionBindable> arcadeButtonReleasedActions = new HashMap<>();
  private final HashMap<Integer, HardwareActionBindable> topButtonPressedActions = new HashMap<>();
  private final HashMap<Integer, HardwareActionBindable> topButtonReleasedActions = new HashMap<>();
  private final HashMap<LEDS, Integer> ledCounts = new HashMap<>();
  private final HashMap<LEDS, HashMap<Integer, Led<?>>> leds = new HashMap<>();

  enum LEDS {
    TOP,
    ARCADE
  }

  public ControlGroupMF3D(HardwareMF3D hardware, int id) {
    super(hardware, id);

    //Set up led groups and data
    ArrayList<LedNoteOnOff> ledTop = hardware.getTopLeds();
    ArrayList<LedMFArcade> ledArcade = hardware.getArcadeLeds();
    ledCounts.put(ControlGroupMF3D.LEDS.TOP, ledTop.size());
    ledCounts.put(LEDS.ARCADE, ledArcade.size());

    //Create Empty hashmaps for future LEds
    leds.put(ControlGroupMF3D.LEDS.TOP,  new HashMap<>());
    leds.put(LEDS.ARCADE, new HashMap<>());
  }

  public int getArcadeButtonCount() {
    return hardware.getArcadeButtonCount();
  }

  public void addArcadeButtonPressedAction(int index, HardwareActionBindable hardwareAction){
    arcadeButtonPressedActions.put(index, hardwareAction);
  }

  public void addArcadeButtonReleasedAction(int index, HardwareActionBindable hardwareAction){
    arcadeButtonReleasedActions.put(index, hardwareAction);
  }

  public void addTopButtonPressedAction(int index, HardwareActionBindable hardwareAction){
    topButtonPressedActions.put(index, hardwareAction);
  }

  public void addTopButtonReleasedAction(int index, HardwareActionBindable hardwareAction){
    topButtonReleasedActions.put(index, hardwareAction);
  }

  public void addTopButtonLed(int index, Supplier<Boolean> ledSupplier) {
    LedNoteOnOff led = hardware.getTopLed(index);
    led.setSupplier(ledSupplier);
    leds.get(LEDS.TOP).put(index, led);
  }

  //use integers from LedMidiFighterButton class
  public void addArcadeButtonLed(int index, Supplier<LedMFArcadeData> ledSupplier) {
    LedMFArcade led = hardware.getArcadeLed(index);
    led.setSupplier(ledSupplier);
    leds.get(LEDS.ARCADE).put(index, led);
  }

  @Override
  public void setActive() {
    arcadeButtonPressedActions.forEach((index, action) -> hardware.getHardwareButton(HardwareMF3D.Buttons.ARCADE, index).pressedAction().setBinding(action));
    arcadeButtonReleasedActions.forEach((index, action) -> hardware.getHardwareButton(HardwareMF3D.Buttons.ARCADE, index).releasedAction().setBinding(action));
    topButtonPressedActions.forEach((index, action) -> hardware.getHardwareButton(HardwareMF3D.Buttons.TOP, index).pressedAction().setBinding(action));
    topButtonReleasedActions.forEach((index, action) -> hardware.getHardwareButton(HardwareMF3D.Buttons.TOP, index).releasedAction().setBinding(action));
  }

  @Override
  public void setInactive() {

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
