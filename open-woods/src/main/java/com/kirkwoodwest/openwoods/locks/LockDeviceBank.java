package com.kirkwoodwest.openwoods.locks;

import com.bitwig.extension.controller.api.DeviceBank;

/**
 * Functionality to set a device bank to a specific scroll postion
 */
class LockDeviceBank extends Lock<DeviceBank> {

  private final DeviceBank bank;

  public LockDeviceBank(DeviceBank bank, int scrollPosition){
    this.bank = bank;
    this.index = scrollPosition;
    bank.itemCount().addValueObserver((index)->{
      bank.scrollPosition().set(scrollPosition);
    });
    bank.scrollPosition().addValueObserver((index)->{
      if(scrollPosition != index) {
        bank.scrollPosition().set(scrollPosition);
      }
    });
  }

  @Override
  public void reset() {
    int position = bank.scrollPosition().get();
    if(position != index) {
      bank.scrollPosition().set(position);
    }
  }
}
