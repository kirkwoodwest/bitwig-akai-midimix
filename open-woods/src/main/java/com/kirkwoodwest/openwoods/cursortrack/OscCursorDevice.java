package com.kirkwoodwest.openwoods.cursortrack;

import com.bitwig.extension.controller.api.*;
import com.kirkwoodwest.closedwoods.trackmaster.projectremotes.OscLastTouchedParameter;
import com.kirkwoodwest.openwoods.osc.OscController;
import com.kirkwoodwest.openwoods.remotecontrol.OwRemoteControl;
import com.kirkwoodwest.openwoods.remotecontrol.OwRemoteControlsPage;
import com.kirkwoodwest.closedwoods.osc.OwRemoteControlOscAdapter;

public class OscCursorDevice {
  public String ID = "CursorDevice";
  private final PinnableCursorDevice cursorDevice;
  private final OscCursorTrackPaths path;

  public OscCursorDevice(PinnableCursorDevice pinnableCursorDevice, OscController oscController, String oscPath, int numRemotePages, String filterExpression) {
    this.path = new OscCursorTrackPaths(oscPath);
    this.cursorDevice = pinnableCursorDevice;
    //Led for Cursor Device name
    cursorDevice.position().addValueObserver((v) -> {
      oscController.forceNextFlush("OscCursorRemote");
    });

//    CursorDeviceLayer cursorLayer = cursorDevice.createCursorLayer();
//    DeviceBank deviceBank = cursorLayer.createDeviceBank(20);
//    for (int i = 0; i < 20; i++) {
//      final int index = i;
//      deviceBank.getDevice(i).exists().markInterested();
//      deviceBank.getDevice(i).name().markInterested();
//      //callback on name i want a list of names...
//      deviceBank.getDevice(i).name().addValueObserver((v) -> {
//        LogUtil.print("Device Name: "+oscPath + " | [" + index + "/" + v + "]");
//      });
//    }

    //Layer Bank
    DeviceLayerBank layerBank = cursorDevice.createLayerBank(16);
    oscController.addBooleanValue(ID, path.LAYER_BANK_EXISTS, layerBank.exists());

    //Loop through items of layer bank and set up observers of the names
    for (int i = 0; i < 16; i++) {
      oscController.addStringValue(ID, path.getLayerBankName(i), layerBank.getItemAt(i).name());
    }

    //Chain/Layer Selector Stuff
    ChainSelector chainSelector = cursorDevice.createChainSelector();
    oscController.addBooleanValue(ID, path.CHAIN_EXISTS, chainSelector.exists());
    oscController.addIntegerValue(ID, path.CHAIN_COUNT, chainSelector.chainCount());
    oscController.addSettableIntegerValue(ID, path.SET_CHAIN_INDEX, chainSelector.activeChainIndex(), "Active Chain Index");

    oscController.addIndexedPathInfo(path.getRemotePage(), "Remote Page Index");
    oscController.addIndexedPathInfo(path.getRemoteParameter(), "Parameter Index");
    for (int pageIndex = 0; pageIndex < numRemotePages; pageIndex++) {
      //Create Remote Page
      final String cursorRemotePageName = "CURSOR_REMOTE_" + pageIndex;
      final int paramCount = 8;
      CursorRemoteControlsPage cursorRemoteControlsPage = cursorDevice.createCursorRemoteControlsPage(cursorRemotePageName, paramCount, filterExpression);

      boolean isLocked = filterExpression.isEmpty();
      OwRemoteControlsPage owRemoteControlsPage = new OwRemoteControlsPage(cursorRemoteControlsPage, pageIndex, true);

      //Create Remote Page Name
      oscController.addStringValue(ID, path.getRemotePageName(pageIndex), cursorRemoteControlsPage.getName());

      //Clear Parameter when pages change
      cursorRemoteControlsPage.getName().addValueObserver((v) -> {
        OscLastTouchedParameter.getInstance().clearParameter();
      });

      //Create Parameters and add them to multiple remotes
      for (int parameterIndex = 0; parameterIndex < paramCount; parameterIndex++) {
        RemoteControl remoteControl = cursorRemoteControlsPage.getParameter(parameterIndex);

        String parameterPath = this.path.getParameter(pageIndex, parameterIndex);
        OwRemoteControl owRemoteControl = new OwRemoteControl(remoteControl, owRemoteControlsPage, parameterIndex, pageIndex, true);
        OwRemoteControlOscAdapter owRemoteControlOscAdapter = new OwRemoteControlOscAdapter(owRemoteControl, oscController, parameterPath, "Remote Control Parameter " + path.getParameterIndex(pageIndex, parameterIndex));
        oscController.addAdapter(ID, owRemoteControlOscAdapter);
      }
    }
  }
}
