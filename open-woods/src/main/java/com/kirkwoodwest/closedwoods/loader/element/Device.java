package com.kirkwoodwest.closedwoods.loader.element;

import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.CursorTrack;
import com.kirkwoodwest.openwoods.cursortrack.OscCursorDevice;
import com.kirkwoodwest.openwoods.osc.OscController;
import com.kirkwoodwest.openwoods.trackbank.CursorDeviceBank;
import com.kirkwoodwest.openwoods.trackbank.CursorDeviceHelper;
import com.kirkwoodwest.openwoods.trackbank.CursorTrackBank;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
[selector.device]
id= "device"
osc_path = "cursor/device"
parent_selector_index = 1
num_remote_pages = 2
max_devices = 32
num_devices = 1

selector

 */
public class Device {
  static final int MAX_DEVICES = 32;
  public static Map<String, ILoaderElement> createElement(ControllerHost host, OscController oscController, Map<String, Object> selector, CursorTrackBank cursorTrackBank, String oscPath) {
    @SuppressWarnings("unchecked")
    List<Map<String, Object>> devices = (List<Map<String, Object>>) selector.get("selector.device");

    HashMap<String, ILoaderElement> elements = new HashMap<>();
    if (devices != null) {
      if (!devices.isEmpty()) {

        Map<Integer, CursorDeviceHelper> cursorDeviceHelperMap = new HashMap<>();
        Map<Integer, CursorDeviceBank> cursorDeviceBankMap = new HashMap<>();

        for (Map<String, Object> device : devices) {
          final String deviceOscPath = oscPath + "/" + device.get("osc_path");
          final int parentSelectorIndex = (Integer) device.get("parent_selector_index") - 1;
          final int maxDevices = (Integer) device.get("max_devices");
          final int numDevices = (Integer) device.get("num_devices");
          final int numRemotePages = (Integer) device.get("remote_pages_count");

          CursorDeviceHelper cursorDeviceHelper;
          CursorDeviceBank cursorDeviceBank;
          CursorTrack cursorTrack = cursorTrackBank.getItemAt(parentSelectorIndex);

          //Get or create device helper...
          if(cursorDeviceHelperMap.containsKey(parentSelectorIndex)){
            cursorDeviceHelper = cursorDeviceHelperMap.get(parentSelectorIndex);
          } else {
            cursorDeviceHelper = new CursorDeviceHelper(host, cursorTrack, maxDevices);
            cursorDeviceHelperMap.put(parentSelectorIndex, cursorDeviceHelper);
          }

          if(cursorDeviceBankMap.containsKey(parentSelectorIndex)){
            cursorDeviceBank = cursorDeviceBankMap.get(parentSelectorIndex);
          } else {
            cursorDeviceBank = new CursorDeviceBank(host, cursorTrack, numDevices, cursorDeviceHelper, deviceOscPath);
            cursorDeviceBankMap.put(parentSelectorIndex, cursorDeviceBank);
          }

          OscCursorDevice oscCursorDevice = new OscCursorDevice(cursorDeviceBank.getItemAt(0), oscController, deviceOscPath, numRemotePages, "");
        }
      }
    }
    return elements;
  }

  /**
   * Get the number of sends for the mixer selector
   * @param selector
   * @return
   */
  public static int get(Map<String, Object> selector) {
    int numSends = 0;
    List<Map<String, Object>> mixers = (List<Map<String, Object>>) selector.get("selector.mixer");
    if (mixers != null) {
      for (Map<String, Object> mixer : mixers) {
        int sendCount = (Integer) mixer.get("num_sends");
        numSends = Math.max(numSends, sendCount);
      }
    }
    return numSends;
  }
}
