package com.kirkwoodwest.openwoods.oscthings.vu;

import com.kirkwoodwest.openwoods.osc.OscController;
import com.kirkwoodwest.openwoods.osc.OscHost;
import com.kirkwoodwest.openwoods.osc.adapters.OscAdapter;

import java.util.List;

public class VuCursorTrackAdapter extends OscAdapter<VuCursorTrackData> {

  private int interval = 0; //Interval time in ms for updates. 0 = no interval
  private long lastUpdate = 0;  //Timstamp for uptates
  private boolean enabled = true; //Enabled flag for adapter

  public VuCursorTrackAdapter(VuCursorTrackData vuCursorTrackData, OscController oscController, String oscTarget, String oscDescription) {
    super(vuCursorTrackData, oscController, oscTarget, oscDescription);
    vuCursorTrackData.addObserver(this::setDirty);
  }

  public void setIntervalUpdates(int interval) {
   this.interval = interval;
  }

  @Override
  public void flush() {
    if (enabled) {
      long now = System.currentTimeMillis();
      boolean intervalPassed = true;
      if (interval > 0) {
        intervalPassed = now - lastUpdate > interval;
        if(intervalPassed) {
          lastUpdate = now;
        }
      }

      if (getDirty() && intervalPassed || forceFlush) {
        //Send Data to targets
        List<Integer> values = dataSource.getValues();
        oscController.addMessageToQueue(oscPath, values);
        setDirty(false);
        forceFlush = false;
      }
    }
  }
}