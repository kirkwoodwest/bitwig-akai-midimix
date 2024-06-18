package com.kirkwoodwest.openwoods.trackbank;

import com.bitwig.extension.controller.api.*;
import com.kirkwoodwest.openwoods.settings.ShowScriptConsole;
import com.kirkwoodwest.utils.StringUtil;

import java.util.stream.IntStream;

/**
 * A basic implementation of device bank which allows you to get visible devices by name or index.
 * Useful for getting a cursor device to pin to a specific device.
 *
 * This item will always be locked to scroll position 0, and will always be in sync with current view.
 */
public class CursorDeviceHelper implements Helper{
  private final DeviceBank deviceBank;
  private final ControllerHost host;

  public CursorDeviceHelper(ControllerHost host, CursorTrack cursorTrack, int numDevices){
    this.host = host;
    deviceBank = cursorTrack.createDeviceBank(numDevices);
    deviceBank.scrollPosition().markInterested();
    IntStream.range(0, deviceBank.getSizeOfBank())
            .forEach(deviceIndex -> {
              deviceBank.getItemAt(deviceIndex).name().markInterested();
              deviceBank.getItemAt(deviceIndex).exists().markInterested();
            });
  }

  public Device getDevice(int index){
    if(index < 0 || index >= deviceBank.getSizeOfBank()){
      return null;
    }
    return deviceBank.getItemAt(index);
  }

  public Device getDeviceByName(String name){
    return IntStream.range(0, deviceBank.getSizeOfBank())
            .mapToObj(deviceBank::getItemAt)
            .filter(deviceItem -> deviceItem.name().get().equals(name))
            .findFirst().orElse(null);
  }

  public int getIndexByName(String name){
    return IntStream.range(0, deviceBank.getSizeOfBank())
            .filter(deviceIndex -> deviceBank.getItemAt(deviceIndex).name().get().equals(name))
            .findFirst().orElse(-1);
  }

  public int getSizeOfBank() {
    return this.deviceBank.getSizeOfBank();
  }

  public void debugBank(){
    host.println("---- Debug Cursor Devices");
    host.println("Bank Size: " + deviceBank.getSizeOfBank());
    host.println("Scroll Position: " + deviceBank.scrollPosition().get());
    host.println("-----");

    IntStream.range(0, deviceBank.getSizeOfBank())
            .forEach(deviceIndex -> {
              Device device = deviceBank.getItemAt(deviceIndex);
              if(!device.name().get().isEmpty()) {
                host.println("" + StringUtil.padIntRight(3,deviceIndex) + " : " + device.name().get());
              }
            });
    host.println("-----");
  }
}
