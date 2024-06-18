package com.kirkwoodwest.openwoods.locks;

import com.bitwig.extension.controller.api.DeviceLayerBank;

class LockDeviceLayerBank extends Lock<DeviceLayerBank> {
  private final DeviceLayerBank bank;

  public LockDeviceLayerBank(DeviceLayerBank bank, int scrollPosition){
    this.bank = bank;
    bank.itemCount().addValueObserver((index)->{
      reset();
    });
    bank.scrollPosition().addValueObserver((index)->{
      reset();
    });
  }

  public void reset() {
    if(bank.scrollPosition().get() != index) {
      bank.scrollPosition().set(index);
    }
  }
}
