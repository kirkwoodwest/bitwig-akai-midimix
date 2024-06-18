package com.kirkwoodwest.openwoods.led;

import com.bitwig.extension.controller.api.SettableBeatTimeValue;
import com.kirkwoodwest.openwoods.osc.OscHost;

import java.util.function.Supplier;

public class LedOscBeatTimeClock extends Led<SettableBeatTimeValue> {
  String oscTargetBeats;
  String oscTargetSubBeats;
  String oscTargetBars;
  String pos = "";
  private double time = 0;
  private int subBeats;
  private int bars;
  private int beats;

  public LedOscBeatTimeClock(OscHost oscHost, String oscTarget, Supplier<SettableBeatTimeValue> beatTimeValue) {
    super(oscHost, oscTarget, beatTimeValue);
    oscTargetBars = oscTarget + "/bars";
    oscTargetBeats = oscTarget + "/beats";
    oscTargetSubBeats = oscTarget + "/sub_beats";
  }

  @Override
  public void update(boolean forceUpdate) {
    if (supplier != null) {
      double timeInSeconds = supplier.get().get();
      if (Double.compare(timeInSeconds, time) == 0) {
        return;
      }
      pos = supplier.get().getFormatted();
      time = timeInSeconds;

      int firstColon = pos.indexOf(':');
      int secondColon = pos.indexOf(':', firstColon + 1);
      int thirdColon = pos.indexOf(':', secondColon + 1);
      int subBeats = Integer.parseInt(pos.substring(secondColon + 1, thirdColon));
      if(subBeats == this.subBeats) return;

      int bars = Integer.parseInt(pos.substring(0, firstColon));
      int beats = Integer.parseInt(pos.substring(firstColon + 1, secondColon));
     // int ticks = Integer.parseInt(pos.substring(thirdColon + 1));
      if(bars != this.bars) {
        oscHost.addMessageToQueue(oscTargetBars, bars);
        this.bars = bars;
      }
      if(beats != this.beats) {
        oscHost.addMessageToQueue(oscTargetBeats, beats);
        this.beats = beats;
      }
      if(subBeats != this.subBeats) {
        oscHost.addMessageToQueue(oscTargetSubBeats, subBeats);
        this.subBeats = subBeats;
      }
    }
  }


  public static MusicStructure calculateMusicStructure(double timeInSeconds, int bpm, int beatsPerBar, int subBeatsPerBeat) {
    double totalBeats = timeInSeconds / 60 * bpm;
    int bars = (int) (totalBeats / beatsPerBar);
    double remainingBeats = totalBeats % beatsPerBar;
    double subBeats = remainingBeats * subBeatsPerBeat;

    return new MusicStructure(bars, remainingBeats, subBeats);
  }

  static class MusicStructure {
    int bars;
    double beats;
    double subBeats;

    MusicStructure(int bars, double beats, double subBeats) {
      this.bars = bars;
      this.beats = beats;
      this.subBeats = subBeats;
    }
  }


  public static Supplier<SettableBeatTimeValue> createSupplier(SettableBeatTimeValue value) {
    value.markInterested();
    return ()->{  return value;};
  }
}
