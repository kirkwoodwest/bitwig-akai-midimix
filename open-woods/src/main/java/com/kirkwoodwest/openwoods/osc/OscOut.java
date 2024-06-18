package com.kirkwoodwest.openwoods.osc;

import com.bitwig.extension.api.opensoundcontrol.OscAddressSpace;
import com.bitwig.extension.api.opensoundcontrol.OscConnection;
import com.bitwig.extension.api.opensoundcontrol.OscModule;
import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.SettableBooleanValue;
import com.bitwig.extension.controller.api.SettableStringValue;
import com.kirkwoodwest.openwoods.settings.NumberSetting;

class OscOut {
  private final String ADDRESS_OUT_DEFAULT = "192.168.86.26";
  private final int PORT_OUT_DEFAULT = 9000;
  private final String address;
  private final int port;
  private boolean enabled = false;
  private OscConnection oscConnection;

  public OscOut(ControllerHost host, int index, boolean isOnlyOutput) {
    String indexString = isOnlyOutput ? "" : " " + ( index+1);

    //Address Out
    SettableStringValue settingAddressOut = host.getPreferences().getStringSetting("Address Out" + indexString, "Osc Output" + indexString, 20, ADDRESS_OUT_DEFAULT);
    settingAddressOut.markInterested();
    address = settingAddressOut.get();

    //Port Out
    NumberSetting settingPortOut = new NumberSetting(host.getPreferences(), "Port Out" + indexString, "Osc Output" + indexString, 8000, 9999, 1, "", PORT_OUT_DEFAULT);
    port = (int) settingPortOut.get();

    if(isOnlyOutput) {
      //If only one output, always enabled
      enabled = true;
    } else {
      //Enabled
      SettableBooleanValue oscOutSettingEnabled = host.getPreferences().getBooleanSetting("OSC Out Enabled" + indexString, "Osc Output" + indexString, false);
      oscOutSettingEnabled.markInterested();
      enabled = oscOutSettingEnabled.get();

      oscOutSettingEnabled.addValueObserver((v) -> {
        enabled = v;
      });
    }
  }

  public void doConnection(OscModule oscModule, OscAddressSpace addressSpace){
    oscConnection = oscModule.connectToUdpServer(address, port, addressSpace);
  }
  public OscConnection getOscConnection() {
    return oscConnection;
  }

  public boolean getEnabled() {
    return enabled;
  }

  public String getAddress() {
    return address;
  }

  public int getPort() {
    return port;
  }
}
