package com.kirkwoodwest.openwoods.oscthings.vu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class VuCursorTrackData {
  private final List<Consumer<Boolean>> consumers;
  private int vuPeakLeft;
  private int vuPeakRight;
  private int vuRMSLeft;
  private int vuRMSRight;
  private VuCursorTrackAdapter adapter;

  public VuCursorTrackData() {
    this.consumers = new ArrayList<>();
  }

  public void setPeakLeft(int i) {
    vuPeakLeft = i;
    setDirty();
  }

  public void setPeakRight(int i) {
    vuPeakRight = i;
    setDirty();
  }

  public void setRMSLeft(int i) {
    vuRMSLeft = i;
    setDirty();
  }

  public void setRMSRight(int i) {
    vuRMSRight = i;
    setDirty();
  }

  public List<Integer> getValues() {
    return Arrays.asList(vuPeakLeft, vuPeakRight, vuRMSLeft, vuRMSRight);
  }

  public void setDirty() {
    consumers.forEach(c -> c.accept(true));
  }

  public void addObserver(Consumer<Boolean> setDirty) {
    consumers.add(setDirty);
  }
}