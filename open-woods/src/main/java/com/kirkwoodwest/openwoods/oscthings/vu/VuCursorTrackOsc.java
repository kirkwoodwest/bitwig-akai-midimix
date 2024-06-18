package com.kirkwoodwest.openwoods.oscthings.vu;

import com.bitwig.extension.controller.api.CursorTrack;
import com.kirkwoodwest.closedwoods.loader.element.ILoaderElement;
import com.kirkwoodwest.openwoods.osc.OscController;

public class VuCursorTrackOsc implements ILoaderElement {
  public static String ID = "VU";
  private final VuCursorTrackData vuCursorTrackData;
  private final OscController oscController;

  public VuCursorTrackOsc(OscController oscController, CursorTrack cursorTrack, String oscPath, int resolution){
    this.oscController = oscController;
    this.vuCursorTrackData = new VuCursorTrackData();
    createCursorVu(cursorTrack, resolution);
    VuCursorTrackAdapter vuCursorTrackAdapter = new VuCursorTrackAdapter(vuCursorTrackData, oscController, oscPath, "");
    oscController.addAdapter(ID, vuCursorTrackAdapter);
  }

  /**
   * Creates a VU meter for the cursor track. Data is stored and sent in update().
   * @param cursorTrack
   */
  private void createCursorVu(CursorTrack cursorTrack, int resolution) {
    cursorTrack.addVuMeterObserver(resolution, 0, true, vuCursorTrackData::setPeakLeft);
    cursorTrack.addVuMeterObserver(resolution, 1, true, vuCursorTrackData::setPeakRight);
    cursorTrack.addVuMeterObserver(resolution, 0, false, vuCursorTrackData::setRMSLeft);
    cursorTrack.addVuMeterObserver(resolution, 1, false, vuCursorTrackData::setRMSRight);
  }

  @Override
  public void refresh() {
  }

  @Override
  public void enable(boolean b) {
    oscController.setGroupEnabled("transport", b);
  }
}
