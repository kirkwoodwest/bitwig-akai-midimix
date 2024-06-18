package com.kirkwoodwest.closedwoods.trackmaster.xytool;

import com.bitwig.extension.controller.api.*;
import com.kirkwoodwest.closedwoods.osc.OwRemoteControlOscAdapter;
import com.kirkwoodwest.openwoods.osc.OscController;
import com.kirkwoodwest.openwoods.osc.OscHost;
import com.kirkwoodwest.openwoods.remotecontrol.OwRemoteBuilder;
import com.kirkwoodwest.openwoods.remotecontrol.OwRemoteControl;
import com.kirkwoodwest.openwoods.remotecontrol.OwRemoteControlsPage;

public class XYTool {
  private String[] parameterTargets = new String[]{"/touch", "/x", "/y", "/hold"};
  public static final String ID = "XYTool";
  private final int index;
  public XYTool(ControllerHost host, OscController oscController, String oscTarget, int index){
    this.index = index;
    //translate oscTarget to include index...
    oscTarget = oscTarget + "/" + index;
    OscHost oscHost = oscController.getOscHost();

    int numParameters = parameterTargets.length;
    CursorTrack cursorTrack = host.createCursorTrack("XY Channel " + index, "XY Channel " + index, 0, 0, true);
    PinnableCursorDevice cursorDevice = cursorTrack.createCursorDevice("XY Device " + index, "XY Device " + index, 0, CursorDeviceFollowMode.FOLLOW_SELECTION);


    OwRemoteControlsPage xytool = OwRemoteBuilder.buildDeviceRemotes(cursorDevice, "XY Remot Page " + oscTarget, numParameters, "xytool", 1, false);

    for (int i = 0; i < numParameters; i++) {
      OwRemoteControl owRemoteControl = xytool.getRemoteControl(i);
      owRemoteControl.getRemoteControl().setIndication(false);
      String oscParameterTarget = oscTarget + parameterTargets[i];
      String oscDescription = "XY Tool" + (index+1) + ":" + i + 1;
      OwRemoteControlOscAdapter adapter = new OwRemoteControlOscAdapter(owRemoteControl, oscController, oscParameterTarget, oscDescription);
      oscController.addAdapter(ID, adapter);
    }

    {
      //Track Name
      oscController.addStringValue(ID, oscTarget + "/track_name", cursorTrack.name());
      //Device Name
      oscController.addStringValue(ID, oscTarget + "/device_name", cursorDevice.name());
      //Color
      oscController.addSettableColorValue(ID, oscTarget + "/color", cursorTrack.color(), "Track Color");
      //Is Pinned
      oscController.addSettableBooleanValue(ID, oscTarget + "/is_pinned", cursorTrack.isPinned(),"Is XY Tool " + index + " pinned?" );
    }

    //Selection
    oscHost.registerOscCallback(oscTarget + "/select", "XY Tool Pin Track" + index, (oscConnection, oscMessage)->{
      cursorTrack.isPinned().set(false);
      cursorTrack.isPinned().set(true);
      cursorDevice.isPinned().set(false);
      cursorDevice.isPinned().set(true);
    });

    //Nav
    oscHost.registerOscCallback(oscTarget + "/nav", "XY Tool Navigate to track" + index, (oscConnection, oscMessage)->{
      cursorTrack.selectInEditor();
    });
  }

}
