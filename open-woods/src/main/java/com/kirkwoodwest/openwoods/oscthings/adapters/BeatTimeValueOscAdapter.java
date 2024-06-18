package com.kirkwoodwest.openwoods.oscthings.adapters;

import com.bitwig.extension.controller.api.BeatTimeValue;
import com.bitwig.extension.controller.api.Transport;
import com.kirkwoodwest.openwoods.osc.OscController;
import com.kirkwoodwest.openwoods.osc.OscHost;
import com.kirkwoodwest.openwoods.osc.adapters.OscAdapter;

public class BeatTimeValueOscAdapter extends OscAdapter<BeatTimeValue> {
  private int beatsPerBar = 4;
  private int subdivisionsPerBeat = 4; // Sixteenth notes
  private int ticksPerSubdivision = 16; // Ticks per sixteenth note
  private double tempo = 0;

  private int bars = 0;
  private int currentBeat = 0;
  private int currentSubdivision = 0;
  private int currentTick = 0;

  public BeatTimeValueOscAdapter(BeatTimeValue beatTimeValue, Transport transport, OscController oscController, String oscTarget, String oscDescription) {
    super(beatTimeValue, oscController, oscTarget, oscDescription);
    beatTimeValue.addValueObserver(this::valueChanged);
    transport.timeSignature().denominator().addValueObserver((v)->beatsPerBar = v);
    transport.timeSignature().numerator().addValueObserver((v)->subdivisionsPerBeat = v);
    transport.timeSignature().ticks().addValueObserver((v)->ticksPerSubdivision = v);
  }

  private void valueChanged(double beatTime) {
    setDirty(true);
  }

  @Override
  public void flush() {
    if (getDirty() || forceFlush) {
      double rawBeatTime = dataSource.getAsDouble();

      // Calculate the total number of beats passed
      int totalBeats = (int) rawBeatTime; // Since 1.0 in raw beat time approximates to one beat

      // Calculate bars and the current beat within the bar
      int bars = totalBeats / beatsPerBar;
      int currentBeat = totalBeats % beatsPerBar;

      // Calculate the current position within the beat for subdivisions and ticks
      double fractionalBeat = (rawBeatTime - totalBeats) * subdivisionsPerBeat;
      int currentSubdivision = (int) fractionalBeat;
      int currentTick = (int) ((fractionalBeat - currentSubdivision) * ticksPerSubdivision);

      if(bars != this.bars || forceFlush){
        oscController.addMessageToQueue(oscPath + "/bars", bars + 1);
        this.bars = bars;
      }

      if(currentBeat != this.currentBeat || forceFlush){
        oscController.addMessageToQueue(oscPath + "/beats", currentBeat + 1);
        this.currentBeat = currentBeat;
      }

      if(currentSubdivision != this.currentSubdivision || forceFlush){
        oscController.addMessageToQueue(oscPath + "/sub_beats", currentSubdivision + 1);
        this.currentSubdivision = currentSubdivision;
      }

      if(currentTick != this.currentTick || forceFlush){
     //   oscHost.sendMessage(oscPath + "/tick", currentTick);
        this.currentTick = currentTick;
      }

      //oscHost.addMessageToQueue(oscPath, bars + 1, currentBeat + 1, currentSubdivision + 1, currentTick);

      setDirty(false);
      forceFlush = false;
    }
  }
}
