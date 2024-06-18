package com.kirkwoodwest.openwoods.remotecontrol;
import com.bitwig.extension.controller.api.*;
import com.kirkwoodwest.openwoods.values.BooleanValueImpl;
import com.kirkwoodwest.openwoods.values.SettableBooleanValueImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

// Class to represent a remote control
// Adds Functionality with the OwRemoteControlsPage to allow for locking to specific remote pages by index
public class OwRemoteControl {
  private final RemoteControl remoteControl;
  private final OwRemoteControlsPage owRemoteControlsPage;
  private final int remoteIndex;
  private int pageIndex;
  private SettableBooleanValue remoteExists = new SettableBooleanValueImpl(false);
  private boolean pageExists;
  private boolean existsInternal;
  private boolean isLocked;
  private List<Consumer<Boolean>> existsConsumers; // List to hold multiple consumers

  public OwRemoteControl(RemoteControl remoteControl, OwRemoteControlsPage owRemoteControlsPage,
                         int remoteIndex, int pageIndex, boolean isLocked) {
    remoteControl.exists().addValueObserver(this::updateRemoteExists);

    owRemoteControlsPage.exists().addValueObserver(this::updatePageExists);

    this.remoteControl = remoteControl;
    this.owRemoteControlsPage = owRemoteControlsPage;
    this.remoteIndex = remoteIndex;
    this.pageIndex = pageIndex;
    this.isLocked = isLocked;
    this.existsConsumers = new ArrayList<>(); // Initialize the list of consumers
  }

  public RemoteControl getRemoteControl() {
    return remoteControl;
  }

  public boolean exists() {
    return existsInternal;
  }

  private void updateRemoteExists(boolean b) {
    remoteExists.set(b);
    updateExists();
  }

  private void updatePageExists(Boolean b) {
    pageExists = b;
    updateExists();
  }

  public void updateExists() {
    boolean exists = remoteControl.exists().get() && (!isLocked || owRemoteControlsPage.exists().get());
    if(exists != existsInternal) {
      existsInternal = exists;
      for(Consumer<Boolean> consumer : existsConsumers) {
        consumer.accept(exists);
      }
    }
  }
  private BooleanValue getRemoteExists() {
    return remoteExists;
  }

  public int getPageIndex() {
    return pageIndex;
  }

  public void setPageIndex(int pageIndex) {
    this.pageIndex = pageIndex;
  }

  public void setLocked(boolean useValidPageIndex) {
    this.isLocked = useValidPageIndex;
  }

  public int getRemoteIndex() {
    return remoteIndex;
  }

  // Method to add a consumer to the list
  public void addExistsConsumer(Consumer<Boolean> existsConsumer) {
    this.existsConsumers.add(existsConsumer);
  }
}
