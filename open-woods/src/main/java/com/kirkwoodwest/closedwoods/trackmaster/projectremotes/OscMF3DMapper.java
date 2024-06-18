package com.kirkwoodwest.closedwoods.trackmaster.projectremotes;

import com.bitwig.extension.api.Color;
import com.kirkwoodwest.openwoods.remotecontrol.OwRemoteControl;
import com.kirkwoodwest.openwoods.savelist.ColorCollection;
import com.kirkwoodwest.openwoods.savelist.DoubleRangeCollection;
import com.kirkwoodwest.openwoods.savelist.StringValueCollectionSaved;
import com.kirkwoodwest.openwoods.osc.OscController;
import com.kirkwoodwest.extensions.hardware.MF3D.HardwareMF3D;

import java.util.ArrayList;

public class OscMF3DMapper extends OscMapper {
  public static final String ID = "OSC_MF3D_MAPPER";
  public static final String ID_ADVANCED = "OSC_MFT_MAPPER_ADVANCED";

  public OscMF3DMapper(OscMappers oscMapper, HardwareMF3D hardwareMF3D, ArrayList<OwRemoteControl> remoteControls, OscController oscController, String oscPath, ColorCollection colors, StringValueCollectionSaved trackNames, StringValueCollectionSaved deviceNames, DoubleRangeCollection ranges) {
    super(oscMapper, oscPath, remoteControls, colors, trackNames, deviceNames);

    //Get encoders
    int buttonCount = hardwareMF3D.getArcadeButtonCount();
    for (int i = 0; i < buttonCount; i++) {
      final int index = i;
      final int oscIndex = i + 1;

      //TODO: Convert data types...
//
//      oscController.addBooleanValue("MFTProjectRemotes", oscPaths.getSelected(oscIndex), ()-> {
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
//      oscController.addLedDouble(ID, oscPaths.getRangeMin(oscIndex), ()->{
//        return ranges.getMin(index);
//      });
//      oscController.registerOscCallback(oscPaths.getRangeMin(oscIndex), "Set Min Value for Range",  (oscConnection, message)->{
//        String typeTag = message.getTypeTag();
//        if(typeTag.equals(",f")) {
//          double value = message.getFloat(0);
//          ranges.setMin(index, value);
//        }
//      });
//
//      oscController.addLedDouble(ID, oscPaths.getRangeMin(oscIndex), ()->{
//        return ranges.getMax(index);
//      });
//      oscController.registerOscCallback(oscPaths.getRangeMax(oscIndex), "Set Max Value for Range",  (oscConnection, message)->{
//        String typeTag = message.getTypeTag();
//        if(typeTag.equals(",f")) {
//          double value = message.getFloat(0);
//          ranges.setMax(index, value);
//        }
//      });
//
//      //Parameter Display
//      oscController.addLedOscPagedRemoteControl(ID_ADVANCED, oscPaths.getRemote(oscIndex), remoteControls.get(index), "MFT Remote " + i);
//    }
//    oscController.addLedInteger(ID, oscPaths.SELECTED_INDEX, this::getSelectedIndex);
    }
  }
}
