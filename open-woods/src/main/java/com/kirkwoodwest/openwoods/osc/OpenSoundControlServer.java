package com.kirkwoodwest.openwoods.osc;

import com.bitwig.extension.api.opensoundcontrol.OscAddressSpace;
import com.bitwig.extension.api.opensoundcontrol.OscMethodCallback;
import com.bitwig.extension.api.opensoundcontrol.OscModule;
import com.bitwig.extension.controller.api.ControllerHost;

//Set up the OSC Server and update its own status
class OpenSoundControlServer {
  private final ControllerHost host;
  private final OscModule oscModule;
  private OscAddressSpace addressSpace;
  private final int port;
  private final String oscPath;
  private String status = "Not Connected";
  private boolean debugEnabled;

  public OpenSoundControlServer(ControllerHost host, OscModule oscModule, OscAddressSpace addressSpace, int port, String oscPath) {
    this.host = host;
    this.oscModule = oscModule;
    this.addressSpace = addressSpace;
    this.port = port;
    this.oscPath = oscPath;

    oscModule.createUdpServer(port, addressSpace);
    addressSpace.registerDefaultMethod((oscConnection, oscMessage)->{
      host.println("No callback method created for: " + oscMessage.getAddressPattern() + " " + oscMessage.getAddressPattern());
    });

    setStatus("RUNNING: " + port);
  }

  public void registerOscCallback(String target, String description, OscMethodCallback callback) {
    addressSpace.registerMethod(oscPath + target, "*", description, callback);
  }

  public void registerOscCallback(String target, String typeTagPattern, String description, OscMethodCallback callback) {
    addressSpace.registerMethod(oscPath + target, typeTagPattern, description, callback);
  }

  public void registerDefaultCallback(OscMethodCallback callback) {
    addressSpace.registerDefaultMethod(callback);
  }

  private void setStatus(String s) {
    status = s;
  }

  public String getStatus() {
    return status;
  }

  public void setDebugEnabled(boolean enabled) {
    debugEnabled = enabled;
    addressSpace.setShouldLogMessages(debugEnabled);
  }
}
