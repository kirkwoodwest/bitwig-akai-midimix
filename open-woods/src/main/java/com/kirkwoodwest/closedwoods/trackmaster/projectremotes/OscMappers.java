package com.kirkwoodwest.closedwoods.trackmaster.projectremotes;

import com.bitwig.extension.api.Color;
import com.bitwig.extension.api.opensoundcontrol.OscConnection;
import com.bitwig.extension.api.opensoundcontrol.OscMessage;
import com.bitwig.extension.controller.api.Parameter;
import com.bitwig.extension.controller.api.RemoteControl;
import com.kirkwoodwest.openwoods.osc.OscController;

import java.util.ArrayList;
import java.util.List;

public class OscMappers {
  private static final String ID = "OSC_MAPPERS";
  private List<OscMapper> mappers = new ArrayList<OscMapper>();
  private OscMapper activeMapper;

  public OscMappers(OscController oscController, String oscPath){
    OscMappersPaths oscPaths = new OscMappersPaths(oscPath);
    //TODO: Fix value Types
//    oscController.registerOscCallback( oscPaths.MAP,"Maps Last Touched Parameter & Last Pressed Knob", this::doMap);
//
//    oscController.addLedString(ID, oscPaths.SELECTED_DEVICE_NAME, ()->{
//      if(activeMapper == null) return "";
//       return activeMapper.getSelectedDeviceName();
//    });
//
//    oscController.addLedString(ID, oscPaths.SELECTED_NAME, ()->{
//      if(activeMapper == null) return "";
//      return activeMapper.getSelectedName();
//    });
//
//    oscController.addLedString(ID, oscPaths.SELECTED_TRACK_NAME, ()->{
//      if(activeMapper== null) return "";
//      return activeMapper.getSelectedTrackName();
//    });
//
//    oscController.addLedColor(ID, oscPaths.SELECTED_COLOR, ()->{
//      if(activeMapper == null) return Color.blackColor();
//      return activeMapper.getSelectedColor();
//    });
//
//    oscController.addLedString(ID, oscPaths.SELECTED_DISPLAY_VALUE, ()-> {
//      if (activeMapper == null) return "";
//      RemoteControl activeRemote = activeMapper.getActiveRemote();
//      if (activeRemote == null) return "";
//      if (!activeRemote.exists().get()) return "";
//      return activeRemote.displayedValue().get();
//    });
//
//    oscController.addLedDouble(ID, oscPaths.SELECTED_VALUE, ()-> {
//      if (activeMapper == null) return 0.0;
//      RemoteControl activeRemote = activeMapper.getActiveRemote();
//      if (activeRemote == null) return 0.0;
//      if (!activeRemote.exists().get()) return 0.0;
//      return activeRemote.value().get();
//    });
  }

  private synchronized void doMap(OscConnection oscConnection, OscMessage oscMessage) {

    Parameter parameter = OscLastTouchedParameter.getInstance().getParameter();
    if(activeMapper == null) return;

    RemoteControl remoteControl = activeMapper.getActiveRemote();
    if(parameter != null && remoteControl != null) {
      remoteControl.deleteObject();
      remoteControl.isBeingMapped().set(true);
      parameter.touch(true);
      remoteControl.isBeingMapped().set(false);
      parameter.touch(false);

      activeMapper.setActiveColor();
      activeMapper.setActiveTrackName();
      activeMapper.setActiveDeviceName();
    } else if (parameter == null && remoteControl != null) {
      remoteControl.deleteObject();
    }
  }

  public synchronized void setActiveMapper(OscMapper oscMapper) {
    if(this.activeMapper != null && this.activeMapper != oscMapper) {
      this.activeMapper.setSelectedIndex(-1);
    }
    this.activeMapper = oscMapper;
  }
}
