package com.kirkwoodwest.closedwoods.trackmaster.snapshots;

import com.bitwig.extension.controller.api.*;

import com.kirkwoodwest.openwoods.remotecontrol.OwRemoteControl;
import com.kirkwoodwest.openwoods.remotecontrol.OwRemoteControlsPage;

import java.util.ArrayList;
import java.util.List;

/**
 * Setsup a cursor track with volume, pan, sends and devices
 */
public class SnapShotCursorTrack implements SnapShotBase {
  private static final int PARAMETER_COUNT = 8;
  private final CursorTrack cursorTrack;
  private final List<Parameter> cursorTrackParameters = new ArrayList<>();
  private final List<Send> sends = new ArrayList<>();
  private final List<PinnableCursorDevice> devices = new ArrayList<>();
  private final List<List<OwRemoteControl>> deviceRemoteControls = new ArrayList<>();

  private final SettableStringValue cursorTrackTarget;

  public SnapShotCursorTrack(ControllerHost host, String name, int numSends, int numDevices, int numRemoteControlPages, String remoteExpressionTag) {
    cursorTrack = host.createCursorTrack(name, name, numSends, numDevices, true);

    DocumentState documentState = host.getDocumentState();

    setupCursorTrack(cursorTrack);
    setupSends(cursorTrack);
    setupDevices(cursorTrack, name, numSends, numDevices, numRemoteControlPages, remoteExpressionTag);

    cursorTrackTarget = documentState.getStringSetting(name + ":cursor", "Cursor Tracks", 36, "");
    cursorTrackTarget.markInterested();
    cursorTrack.name().markInterested();
    cursorTrack.isPinned().markInterested();
    cursorTrack.name().addValueObserver(this::updateStringSetting);
    for (int i = 0; i < numDevices; i++) {
      PinnableCursorDevice cursorDevice = devices.get(i);
      cursorDevice.name().addValueObserver(this::updateStringSetting);
    }
  }

  private void updateStringSetting(String s) {
    String cursorTrackString = cursorTrack.name().get();
    for (int i = 0; i < getNumDevices(); i++) {
      cursorTrackString += ":" + devices.get(i).name().get();
    }
    cursorTrackTarget.set(cursorTrackString);
  }

  private void setupCursorTrack(CursorTrack cursorTrack) {
    cursorTrack.volume().value().markInterested();
    cursorTrackParameters.add(cursorTrack.volume());
    cursorTrack.pan().value().markInterested();
    cursorTrackParameters.add(cursorTrack.pan());
  }

  private void setupSends(CursorTrack cursorTrack) {
    for (int i = 0; i < cursorTrack.sendBank().getSizeOfBank(); i++) {
      Send send = cursorTrack.sendBank().getItemAt(i);
      send.value().markInterested();
      sends.add(send);
    }
  }

  private void setupDevices(CursorTrack cursorTrack, String name, int numSends, int numDevices, int numRemoteControlPages, String remoteExpressionTag) {
    for (int deviceIndex = 0; deviceIndex < numDevices; deviceIndex++) {
      PinnableCursorDevice cursorDevice = cursorTrack.createCursorDevice(name + deviceIndex, name + deviceIndex, numSends, CursorDeviceFollowMode.FOLLOW_SELECTION);
      devices.add(cursorDevice);

      boolean useValidPageIndex = !remoteExpressionTag.isEmpty();

      List<OwRemoteControl> remoteControls = new ArrayList<>();
      for (int pageIndex = 0; pageIndex < numRemoteControlPages; pageIndex++) {
        CursorRemoteControlsPage remoteControlsPage = cursorDevice.createCursorRemoteControlsPage(name + pageIndex, PARAMETER_COUNT, remoteExpressionTag);
        OwRemoteControlsPage cursorPageLocked = new OwRemoteControlsPage(remoteControlsPage, pageIndex, useValidPageIndex);
        for (int remoteIndex = 0; remoteIndex < PARAMETER_COUNT; remoteIndex++) {
          RemoteControl remoteControl = remoteControlsPage.getParameter(remoteIndex);
          remoteControl.value().markInterested();
          remoteControl.exists().markInterested();
          OwRemoteControl owRemoteControl = new OwRemoteControl(remoteControl, cursorPageLocked, remoteIndex, pageIndex, useValidPageIndex);
          remoteControls.add(owRemoteControl);
        }
      }
      deviceRemoteControls.add(remoteControls);
    }
  }

  public void pinCursorTrack() {
    cursorTrack.isPinned().set(false);
    cursorTrack.isPinned().set(true);
  }

  public void pinCursorDevice(int index) {
    PinnableCursorDevice cursorDevice = devices.get(index);

    cursorDevice.isPinned().set(false);
    cursorDevice.isPinned().set(true);
  }

  private int getNumDevices(){
    return devices.size();
  }

  @Override
  public List<Double> getParameterData(){
    if(!cursorTrack.isPinned().get()) {
      return null;
    }
    List<Double> data = new ArrayList<>();
    cursorTrackParameters.forEach(parameter -> {
      data.add(parameter.get());
    });
    sends.forEach(send -> {
      data.add(send.get());
    });
    deviceRemoteControls.forEach(remoteControls -> {
      remoteControls.forEach(remoteControl -> {
        if(remoteControl.exists()) {
          data.add(remoteControl.getRemoteControl().value().get());
        } else {
          data.add(-1.0);
        }
      });
    });
    return data;
  }

  @Override
  public void setParameterData(List<Double> data){
    if(!cursorTrack.isPinned().get()) {
      return;
    }
    int index = 0;
    for (Parameter parameter : cursorTrackParameters) {
      setParameter(parameter, data.get(index));
      index++;
    }
    for (Send send : sends) {
      setParameter(send, data.get(index));
      index++;
    }

    for (List<OwRemoteControl> remoteControls : deviceRemoteControls) {
      for (OwRemoteControl remoteControl : remoteControls) {
        if(remoteControl.exists()) {
          RemoteControl remote = remoteControl.getRemoteControl();
          setParameter(remote, data.get(index));
        }
        index++;
      }
    }
  }

  private void setParameter(Parameter parameter, Double value){
    if(Double.compare(value, -1.0) != 0) {
      parameter.value().setImmediately(value);
    }
  }

  @Override
  public int getNumParameters() {
    return cursorTrackParameters.size() + sends.size() + (getNumDevices() * deviceRemoteControls.get(0).size());
  }


}
