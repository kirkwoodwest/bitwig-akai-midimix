package com.kirkwoodwest.openwoods.trackbank;
import com.bitwig.extension.controller.api.*;
import com.kirkwoodwest.openwoods.settings.SettingsHelper;
import com.kirkwoodwest.openwoods.settings.ShowScriptConsole;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static com.kirkwoodwest.utils.StringUtil.padInt;

/**
 * This is a helper class to create multiple cursor devices and position them using a reference name and index.
 * This index is limited to the size of the CursorDeviceHelper size of Bank.
 * Simulates a device bank for multiple cursor devices, allowing you to create a collection of devices.
 */
public class CursorDeviceBank {

  private final ArrayList<PinnableCursorDevice> cursorDevices = new ArrayList<>();
  private final ControllerHost host;
  private final CursorDeviceHelper cursorDeviceHelper;
  private final String name;
  private String settingsName = "Cursor Device Bank";


  private SettableStringValue settingTargetName;
  private Signal settingTargetSet;
  private Action actionOpenControllerConsole;

  //Creates any number of Cursor Devices on a cursor Track
  public CursorDeviceBank(ControllerHost host, CursorTrack cursorTrack, int numDevices, CursorDeviceHelper cursorDeviceHelper, String name) {
    this.host = host;
    this.cursorDeviceHelper = cursorDeviceHelper;
    this.name = name;

    IntStream.range(0, numDevices)
            .forEach(i -> {
              PinnableCursorDevice cursorDevice = cursorTrack.createCursorDevice(name + i, name + i, 0, CursorDeviceFollowMode.FOLLOW_SELECTION);
              setupCursorDevice(cursorDevice, i);
            });
    generateSettingsControls(host, name);
  }

  private void setupCursorDevice(PinnableCursorDevice cursorDevice, int index) {
    cursorDevice.exists().markInterested();
    cursorDevice.name().markInterested();
    cursorDevice.position().markInterested();
    cursorDevices.add(cursorDevice);
  }

  private void generateSettingsControls(ControllerHost host, String settingsName) {
    this.settingsName = settingsName;
    DocumentState documentState = host.getDocumentState();

    actionOpenControllerConsole = ShowScriptConsole.createAction(host);
    settingTargetName = documentState.getStringSetting(settingsName + " [Cursors Selection]", "Cursor Device Bank", 24, "");
    settingTargetName.markInterested();
    settingTargetSet = documentState.getSignalSetting(settingsName + " [Set Cursors]", "Cursor Device Bank", "Set");
    settingTargetSet.addSignalObserver(this::set);
    Signal settingDebug = documentState.getSignalSetting((settingsName + " [Debug]"), "Cursor Device Bank", "Debug");
    settingDebug.addSignalObserver(this::debugBank);

    //Hide settings
    SettingsHelper.setVisible(settingTargetName, false);
    SettingsHelper.setVisible(settingTargetSet, false);
    SettingsHelper.setVisible(settingDebug, false);

  }

  public int getSizeOfBank() {
    return cursorDevices.size();
  }

  public PinnableCursorDevice getItemAt(int index) {
    return cursorDevices.get(index);
  }

  public void positionAllByDeviceName(String deviceName) {
    positionAllByFirstDeviceIndex(cursorDeviceHelper.getIndexByName(deviceName));
  }

  private void positionAllByFirstDeviceIndex(int deviceIndex) {
    IntStream.range(0, cursorDevices.size())
            .forEach(index -> {
              CursorDevice cursorDevice = cursorDevices.get(index);
              positionCursorByDeviceIndex(index, deviceIndex + index);
            });
  }

  public void positionCursorByDeviceIndex(int cursorIndex, int deviceIndex) {
    PinnableCursorDevice cursorDevice = cursorDevices.get(cursorIndex);
    Device device = cursorDeviceHelper.getDevice(deviceIndex);
    if (device != null) {
      cursorDevice.isPinned().set(false);
      cursorDevice.selectDevice(device);
      cursorDevice.isPinned().set(true);
    }
  }

  public void debugBank() {
    host.println("---- " + name + " debug ");
    host.println("Bank Size: " + cursorDevices.size());
    host.println("-----");
    IntStream.range(0, cursorDevices.size()).forEach(i -> {
      CursorDevice cursorDevice = cursorDevices.get(i);
      String name = cursorDevice.name().get();
      if (!name.isEmpty()) {
        host.println("" + padInt(3, i) + " : " + cursorDevice.name().get());
      }
    });
    host.println("-----");
  }

  public void set() {
    String s = settingTargetName.get();
    if (s.isEmpty()) return;
    try {
      List<Integer> indexes = IndexParser.parseIndexes(s, cursorDeviceHelper, this.getSizeOfBank(), cursorDeviceHelper.getSizeOfBank());
      if (indexes.get(0).equals(-1)) {
        host.showPopupNotification("Invalid String Selection");
      } else if (indexes.size() != cursorDevices.size()) {
        host.showPopupNotification("Mismatched selection: You provided " + indexes.size() + " devices, but there are " + cursorDevices.size() + " devices.");
        //print indexes user provided
        host.println("-----------------------");
        host.println(settingsName);
        host.println("String Provided: " + s);
        host.println("Number of Cursor Devices: " + indexes.size());
        host.println("Indexes Provided: " + indexes);
      } else {
        positionAllByFirstDeviceIndex(indexes.get(0));
        host.showPopupNotification("Positions Updated:" + indexes);
      }
    } catch (Exception e) {
      host.showPopupNotification(e.getMessage());
      host.println("-----------------------");
      host.println("OPEN WOODS HELP");
      host.println("Your selection string was: " + s);
      host.println("Something is wrong with it. Refer to the help below.");
      host.println(IndexParser.getHelpString(settingsName));
    }
  }

  public void setDeviceTargets(String s) {
    settingTargetName.set(s);
    set();
  }
}