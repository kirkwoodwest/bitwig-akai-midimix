package com.kirkwoodwest.hardware;

import com.bitwig.extension.api.util.midi.ShortMidiMessage;
import com.bitwig.extension.callback.ShortMidiDataReceivedCallback;
import com.bitwig.extension.controller.api.*;
import com.kirkwoodwest.openwoods.HardwareBasic;
import com.kirkwoodwest.openwoods.led.LedCCRanged;
import com.kirkwoodwest.openwoods.led.LedNoteOnOff;


import java.util.ArrayList;
import java.util.function.Supplier;

public class HardwareMidiMix extends HardwareBasic {

  public HardwareSurface hardware_surface;

  public ArrayList<AbsoluteHardwareKnob>  hardware_knobs_row_1;
  public ArrayList<AbsoluteHardwareKnob>  hardware_knobs_row_2;
  public ArrayList<AbsoluteHardwareKnob>  hardware_knobs_row_3;
  public ArrayList<AbsoluteHardwareKnob>  hardware_faders;
  private final ArrayList<HardwareButton> hardware_buttons_row_1;
  private final ArrayList<HardwareButton> hardware_buttons_row_2;
  private final ArrayList<HardwareButton> hardware_buttons_row_3;
  private final ArrayList<HardwareButton> hardware_button_banks;

  public HardwareMidiMix(ControllerHost host, int midi_port, ShortMidiDataReceivedCallback input_callback) {
    super(host.getMidiInPort(midi_port), host.getMidiOutPort(midi_port));
    hardware_surface = host.createHardwareSurface();

    host.getMidiOutPort(0).sendMidi(ShortMidiMessage.CONTROL_CHANGE|1, 0, 0);

    //Build Buttons
    hardware_buttons_row_1 = buildHardwareButtons("MIDIMIX_BUTTONS_1", MidiMap.BTN_ROW_1,  MidiMap.MIDI_CHANNEL);
    hardware_buttons_row_2 = buildHardwareButtons("MIDIMIX_BUTTONS_2", MidiMap.BTN_ROW_2,  MidiMap.MIDI_CHANNEL);
    hardware_buttons_row_3 = buildHardwareButtons("MIDIMIX_BUTTONS_3", MidiMap.BTN_ROW_3,  MidiMap.MIDI_CHANNEL);
    hardware_button_banks = buildHardwareButtons("MIDIMIX_BANKS", MidiMap.CC_BANKS,  0);

    hardware_knobs_row_1 = buildAbsoluteControls("MIDIMIX_KNOBS_1", MidiMap.CC_KNOBS_1, MidiMap.MIDI_CHANNEL);
    hardware_knobs_row_2 = buildAbsoluteControls("MIDIMIX_KNOBS_2", MidiMap.CC_KNOBS_2, MidiMap.MIDI_CHANNEL);
    hardware_knobs_row_3= buildAbsoluteControls("MIDIMIX_KNOBS_3", MidiMap.CC_KNOBS_3, MidiMap.MIDI_CHANNEL);
    hardware_faders = buildAbsoluteControls("MIDIMIX_FADERS", MidiMap.CC_FADERS, MidiMap.MIDI_CHANNEL);

    //TODO: Setup Leds?
    //
  }

  private ArrayList<AbsoluteHardwareKnob> buildAbsoluteControls(String base_name, int[] cc_list, int midi_channel) {
    ArrayList<AbsoluteHardwareKnob> absolute_controls = new ArrayList<>();
    int num_knobs = cc_list.length;
    for (int i=0; i < num_knobs; i++ ) {
      AbsoluteHardwareKnob         hardware_knob = hardware_surface.createAbsoluteHardwareKnob(base_name + i);
      AbsoluteHardwareValueMatcher value_matcher = inputPort.createAbsoluteCCValueMatcher(midi_channel, cc_list[i]);

    //  hardware_knob.disableTakeOver();

      hardware_knob.setAdjustValueMatcher(value_matcher);
      absolute_controls.add(hardware_knob);
    }
    return absolute_controls;
  }

  private ArrayList<HardwareButton> buildHardwareButtons(String base_name, int[] button_notes, int midi_channel) {
    //Build Buttons
    ArrayList absolute_buttons = new ArrayList<HardwareButton>();
    for(int index=0; index<button_notes.length; index++) {
      HardwareButton hardware_button = hardware_surface.createHardwareButton(base_name + index);
      HardwareActionMatcher   pressedButtonAction = inputPort.createNoteOnActionMatcher(midi_channel, button_notes[index]);
      hardware_button.pressedAction().setActionMatcher(pressedButtonAction);
      HardwareActionMatcher   releasedButtonAction = inputPort.createNoteOffActionMatcher(midi_channel, button_notes[index]);
      hardware_button.releasedAction().setActionMatcher(releasedButtonAction);
      absolute_buttons.add(hardware_button);
    }
    return absolute_buttons;
  }

  private void addBinding(AbsoluteHardwareKnob knob, HardwareBindable bindable){
    if (bindable == null) {
      knob.clearBindings();
    } else {
      knob.setBinding(bindable);
    }
  }

  public HardwareButton getHardwareButtonRow1(int index) {
    return hardware_buttons_row_1.get(index);
  }
  public HardwareButton getHardwareButtonRow2(int index) {
    return hardware_buttons_row_2.get(index);
  }
  public HardwareButton getHardwareButtonRow3(int index) { return hardware_buttons_row_3.get(index); }

  public HardwareButton getHardwareButtonBank(int index) { return hardware_button_banks.get(index); }


  private void bindHardwareButton(HardwareButton button, HardwareActionBindable pressed_action, HardwareActionBindable released_action){
    if(pressed_action != null) {
      button.pressedAction().setBinding( pressed_action);
    } else {
      button.pressedAction().clearBindings();
    }
    if(released_action != null) {
      button.releasedAction().setBinding( released_action);
    } else {
      button.releasedAction().clearBindings();
    }
  }

  public void bindHardwareButtonRow1(int index, HardwareActionBindable pressed_action, HardwareActionBindable released_action) {
    HardwareButton button          = getHardwareButtonRow1(index);
    bindHardwareButton(button, pressed_action, released_action);
  }

  public void bindHardwareButtonRow2(int index, HardwareActionBindable pressed_action, HardwareActionBindable released_action) {
    HardwareButton button          = getHardwareButtonRow2(index);
    bindHardwareButton(button, pressed_action, released_action);
  }

  public void bindHardwareButtonRow3(int index, HardwareActionBindable pressed_action, HardwareActionBindable released_action) {
    HardwareButton button          = getHardwareButtonRow3(index);
    bindHardwareButton(button, pressed_action, released_action);
  }

  private void releaseBindingsInternal(ArrayList<AbsoluteHardwareKnob> knobs) {
    int size = knobs.size();
    for (int i = 0; i < size; i++) {
      knobs.get(i).clearBindings();
    }
  }

  private void releaseButtonBindingsInternal(ArrayList<HardwareButton> buttons) {
    int size = buttons.size();
    for (int i = 0; i < size; i++) {
      buttons.get(i).pressedAction().clearBindings();
      buttons.get(i).releasedAction().clearBindings();
    }
  }

  public void releaseBindings() {
    releaseBindingsInternal(hardware_faders);
    releaseBindingsInternal(hardware_knobs_row_1);
    releaseBindingsInternal(hardware_knobs_row_2);
    releaseBindingsInternal(hardware_knobs_row_3);
    releaseButtonBindingsInternal(hardware_buttons_row_1);
    releaseButtonBindingsInternal(hardware_buttons_row_2);
  }

  private LedCCRanged getEncoderLed(int[] cc_list, int index, Supplier<Integer> supplier) {
    int status = ShortMidiMessage.CONTROL_CHANGE;
    int cc = cc_list[index];
    LedCCRanged led = new LedCCRanged(outputPort, status, MidiMap.MIDI_CHANNEL, cc, supplier);
    return led;
  }


  /**
   * Returns an the button led matching the row / index.
   * @param cc_list list of cc values
   * @param index index of the button
   * @param supplier method to determine the value for the knob led.
   * @return
   */
  private LedNoteOnOff getButtonLed(int[] cc_list, int index, Supplier<Boolean> supplier){
    int note = cc_list[index];
    LedNoteOnOff led = new LedNoteOnOff(outputPort, 0, note, supplier, 127,0);
    return led;
  }

  public LedNoteOnOff getButtonLedRow1( int index, Supplier<Boolean> supplier) {
    return getButtonLed(MidiMap.LED_BTNS_1, index, supplier);
  }
  public LedNoteOnOff getButtonLedRow2( int index, Supplier<Boolean> supplier) {
    return getButtonLed(MidiMap.LED_BTNS_2, index, supplier);
  }
  public LedNoteOnOff getButtonLedRow3( int index, Supplier<Boolean> supplier) {
    return getButtonLed(MidiMap.LED_BTNS_3, index, supplier);
  }
}