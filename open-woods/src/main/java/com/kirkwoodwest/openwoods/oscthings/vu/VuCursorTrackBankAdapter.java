package com.kirkwoodwest.openwoods.oscthings.vu;

import com.kirkwoodwest.openwoods.osc.OscController;
import com.kirkwoodwest.openwoods.osc.OscHost;
import com.kirkwoodwest.openwoods.osc.adapters.OscAdapter;

import java.util.HashMap;

public class VuCursorTrackBankAdapter extends OscAdapter<VuCursorTrackBankOsc> {
  private final boolean useRms;
  private boolean usePeak ;
  private int interval;
  private long lastUpdate;

  public VuCursorTrackBankAdapter(VuCursorTrackBankOsc dataSource, OscController oscController, String oscTarget, int resolution, boolean usePeak, boolean useRms) {
    super(dataSource, oscController, oscTarget, "");
    this.useRms = useRms;
    this.usePeak = usePeak;
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

      if (dataSource.getDirty() && intervalPassed || forceFlush) {
        if (dataSource.getDirty()) {
          //TODO: Complete RMS implementation

          //do rms
          int trackCount = dataSource.getTrackCount();
          int dataLength = trackCount;

          if (usePeak && useRms) {
            dataLength = trackCount * 2;
          } else if (!usePeak && !useRms) {
            return;
          }

          Integer[] data = new Integer[dataLength];
          int dataIndex = 0;
          HashMap<Integer, Integer> peak = dataSource.getPeak();
          HashMap<Integer, Integer> rms = dataSource.getRms();
          for (int i = 0; i < trackCount; i++) {
            if (usePeak) {
              int peakValue = peak.get(i);
              data[dataIndex] = peakValue;
              dataIndex++;
            }
            if (useRms) {
              int rmsValue = rms.get(i);
              data[dataIndex] = rmsValue;
              dataIndex++;
            }
          }
          oscController.addMessageToQueue(oscPath, (Object[]) data);
        }
        dataSource.setDirty(false);
        forceFlush = false;
      }
    }
  }
}
