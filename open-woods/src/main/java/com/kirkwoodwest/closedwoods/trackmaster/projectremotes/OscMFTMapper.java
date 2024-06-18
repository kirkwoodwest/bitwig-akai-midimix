package com.kirkwoodwest.closedwoods.trackmaster.projectremotes;
import com.bitwig.extension.api.Color;
import com.kirkwoodwest.openwoods.remotecontrol.OwRemoteControl;
import com.kirkwoodwest.openwoods.savelist.ColorCollection;
import com.kirkwoodwest.openwoods.savelist.StringCollection;
import com.kirkwoodwest.openwoods.osc.OscController;
import com.kirkwoodwest.extensions.hardware.MFT.HardwareMFT;

import java.util.ArrayList;

//Class to handle midifighter mapping
public class OscMFTMapper extends OscMapper {
  public static final String ID = "OSC_MFT_MAPPER";
  public static final String ID_ADVANCED = "OSC_MFT_MAPPER_ADVANCED";

  public OscMFTMapper(OscMappers oscMappers, HardwareMFT hardwareMFT, ArrayList<OwRemoteControl> remoteControls, OscController oscController, String oscPath, ColorCollection colors, StringCollection trackNames, StringCollection deviceNames){
    super(oscMappers, oscPath, remoteControls, colors, trackNames, deviceNames);

    //Get encoders
    int encoderCount = hardwareMFT.getEncoderCount();
    for (int i = 0; i < encoderCount; i++) {
      final int index = i;
      final int oscIndex = i + 1;

      //TODO: Fix value types...
//      oscController.addLedBoolean("MFTProjectRemotes", oscPaths.getSelected(oscIndex), ()-> {
//        return index == getSelectedIndex();
//      });
//
//      oscController.addLedColor(ID, oscPaths.getColor(oscIndex), ()->{
//        if(remoteControls.get(index).exists()) {
//          return colors.getColor(index);
//        } else {
//          return Color.fromRGB(0.5,0.5,0.5);
//        }
//      });
//
//      oscController.addLedString(ID, oscPaths.getTrackName(oscIndex), ()->{
//        if(remoteControls.get(index).exists()) {
//          return trackNames.getString(index);
//        } else {
//          return "";
//        }
//      });
//
//      oscController.addLedString(ID, oscPaths.getDeviceName(oscIndex), ()->{
//        if(remoteControls.get(index).exists()) {
//          return deviceNames.getString(index);
//        } else {
//          return "";
//        }
//      });
//
//      //Map Parameter
//      oscController.addLedOscPagedRemoteControl(ID_ADVANCED, oscPaths.getRemote(oscIndex), remoteControls.get(index), "MFT Remote " + i);
//    }
//
//    oscController.addLedInteger(ID, oscPaths.SELECTED_INDEX, this::getSelectedIndex);
    }
  }
}
