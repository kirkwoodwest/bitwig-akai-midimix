package com.kirkwoodwest.closedwoods.trackmaster.snapshots;

import java.util.ArrayList;
import java.util.List;

public class SnapShotData {
  private final SnapShotBase snapShotBase;
  private final List<List<Double>> data;
  private final int numParameters;
  private final List<Boolean> hasData = new ArrayList<>();

  public SnapShotData(SnapShotBase snapShotBase, int slots, int numParameters) {
    this.snapShotBase = snapShotBase;
    data = new ArrayList<>(slots);
    this.numParameters = numParameters;
    for (int i = 0; i < slots; i++) {
      data.add(new ArrayList<>());
      for (int j = 0; j < numParameters; j++) {
        //Fill with empty data
        data.get(i).add(-1.0);
      }
      hasData.add(false);
    }
  }

  public int getNumParameters() {
    return numParameters;
  }

  public void storeDataIntoSlot(int slot) {
    List<Double> parameterData = snapShotBase.getParameterData();
    if (parameterData == null) {
      return;
    }
    if (slot >= 0 && slot < data.size()) {
      data.set(slot, parameterData);
    }
    hasData.add(true);
  }

  public void clearDataFromSlot(int slot) {
    if (slot >= 0 && slot < data.size()) {
      //Fill with empty data
      data.get(slot).replaceAll(ignored -> -1.0);
    }
    hasData.add(false);
  }

  public void recallDataFromSlot(int slot){
    if (slot >= 0 && slot < data.size()) {
      snapShotBase.setParameterData(data.get(slot));
    }
  }

  //For recovering all data from a snapshot
  public List<Double> getAllData() {
    List<Double> allData = new ArrayList<>();
    for (List<Double> datum : data) {
      allData.addAll(datum);
    }
    return allData;
  }

  //Storing all data into a snapshot
  public void setAllData(List<Double> newData) {
    int index = 0;
    for (List<Double> datum : data) {
      for (int i = 0; i < datum.size() && index < newData.size(); i++) {
        datum.set(i, newData.get(index++));
      }
    }
  }

  public boolean hasData(int slotIndex) {
    return hasData.get(slotIndex);
  }


}
