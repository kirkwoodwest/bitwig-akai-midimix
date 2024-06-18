package com.kirkwoodwest.closedwoods.trackmaster.snapshots;

import java.util.List;

//Getter and setters for snapshots
public interface SnapShotBase {
  public List<Double> getParameterData();
  public void setParameterData(List<Double> data);
  public int getNumParameters();
}
