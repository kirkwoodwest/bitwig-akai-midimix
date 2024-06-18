package com.kirkwoodwest.closedwoods.trackmaster.snapshots;

import com.bitwig.extension.controller.api.ControllerHost;
import com.kirkwoodwest.openwoods.remotecontrol.OwRemoteControl;
import com.kirkwoodwest.openwoods.savelist.SaveListData;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Snapshot {
  private final List<SnapShotData> allData = new ArrayList<>();
  private final List<SnapShotData> cursorTrackData = new ArrayList<>();
  private final List<SnapShotData> remoteControlData = new ArrayList<>();
  private final int slots;
  private final SaveListData<Double> saveListData;
  List<SnapShotCursorTrack> snapShotCursorTracks = new ArrayList<>();

  public Snapshot(ControllerHost host,  int slots) {
    Supplier<ArrayList<Double>> saveDataSupplier = this::getAllData;
    Consumer<ArrayList<Double>> restoreConsumer = this::setAllData;
    saveListData = new SaveListData<>(host, "snapshot", saveDataSupplier , restoreConsumer, Double.class);
    this.slots = slots;
  }

  public void setupRemoteControls(ArrayList<OwRemoteControl> remoteControls, String name) {
    SnapShotRemotes snapShotRemotes = new SnapShotRemotes(remoteControls);
    SnapShotData snapShotData = new SnapShotData(snapShotRemotes, slots, snapShotRemotes.getNumParameters());
    remoteControlData.add(snapShotData);
    allData.add(snapShotData);
  }

  public void createCursorTrack(ControllerHost host, String name, int numSends, int numDevices, int numRemoteControlPages, String remoteExpressionTag) {
    SnapShotCursorTrack snapShotCursorTrack = new SnapShotCursorTrack(host, name, numSends, numDevices, numRemoteControlPages, remoteExpressionTag);
    snapShotCursorTracks.add(snapShotCursorTrack);
    SnapShotData snapShotData = new SnapShotData(snapShotCursorTrack, slots, snapShotCursorTrack.getNumParameters());
    cursorTrackData.add(snapShotData);
    allData.add(snapShotData);
  }

  private ArrayList<Double> getAllData() {
    ArrayList<Double> data = new ArrayList<>();
    for (SnapShotData snapShotData : allData) {
      data.addAll(snapShotData.getAllData());
    }
    return data;
  }

  private void setAllData(ArrayList<Double> data) {
    int index = 0;
    for (SnapShotData snapShotData : allData) {
      snapShotData.setAllData(data.subList(index, index + snapShotData.getNumParameters()));
      index += snapShotData.getNumParameters();
    }
  }

  public int getNumCursorTracks() {
    return snapShotCursorTracks.size();
  }

  public int getNumRemoteControls(){
    return remoteControlData.size();
  }

  //--------------------------------------------------------------------------------------
  //Cursor Tracks
  public boolean hasData(int index, int slotIndex) {
    if(slotIndex < slots && slotIndex >= 0) {
      return cursorTrackData.get(index).hasData(slotIndex);
    }
    return false;
  }

  public void storeCursorTrackSlotData(int index, int slotIndex) {
    if(slotIndex < slots && slotIndex >= 0) {
      cursorTrackData.get(index).storeDataIntoSlot(slotIndex);
    }
  }

  public void recallCursorTrackSlotData(int index, int slotIndex) {
    if(slotIndex < slots && slotIndex >= 0) {
      cursorTrackData.get(index).recallDataFromSlot(slotIndex);
    }
  }

  public void clearCursorTrackSlotData(int index, int slotIndex) {
    if(slotIndex < slots && slotIndex >= 0) {
      cursorTrackData.get(index).clearDataFromSlot(slotIndex);
    }
  }

  public void pinCursorTrack(int index) {
    snapShotCursorTracks.get(index).pinCursorTrack();
  }

  public void pinCursorDevice(int cursorIndex, int deviceIndex) {
    snapShotCursorTracks.get(cursorIndex).pinCursorDevice(deviceIndex);
  }

  //--------------------------------------------------------------------------------------
  //Remote Controls
  public void storeRemoteControlSlotData(int index, int slotIndex) {
    if(slotIndex < slots && slotIndex >= 0) {
      remoteControlData.get(index).storeDataIntoSlot(slotIndex);
    }
  }

  public void recallRemoteControlSlotData(int index, int slotIndex) {
    if(slotIndex < slots && slotIndex >= 0) {
      remoteControlData.get(index).recallDataFromSlot(slotIndex);
    }
  }

  public void clearRemoteControlSlotData(int index, int slotIndex) {
    if(slotIndex < slots && slotIndex >= 0) {
      remoteControlData.get(index).clearDataFromSlot(slotIndex);
    }
  }

  public void flush() {
  }
}
