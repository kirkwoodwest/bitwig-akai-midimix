package com.kirkwoodwest.extensions.hardware.LaunchPadMiniMK3;

import com.bitwig.extension.controller.api.HardwareActionBindable;
import com.kirkwoodwest.openwoods.hardware.layer.ControlGroup;
import com.kirkwoodwest.openwoods.led.Led;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.kirkwoodwest.extensions.hardware.LaunchPadMiniMK3.HardwareLaunchPadMiniMK3.GRID_WIDTH;

public class ControlGroupLaunchPadMiniMK3 extends ControlGroup<HardwareLaunchPadMiniMK3> {
  private final HashMap<Integer, HardwareActionBindable> actionsGridPressed = new HashMap<>();
  private final HashMap<Integer, HardwareActionBindable> actionsGridReleased = new HashMap<>();
  private final HashMap<Integer, HardwareActionBindable> actionsFunctionPressed = new HashMap<>();
  private final HashMap<Integer, HardwareActionBindable> actionsFunctionReleased = new HashMap<>();
  private final HashMap<Integer, HardwareActionBindable> actionsScenePressed = new HashMap<>();
  private final HashMap<Integer, HardwareActionBindable> actionsSceneReleased = new HashMap<>();

  private final HashMap<LEDS, Integer> ledCounts = new HashMap<>();
  private final HashMap<Integer, Led<LedLaunchPadMiniMK3>> ledsGrid = new HashMap<>();
  private final HashMap<Integer, Led<LedLaunchPadMiniMK3>> ledsFunction = new HashMap<>();
  private final HashMap<Integer, Led<LedLaunchPadMiniMK3>> ledsScene = new HashMap<>();

  enum LEDS {
    GRID,
    FUNCTION,
    SCENE
  }

  public ControlGroupLaunchPadMiniMK3(HardwareLaunchPadMiniMK3 hardware, int id) {
    super(hardware, id);
    //Build Action List

    //Create leds
    //Set up led groups and data
    List<LedLaunchPadMiniMK3> ledsGrid = hardware.getGridLeds();
    List<LedLaunchPadMiniMK3> ledsFunction = hardware.getFunctionLeds();
    List<LedLaunchPadMiniMK3> ledsScene = hardware.getSceneLeds();
    ledCounts.put(LEDS.GRID, ledsGrid.size());
    ledCounts.put(LEDS.FUNCTION, ledsFunction.size());
    ledCounts.put(LEDS.SCENE, ledsScene.size());
  }


  public void addGridButtonPressedAction(int index, HardwareActionBindable hardwareAction){
    actionsGridPressed.put(index, hardwareAction);
  }

  public void addGridButtonReleasedAction(int index, HardwareActionBindable hardwareAction){
    actionsGridReleased.put(index, hardwareAction);
  }

  public void addGridButtonPressedAction(int row, int col, HardwareActionBindable hardwareAction){
    actionsGridPressed.put(row * GRID_WIDTH + col, hardwareAction);
  }

  public void addGridButtonReleasedAction(int row, int col, HardwareActionBindable hardwareAction){
    actionsGridReleased.put(row * GRID_WIDTH + col, hardwareAction);
  }

  public void addFunctionButtonPressedAction(int index, HardwareActionBindable hardwareAction){
    actionsFunctionPressed.put(index, hardwareAction);
  }

  public void addFunctionButtonReleasedAction(int index, HardwareActionBindable hardwareAction){
    actionsFunctionReleased.put(index, hardwareAction);
  }

  public void addSceneButtonPressedAction(int index, HardwareActionBindable hardwareAction){
    actionsScenePressed.put(index, hardwareAction);
  }

  public void addSceneButtonReleasedAction(int index, HardwareActionBindable hardwareAction){
    actionsSceneReleased.put(index, hardwareAction);
  }

  public void addGridLed(int index, Led<LedLaunchPadMiniMK3> led){
    ledsGrid.put(index, led);
  }

  public void addFunctionLed(int index, Led<LedLaunchPadMiniMK3> led){
    ledsFunction.put(index, led);
  }

  public void addSceneLed(int index, Led<LedLaunchPadMiniMK3> led){
    ledsScene.put(index, led);
  }


  @Override
  public void setActive() {
    actionsGridPressed.forEach((index, action) -> hardware.getGridButton(index).pressedAction().setBinding(action));
    actionsGridReleased.forEach((index, action) -> hardware.getGridButton(index).releasedAction().setBinding(action));
    actionsFunctionPressed.forEach((index, action) -> hardware.getFunctionButton(index).pressedAction().setBinding(action));
    actionsFunctionReleased.forEach((index, action) -> hardware.getFunctionButton(index).releasedAction().setBinding(action));
    actionsScenePressed.forEach((index, action) -> hardware.getSceneButton(index).pressedAction().setBinding(action));
    actionsSceneReleased.forEach((index, action) -> hardware.getSceneButton(index).releasedAction().setBinding(action));
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
  public Led<?> getLed(int ledGroup, int index) {
    return switch (LEDS.values()[ledGroup]) {
      case GRID -> ledsGrid.get(index);
      case FUNCTION -> ledsFunction.get(index);
      case SCENE -> ledsScene.get(index);
    };
  }
}
