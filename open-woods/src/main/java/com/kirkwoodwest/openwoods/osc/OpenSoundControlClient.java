package com.kirkwoodwest.openwoods.osc;

import com.bitwig.extension.api.opensoundcontrol.OscAddressSpace;
import com.bitwig.extension.api.opensoundcontrol.OscConnection;
import com.bitwig.extension.api.opensoundcontrol.OscInvalidArgumentTypeException;
import com.bitwig.extension.api.opensoundcontrol.OscModule;
import com.bitwig.extension.controller.api.ControllerHost;

import java.io.IOException;
import java.net.PortUnreachableException;
import java.util.Arrays;


//Sets Up the OSC Client and updates its own status
class OpenSoundControlClient {

  private final OscConnection oscConnection;
  private final ControllerHost host;
  private final String oscPath;
  private final String address;
  private final int port;
  private String status = "Not Connected";
  private boolean debug;
  private String debugFilter;

  public OpenSoundControlClient(ControllerHost host, OscModule oscModule, OscAddressSpace addressSpace, String address, int port, String oscPath) {
    this.host = host;

    this.oscPath = oscPath;
    this.address = address;
    this.port = port;
    oscConnection = oscModule.connectToUdpServer(address, port, addressSpace);
    setStatus("Connected: " + address + ":" + port);
  }

  public void send(String address, Object... data) {
    address = oscPath + address;
    try {
      oscConnection.sendMessage(address, data);
      //If debug mode and filter is empty or address contains filter
      if (debug && (debugFilter.isEmpty() || address.contains(debugFilter))) {
        host.println("Sending OSC Message: " + address + " " + Arrays.toString(data));
      }
    } catch (OscInvalidArgumentTypeException e) {
      host.println("OscInvalidArgumentTypeException: " + e);
    } catch (PortUnreachableException e) {
      host.showPopupNotification("Cannot Reach: " + address +":" +port + "Check Your OSC Configuration" );
    } catch (IOException e) {
      host.println("IOException:" + e);
    }
  }


  private void setStatus(String status) {
    this.status = status;
  }

  public String getStatus() {
    return status;
  }

  public void setDebugEnabled(boolean enabled) {
    this.debug = enabled;
  }

  public void setDebugFilter(String debugFilter) {
    this.debugFilter = debugFilter;
  }
}