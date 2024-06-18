// Written by Kirkwood West - kirkwoodwest.com
// (c) 2020

package com.kirkwoodwest.openwoods;

import com.bitwig.extension.callback.ShortMidiDataReceivedCallback;
import com.bitwig.extension.callback.SysexMidiDataReceivedCallback;
import com.bitwig.extension.controller.api.*;

public class HardwareBasic {

  public MidiIn  inputPort  = null;
  public MidiOut outputPort = null;

  public HardwareBasic(MidiIn inputPort, MidiOut outputPort) {
    this.inputPort 	= inputPort;
    this.outputPort = outputPort;
  }

  public void setInputCallback(ShortMidiDataReceivedCallback midiCallback){
    inputPort.setMidiCallback(midiCallback);
  }

  public void setSysexCallback(SysexMidiDataReceivedCallback sysexCallback){
    inputPort.setSysexCallback(sysexCallback);
  }

  public void sendMidi(int status, int data1, int data2) {
    this.outputPort.sendMidi(status, data1, data2);
  }

  public void sendSysex(String s) {
    this.outputPort.sendSysex(s);
  }
}
