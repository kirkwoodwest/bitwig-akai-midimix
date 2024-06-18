package com.kirkwoodwest.extensions.hardware.LaunchPadMiniMK3;

import com.bitwig.extension.controller.api.*;
import com.kirkwoodwest.openwoods.hardware.HardwareController;
import com.kirkwoodwest.openwoods.hardware.layer.ControlGroupFactory;
import com.kirkwoodwest.openwoods.led.Led;
import com.kirkwoodwest.utils.MidiUtil;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static com.kirkwoodwest.extensions.hardware.LaunchPadMiniMK3.MidiData.*;

public class HardwareLaunchPadMiniMK3 extends HardwareController<HardwareLaunchPadMiniMK3, ControlGroupLaunchPadMiniMK3> {
  private final ControllerHost host;
  HardwareSurface hardwareSurface;

  //Leds
  private final List<HardwareButton> buttonsGrid = new ArrayList<>();
  private final List<HardwareButton> buttonsFunction = new ArrayList<>();
  private final List<HardwareButton> buttonsScenes = new ArrayList<>();



  private HashMap<String, Led<LedLaunchPadMiniMK3.LedData>> leds;
  private HashMap<String, HardwareButton> hardwareButtons = new HashMap<>();
  private HashMap<String, HardwareBindable> pressedActions = new HashMap<>();
  private HashMap<String, HardwareBindable> releasedActions = new HashMap<>();
  private HashMap<String, Supplier> supplier= new HashMap<>();

  public enum LIGHT_MODE_STATUS {
    STATIC(LIGHT_MODE_STATUS_STATIC),
    FLASH(LIGHT_MODE_STATUS_FLASH),
    PULSE(LIGHT_MODE_STATUS_PULSE);
    public final int status;

    private LIGHT_MODE_STATUS(Integer value) {
      this.status = value;
    }
  }

  static final int GRID_WIDTH = 8;
  static final int GRID_HEIGHT = 8;

  public enum FUNCTION_BUTTONS {
    UP,
    DOWN,
    LEFT,
    RIGHT,
    SESSION,
    DRUMS,
    KEYS,
    USER
  }

  public HardwareLaunchPadMiniMK3(ControllerHost host, int midi_port, ControlGroupFactory<HardwareLaunchPadMiniMK3, ControlGroupLaunchPadMiniMK3> controlGroupFactory) {
    super(host, midi_port, controlGroupFactory);

    this.host = host;
    hardwareSurface = host.createHardwareSurface();

    String baseHardwareName = "LaunchPadMini_";

    //Create Grid Buttons and insert into array

    //build grid buttons from GRID_CC_MAP
    IntStream.range(0, GRID_CC_MAP.size()).forEach((i) -> {
      int cc = GRID_CC_MAP.get(i);
      String name = "GRID " + i;
      HardwareButton button = createButton(name, BUTTON_MIDI_CHANNEL, cc, MidiUtil.NOTE_ON);
      buttonsGrid.add(button);
    });

    //Build Function Buttons
    IntStream.range(0, FUNCTION_BUTTONS.values().length).forEach((i) -> {
      int cc = FUNCTION_CC_MAP.get(i);
      String name = baseHardwareName + "function_" + i;
      HardwareButton button = createButton(name, BUTTON_MIDI_CHANNEL, cc, MidiUtil.CC);
      buttonsFunction.add(button);
    });

    //Build Scene Buttons
    IntStream.range(0, SCENE_CC_MAP.size()).forEach((i) -> {
      int cc = SCENE_CC_MAP.get(i);
      String name = baseHardwareName + "scenes_" + i;
      HardwareButton button = createButton(name, BUTTON_MIDI_CHANNEL, cc, MidiUtil.CC);
      buttonsScenes.add(button);
    });


    // Initialize Controller
    sendSysex(SYSEX_MODE_DAW);
    sendSysex(SYSEX_MODE_DAW_CLEAR);
    sendSysex(SYSEX_MODE_SESSION);
    sendSysex(SYSEX_PROGRAMMER_MODE);

  }

  private HardwareButton createButton(String name, int midi_channel, int cc, int messageType) {
    HardwareButton hardwareButton = hardwareSurface.createHardwareButton(name);
    HardwareActionMatcher pressedButtonAction;
    HardwareActionMatcher releasedButtonAction;
    if(messageType == MidiUtil.NOTE_ON) {
      pressedButtonAction = inputPort.createNoteOnActionMatcher(midi_channel, cc);
      releasedButtonAction = inputPort.createNoteOffActionMatcher(midi_channel, cc);
    } else {
      pressedButtonAction = inputPort.createCCActionMatcher(midi_channel, cc, 127);
      releasedButtonAction = inputPort.createCCActionMatcher(midi_channel, cc, 0);

    }
    hardwareButton.pressedAction().setActionMatcher(pressedButtonAction);
    hardwareButton.releasedAction().setActionMatcher(releasedButtonAction);
    return hardwareButton;
  }

  public List<HardwareButton> getGridButtons() {
    return buttonsGrid;
  }

  public List<HardwareButton> getFunctionButtons() {
    return buttonsFunction;
  }

  public List<HardwareButton> getSceneButtons() {
    return buttonsScenes;
  }

  public HardwareButton getGridButton(int col, int row){
    return buttonsGrid.get(col + (row * GRID_WIDTH));
  }

  public HardwareButton getGridButton(int index) {
    return buttonsGrid.get(index);
  }

  public HardwareButton getFunctionButton(int index) {
    return buttonsFunction.get(index);
  }

  public HardwareButton getSceneButton(int index) {
    return buttonsScenes.get(index);
  }

  public List<LedLaunchPadMiniMK3> getGridLeds(){
    return IntStream.range(0, GRID_CC_MAP.size())
            .mapToObj(this::getGridLed)
            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
  }

  public List<LedLaunchPadMiniMK3> getFunctionLeds(){
    return IntStream.range(0, FUNCTION_CC_MAP.size())
            .mapToObj(this::getFunctionLed)
            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
  }

  public List<LedLaunchPadMiniMK3> getSceneLeds(){
    return IntStream.range(0, SCENE_CC_MAP.size())
            .mapToObj(this::getSceneLed)
            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
  }

  public LedLaunchPadMiniMK3 getGridLed(int col, int row){
    return new LedLaunchPadMiniMK3(outputPort, GRID_CC_MAP.get(col + (row * GRID_WIDTH)), null);
  }

  public LedLaunchPadMiniMK3 getGridLed(int index) {
    return new LedLaunchPadMiniMK3(outputPort, GRID_CC_MAP.get(index), null);
  }

  public LedLaunchPadMiniMK3 getFunctionLed(int index) {
    return new LedLaunchPadMiniMK3(outputPort, FUNCTION_CC_MAP.get(index), null);
  }

  public LedLaunchPadMiniMK3 getSceneLed(int index) {
    return new LedLaunchPadMiniMK3(outputPort, SCENE_CC_MAP.get(index), null);
  }


  public void clearBindings() {
    buttonsGrid.forEach(button -> {
      button.pressedAction().clearBindings();
      button.releasedAction().clearBindings();
    });

    buttonsFunction.forEach(button -> {
      button.pressedAction().clearBindings();
      button.releasedAction().clearBindings();
    });

    buttonsScenes.forEach(button -> {
      button.pressedAction().clearBindings();
      button.releasedAction().clearBindings();
    });
  }
}