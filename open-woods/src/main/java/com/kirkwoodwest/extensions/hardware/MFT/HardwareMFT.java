package com.kirkwoodwest.extensions.hardware.MFT;

import com.bitwig.extension.api.Color;
import com.bitwig.extension.api.util.midi.ShortMidiMessage;
import com.bitwig.extension.callback.ShortMidiDataReceivedCallback;
import com.bitwig.extension.controller.api.*;
import com.kirkwoodwest.openwoods.flush.Flushables;
import com.kirkwoodwest.openwoods.hardware.HardwareController;
import com.kirkwoodwest.openwoods.hardware.valuetarget.AbsoluteValueTarget;
import com.kirkwoodwest.openwoods.led.LedCCRanged;
import static com.kirkwoodwest.utils.ArrayUtil.generateIncrementalArray;

import java.util.ArrayList;
import java.util.List;


//Implementation of Midi Fighter Twister. Knob1 = top left, Knob16 = bottom right
public class HardwareMFT extends HardwareController<HardwareMFT, ControlGroupMFT> {


  HardwareSurface hardware_surface;
  public ArrayList<RelativeHardwareKnob> encoders;
  private final ArrayList<HardwareButton> encoderButtons = new ArrayList<HardwareButton>();
  private final ArrayList<HardwareActionBindable> actionsSpeedFast = new ArrayList<>();
  private final ArrayList<HardwareActionBindable> actionsSpeedSlow = new ArrayList<>();

  public static final int      MIDI_CHANNEL = 0;
  public static final int      TWISTER_COLOR_MIDI_CHANNEL   = 0;
  public static final int      TWISTER_CC_KNOB_COUNT         = 16;
  private static final int      TWISTER_CC_MIN               = 0;
  public static final int[]    TWISTER_CC_LIST = generateIncrementalArray(TWISTER_CC_MIN, TWISTER_CC_KNOB_COUNT);

  private boolean force_next_update;

  private final int speed_released = (int) Math.floor(127 * 0.75);
  private final int speed_pressed = (int) Math.floor(speed_released * 0.5);
  private final List<HardwareLight> encoderRings = new ArrayList<>();
  private final List<HardwareLight> encoderColors = new ArrayList<>();

  public HardwareMFT(ControllerHost host, int midi_port, ShortMidiDataReceivedCallback input_callback) {
    super(host, midi_port, ControlGroupMFT::new);
    setHardware(this);
    hardware_surface = host.createHardwareSurface();

    //Encoders & Leds
    int num_knobs = TWISTER_CC_LIST.length;
    for (int i=0; i < num_knobs; i++ ) {
      final int button_index = i;

      //Encoder Knobs
      RelativeHardwareKnob hardwareKnob = hardware_surface.createRelativeHardwareKnob("TWISTER_ENCODER_" + midi_port + "_" + i);
      RelativeHardwareValueMatcher valueMatcher = inputPort.createRelativeBinOffsetCCValueMatcher(MIDI_CHANNEL, TWISTER_CC_LIST[i], speed_released);
      hardwareKnob.setAdjustValueMatcher(valueMatcher);
      encoders.add(hardwareKnob);

      MultiStateHardwareLight encoderRing = hardware_surface.createMultiStateHardwareLight("TWISTER LIGHT " + midi_port + "_" + i);
      encoderRings.add(encoderRing);
      MultiStateHardwareLight encoderColor = hardware_surface.createMultiStateHardwareLight("TWISTER COLOR" + midi_port + "-" + i);
      encoderColors.add(encoderColor);

      
      //Encoder Buttons
      int[] buttons = TWISTER_CC_LIST;
      HardwareButton hardwareButton = hardware_surface.createHardwareButton("TWISTER_ENCODER_BUTTON_" + midi_port +" _" + i);
      HardwareActionMatcher   pressedButtonAction = inputPort.createNoteOnActionMatcher(MIDI_CHANNEL, buttons[i]);
      hardwareButton.pressedAction().setActionMatcher(pressedButtonAction);
      HardwareActionMatcher   releasedButtonAction = inputPort.createNoteOffActionMatcher(MIDI_CHANNEL, buttons[i]);
      hardwareButton.releasedAction().setActionMatcher(releasedButtonAction);
      encoderButtons.add(hardwareButton);

      //TODO: this somehow needs to be pushed down to the interfaces that use it instead of blindly stacking encoder speeds onto things.
      HardwareActionBindable pressed_action = host.createAction(()->this.setEncoderSpeed(button_index, speed_pressed), () -> "Encoder Pressed");
      HardwareActionBindable released_action = host.createAction(()->this.setEncoderSpeed(button_index, speed_released), () -> "Encoder Released");

      //create array of pressed actions
      actionsSpeedFast.add(pressed_action);
      actionsSpeedSlow.add(released_action);
    }
  }

  public int getEncoderCount() {
    return TWISTER_CC_KNOB_COUNT;
  }

  public RelativeHardwareKnob getEncoder(int index) {
    return encoders.get(index);
  }

  public HardwareButton getEncoderButton(int index) {
    return encoderButtons.get(index);
  }


  public HardwareLight getEncoderRing(int index) {
    return encoderRings.get(index);
  }

  public HardwareLight getEncoderColor(int index) {
    return encoderColors.get(index);
  }


  public void setEncoderSpeed(int index, int speed){
    RelativeHardwareKnob hardware_knob = getEncoder(index);
    int cc = TWISTER_CC_LIST[index];
    RelativeHardwareValueMatcher value_matcher = inputPort.createRelativeBinOffsetCCValueMatcher(MIDI_CHANNEL, cc, speed);
    hardware_knob.setAdjustValueMatcher(value_matcher);
  }

  public void flush() {
    super.flush();
  }

  public void shutdown(){
  }

  public HardwareActionBindable getActionEncoderFastSpeed(int i) {
    return actionsSpeedFast.get(i);
  }

  public HardwareActionBindable getActionEncoderSlowSpeed(int i) {
    return actionsSpeedSlow.get(i);
  }

  /**
   * Clears all bindings from the hardware
   */
  public void clearBindings() {
    encoderButtons.forEach((button)->{
      button.pressedAction().clearBindings();
      button.releasedAction().clearBindings();
    });
    encoders.forEach(HardwareBindingSource::clearBindings);
  }
}

