package com.kirkwoodwest.extensions.hardware.MF3D;

import com.bitwig.extension.callback.ShortMidiDataReceivedCallback;
import com.bitwig.extension.controller.api.*;
import com.kirkwoodwest.openwoods.hardware.HardwareController;
import com.kirkwoodwest.openwoods.led.LedNoteOnOff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Supplier;

public class HardwareMF3D extends HardwareController<HardwareMF3D, ControlGroupMF3D> {
  private final HardwareSurface hardwareSurface;

  public static final int MIDI_CHANNEL = 0;

  public static final int MIDI_CHANNEL_ANIMATIONS = 1;
  public static final int MIDI_CHANNEL_TILT = 1;
  public static final int MIDI_CHANNEL_TOP_ROW = 1;

  public static final int[] NOTES_ARCADE = {
          48, 49, 50, 51,
          44, 45, 46, 47,
          40, 41, 42, 43,
          36, 37, 38, 39,
  };
  public static final int[] NOTES_TOP = {0, 1, 2, 3};
  public static final int[] CC_TILT = new int[]{0,1,2,3};



  public enum Tilt { LEFT, FORWARDS, RIGHT, BACK }
  public enum Buttons {ARCADE, TOP }

  private final HashMap<Buttons, ArrayList<HardwareButton>> hardwareButtons;
  private final ArrayList<AbsoluteHardwareKnob> hardwareTilt;

  public HardwareMF3D(ControllerHost host, int midiPort, ShortMidiDataReceivedCallback inputCallback) {
    super(host, midiPort, ControlGroupMF3D::new);
    setHardware(this);
    hardwareSurface = host.createHardwareSurface();

    ArrayList<HardwareButton> hardwareButtonsPads = buildHardwareButtonArrayList("Arcade Button ", NOTES_ARCADE, MIDI_CHANNEL);
    ArrayList<HardwareButton> hardwareButtonsTop = buildHardwareButtonArrayList("Top Button ", NOTES_TOP, MIDI_CHANNEL_TOP_ROW);
    hardwareButtons = new HashMap<>();
    hardwareButtons.put(Buttons.ARCADE, hardwareButtonsPads);
    hardwareButtons.put(Buttons.TOP, hardwareButtonsTop);

    hardwareTilt = new ArrayList<>();
    for (int cc : CC_TILT) {
      AbsoluteHardwareKnob knob = hardwareSurface.createAbsoluteHardwareKnob("Tilt " + cc);
      AbsoluteHardwareValueMatcher valueMatcher = inputPort.createAbsoluteCCValueMatcher(MIDI_CHANNEL_TILT, cc);
      knob.setAdjustValueMatcher(valueMatcher);
      hardwareTilt.add(knob);
    }
  }


  private ArrayList<HardwareButton> buildHardwareButtonArrayList(String baseName, int[] buttonNotes, int midiChannel) {
    ArrayList<HardwareButton> buttons = new ArrayList<>();
    for (int index = 0; index < buttonNotes.length; index++) {
      HardwareButton button = hardwareSurface.createHardwareButton(baseName + index);

      HardwareActionMatcher actionMatcherPressed = inputPort.createNoteOnActionMatcher(midiChannel, buttonNotes[index]);
      button.pressedAction().setActionMatcher(actionMatcherPressed);

      HardwareActionMatcher actionMatcherReleased = inputPort.createNoteOffActionMatcher(midiChannel, buttonNotes[index]);
      button.releasedAction().setActionMatcher(actionMatcherReleased);

      buttons.add(button);
    }
    return buttons;
  }

  @Deprecated
  public void bindButton(Buttons buttons, int index, HardwareActionBindable pressedAction, HardwareActionBindable releasedAction) {
    HardwareButton button = getHardwareButton(buttons, index);

    // Binding the pressed action
    if (pressedAction != null) {
      button.pressedAction().setBinding(pressedAction);
    } else {
      button.pressedAction().clearBindings();
    }

    // Binding the released action
    if (releasedAction != null) {
      button.releasedAction().setBinding(releasedAction);
    } else {
      button.releasedAction().clearBindings();
    }
  }


  public HardwareButton getHardwareButton(Buttons buttons, int index) {
    return hardwareButtons.get(buttons).get(index);
  }

  public AbsoluteHardwareKnob getHardwareTilt(Tilt direction) {
    int index = direction.ordinal();
    return hardwareTilt.get(index);
  }

  @Deprecated
  public void setLedTopSupplier(int btnId, Supplier<Boolean> supplier) {
  }

  @Deprecated
  public void setLedArcadeSupplier(int btnId, Supplier<Integer> colorSupplier) {
  }

  public int getArcadeButtonCount() {
    return NOTES_ARCADE.length;
  }

  public ArrayList<LedNoteOnOff> getTopLeds() {
    ArrayList<LedNoteOnOff> leds = new ArrayList<>();
    for (int i = 0; i < NOTES_TOP.length; i++) {
      leds.add(getTopLed(i));
    }
    return leds;
  }

  public ArrayList<LedMFArcade> getArcadeLeds() {
    ArrayList<LedMFArcade> leds = new ArrayList<>();
    for (int i = 0; i < NOTES_ARCADE.length; i++) {
      leds.add(getArcadeLed(i));
    }
    return leds;
  }

  public LedNoteOnOff getTopLed( int index) {
    return new LedNoteOnOff(outputPort, MIDI_CHANNEL_TOP_ROW, NOTES_TOP[index], null, 127, 0);
  }

  public LedMFArcade getArcadeLed(int index) {
   return new LedMFArcade(outputPort, NOTES_ARCADE[index], null);
  }


  public void clearBindings() {
    hardwareButtons.forEach((buttonGroup, buttons) -> {
      buttons.forEach(button -> {
        button.pressedAction().clearBindings();
        button.releasedAction().clearBindings();
      });
    });
  }

}
