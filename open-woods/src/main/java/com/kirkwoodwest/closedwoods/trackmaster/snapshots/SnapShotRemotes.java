package com.kirkwoodwest.closedwoods.trackmaster.snapshots;

import com.bitwig.extension.controller.api.RemoteControl;
import com.kirkwoodwest.openwoods.remotecontrol.OwRemoteControl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SnapShotRemotes implements SnapShotBase {
  private final List<OwRemoteControl> remoteControls;

  public SnapShotRemotes(List<OwRemoteControl> remoteControls) {
    remoteControls.forEach(pagedRemoteControl -> {
      RemoteControl remoteControl = pagedRemoteControl.getRemoteControl();
      remoteControl.name().markInterested();
      remoteControl.value().markInterested();
    });
    this.remoteControls = remoteControls;
  }

  @Override
  public List<Double> getParameterData() {
    return remoteControls.stream().map(control -> {
      RemoteControl remoteControl = control.getRemoteControl();
      return remoteControl.value().get();
    }).collect(Collectors.toList());
  }

  @Override
  public void setParameterData(List<Double> data) {
    IntStream.range(0, data.size()).forEach(index -> {
      OwRemoteControl owRemoteControl = remoteControls.get(index);
      RemoteControl remoteControl = owRemoteControl.getRemoteControl();
      double value = data.get(index);
      if (Double.compare(value, -1.0) != 0) {
        remoteControl.value().set(value);
      }
    });
  }

  @Override
  public int getNumParameters() {
    return remoteControls.size();
  }
}
