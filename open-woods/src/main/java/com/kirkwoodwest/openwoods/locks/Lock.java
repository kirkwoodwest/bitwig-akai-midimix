package com.kirkwoodwest.openwoods.locks;

import com.bitwig.extension.controller.api.CursorRemoteControlsPage;
import com.bitwig.extension.controller.api.DeviceBank;
import com.bitwig.extension.controller.api.DeviceLayerBank;

public abstract class Lock<T> {
  int index = 0;
  public void setIndex(int i){
    this.index = i;
    reset();
  };
  public abstract void reset();

  public static Lock<?> create(Object type, int index) {
    // Example: Create a LockCursorRemotePage if type matches
    if (type instanceof CursorRemoteControlsPage) {
      int pageIndex = (int) index; // Assuming the first param is the pageIndex
      return new LockCursorRemotePage((CursorRemoteControlsPage) type, pageIndex);
    } else if (type instanceof DeviceLayerBank) {
      int scrollPosition = (int) index; // Assuming the first param is the scrollPosition
      return new LockDeviceLayerBank((DeviceLayerBank) type, scrollPosition);
    } else if (type instanceof DeviceBank) {
      int scrollPosition = (int) index; // Assuming the first param is the scrollPosition
      return new LockDeviceBank((DeviceBank) type, scrollPosition);
    }
    // Other cases for different types of Locks
    // ...

    throw new IllegalArgumentException("Unsupported lock type");
  }
}