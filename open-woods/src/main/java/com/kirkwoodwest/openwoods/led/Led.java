package com.kirkwoodwest.openwoods.led;

import com.bitwig.extension.controller.api.MidiOut;
import com.kirkwoodwest.openwoods.osc.OscHost;
import java.util.function.Supplier;

public class Led<T> implements ILed<T> {

  public OscHost oscHost;
  public String oscTarget;

  public MidiOut midiOut;
  public int status;
  public int data2;

  public Supplier<T> supplier;
  public T localValue;
  private boolean blockUpdate;
  public boolean nullSupplierDataSent;

  private Modes mode;
  private enum Modes {
    MIDI,
    OSC,
  }

  private int updateEvery = 1;
  public int updateInterval = 0;

  public Led(MidiOut midiOut, int channel, int message, int data2, Supplier<T>  supplier){
    mode = Modes.MIDI;
    this.midiOut = midiOut;
    this.status = message | channel;
    this.data2 = data2;
    this.supplier = supplier;
  }

  public Led(OscHost oscHost, String oscTarget, Supplier<T> supplier){
    mode = Modes.OSC;
    this.oscHost = oscHost;
    this.oscTarget = oscTarget;
    this.supplier = supplier;
  }

  public void update(boolean forceUpdate) {
    if (supplier == null) {
      if (!nullSupplierDataSent) {
        sendData(getEmptyData());
        nullSupplierDataSent = true;
      }
      return; // Early return to avoid deep nesting
    }

    if (blockUpdate) return;

    if (++updateInterval == updateEvery) {
      T value = supplier.get();
      if (forceUpdate || !compare(value)) {
        sendData(value);
      }
      updateInterval = 0;
    }
  }

  public boolean compare(T data) {
    return data.equals(localValue);
  }

  public T getEmptyData(){
    return null;
  }

  public void sendData(T value) {
    if(value==null) return;
    if(mode == Modes.MIDI) {
      midiOut.sendMidi(status, data2, (int) value);
      localValue = value;
    } else {
      if (!oscTarget.isEmpty()) {
        oscHost.addMessageToQueue(oscTarget, value);
      }
      localValue = value;
    }
  }

  public void blockUpdates(boolean blockUpdate){
    this.blockUpdate = blockUpdate;
  }

  public void throttleUpdates(int updateEvery){
    if(updateEvery <1) updateEvery = 1;
    this.updateEvery = updateEvery;
  }

  public void setSupplier(Supplier<T> supplier) {
    this.supplier = supplier;
    nullSupplierDataSent = false;
  }
  
  public void clearResultsSupplier() {
    this.supplier = null;
    T value = getEmptyData();
    if(value!=null){
      sendData(value);
    }
  }

}
