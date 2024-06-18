package com.kirkwoodwest.closedwoods.trackmaster.pluginwindow;

import com.bitwig.extension.controller.api.*;
import com.kirkwoodwest.openwoods.values.SettableBooleanValueImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Garbage Utility Class that opens up the currently selected top level device and their respective chains
 * Device
 *  -> Device Slots
 *  -> LayerBank
 *    -> Layer
 *      ->Device Bank
 *          ->Device
 *          ->Device Slots
 *            ->Device
 */
public class CursorDevicePluginWindow {
  private final ArrayList<Device> devices = new ArrayList<>();
  private static final int NUM_SLOT_CHAIN = 16;
  private final List<Boolean> status;
  private final List<Boolean> exists;
  int NUM_DEVICE_CHAIN = 16;
  int NUM_LAYER_BANK = 16;
  private SettableBooleanValueImpl pluginWindowsOpen = new SettableBooleanValueImpl(false);
  private SettableBooleanValueImpl pluginExists = new SettableBooleanValueImpl(false);

  public CursorDevicePluginWindow(CursorDevice cursorDevice) {
    addToDeviceList(cursorDevice);
    createDeviceSlots(cursorDevice);
    createLayerBank(cursorDevice);
    status = new ArrayList<>();
    exists = new ArrayList<>();
    IntStream.range(0, devices.size()).forEach((i)-> {
      status.add(false);
      devices.get(i).isWindowOpen().addValueObserver((b) -> {
        status.set(i, b);
        updateStatus();
      });
      exists.add(false);
      devices.get(i).exists().addValueObserver((b) -> {
        exists.set(i, b);
        updateExists();
      });
    });
  }

  private void updateExists() {
    pluginExists.set(exists.stream().anyMatch((a)->a));
  }

  private void updateStatus() {
    pluginWindowsOpen.set(status.stream().anyMatch((a)->a));
  }

  public void createLayerBank(Device device){
    DeviceLayerBank layerBank = device.createLayerBank(NUM_LAYER_BANK);
    for (int i = 0; i < NUM_LAYER_BANK; i++) {
      DeviceLayer deviceLayer = layerBank.getItemAt(i);
      DeviceBank deviceBank = deviceLayer.createDeviceBank(NUM_DEVICE_CHAIN);
      for (int j = 0; j < NUM_DEVICE_CHAIN; j++) {
        Device device1 = deviceBank.getDevice(j);
        addToDeviceList(device1);
        createDeviceSlots(device1);
      }
    }
  }

  public void createDeviceSlots(Device device){
    DeviceBank slotBank = device.getCursorSlot().createDeviceBank(NUM_SLOT_CHAIN);
    for (int j = 0; j < NUM_SLOT_CHAIN; j++) {
      Device deviceSlot = slotBank.getItemAt(j);
      addToDeviceList(deviceSlot);
    }
  }

  public void openPluginWindows(boolean b) {
    devices.forEach((device)->{
      if(device.exists().get()) {
        if(device.isPlugin().get()) {
          device.isWindowOpen().set(b);
        }
      }
    });
  }

  public BooleanValue isPluginWindowsOpen(){
    return pluginWindowsOpen;
  }

  public BooleanValue pluginExists(){
    return pluginExists;
  }

  private void addToDeviceList(Device device) {
    markDeviceInterested(device);
    devices.add(device);
  }
  private void markDeviceInterested(Device device){
    device.name().markInterested();
    device.exists().markInterested();
    device.isPlugin().markInterested();
    device.isWindowOpen().markInterested();
  }
}
