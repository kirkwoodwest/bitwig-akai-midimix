package com.kirkwoodwest.openwoods.oscthings.vu;

import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.Track;
import com.kirkwoodwest.closedwoods.loader.element.ILoaderElement;
import com.kirkwoodwest.openwoods.osc.OscController;
import com.kirkwoodwest.openwoods.trackbank.*;

import java.util.HashMap;

public class VuCursorTrackBankOsc implements ILoaderElement {
  private final String id;
  private final OscController oscController;
  private boolean isDirty;

  private final String oscPath;
  private HashMap<Integer, Integer> vuMeterPeak = new HashMap<>();
  private HashMap<Integer, Integer> vuMeterRms = new HashMap<>();

  public VuCursorTrackBankOsc(ControllerHost host, OscController oscController, String id, String oscPath, CursorTrackBank cursorTrackBank, int resolution, boolean usePeak, boolean useRms) {
    this.oscController = oscController;
    this.oscPath = oscPath;
    this.id = id;

    int sizeOfBank = cursorTrackBank.getSizeOfBank();
    for (int i = 0; i < sizeOfBank; i++) {
      final int index = i;
      Track track = cursorTrackBank.getItemAt(i);
      vuMeterPeak.put(index, 0);
      vuMeterRms.put(index, 0);

      if (usePeak) {
        track.addVuMeterObserver(resolution, -1, true, (vuLevel) -> {
          vuMeterPeak.put(index, vuLevel);
          setDirty(true);
        });
      }

      if (useRms) {
        track.addVuMeterObserver(resolution, -1, false, (vuLevel) -> {
          vuMeterRms.put(index, vuLevel);
          setDirty(true);
        });
      }
    }

    //Build and Add Adapter
    VuCursorTrackBankAdapter vuCursorTrackBankAdapter = new VuCursorTrackBankAdapter(this, oscController, oscPath, resolution, usePeak,useRms);
    oscController.addAdapter(id, vuCursorTrackBankAdapter);
  }

  public void setDirty(boolean dirty) {
    this.isDirty = dirty;
  }

  public boolean getDirty() {
    return this.isDirty;
  }

  public HashMap<Integer, Integer> getRms() {
    return vuMeterRms;
  }

  public HashMap<Integer, Integer> getPeak() {
    return vuMeterPeak;
  }

  public int getTrackCount() {
    return vuMeterPeak.size();
  }

  @Override
  public void refresh() {
    oscController.forceNextFlush(id);
  }

  @Override
  public void enable(boolean enabled) {
    oscController.setGroupEnabled(id, enabled);
  }
}
