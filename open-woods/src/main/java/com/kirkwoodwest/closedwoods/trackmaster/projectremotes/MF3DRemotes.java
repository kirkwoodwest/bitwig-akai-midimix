package com.kirkwoodwest.closedwoods.trackmaster.projectremotes;

import com.bitwig.extension.api.Color;
import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.HardwareActionBindable;
import com.bitwig.extension.controller.api.RemoteControl;
import com.kirkwoodwest.openwoods.savelist.ColorCollection;
import com.kirkwoodwest.openwoods.savelist.DoubleRangeCollectionSaved;
import com.kirkwoodwest.openwoods.hardware.layer.ControlLayer;
import com.kirkwoodwest.openwoods.remotecontrol.OwRemoteControl;
import com.kirkwoodwest.extensions.hardware.MF3D.ControlGroupMF3D;
import com.kirkwoodwest.extensions.hardware.MF3D.HardwareMF3D;
import com.kirkwoodwest.extensions.hardware.MF3D.LedMFArcadeData;

import java.util.ArrayList;

/**
 * Create Project remotes for 8 remotes.
 */
public class MF3DRemotes {
  public static String ID = "MFTProjectRemotes";
  private final ArrayList<OwRemoteControl> remotes;
  private final ControllerHost host;
  private final ColorCollection colorCollection;
  private final DoubleRangeCollectionSaved mf3dRanges;
  private final OscMF3DMapper oscMF3DMapper;
  private final ControlGroupMF3D subLayerMain;;
  private  ArrayList<Boolean> buttonStates = new ArrayList<>();
  private  ArrayList<Boolean> buttonIsToggle = new ArrayList<>();

  static class RemoteColors {
    public static final LedMFArcadeData LED_ON;
    public static final LedMFArcadeData LED_OFF;
    public static final LedMFArcadeData LED_EMPTY;

    static {
      LED_ON = new LedMFArcadeData(Color.fromRGB(1.0,0.0,0.0));
      LED_ON.setBrightness(1.0);

      LED_OFF = new LedMFArcadeData(Color.fromRGB(1.0,0.0,0.0));
      LED_OFF.setBrightness(0.25);

      LED_EMPTY = new LedMFArcadeData(Color.fromRGB(1.0,0.0,0.0));
      LED_EMPTY.setBrightness(0.0);
    }
  }

  public  MF3DRemotes(ControllerHost host, HardwareMF3D hardwareMF3D, ArrayList<OwRemoteControl> remotes, ColorCollection colorCollection, DoubleRangeCollectionSaved mf3dRanges, OscMF3DMapper oscMF3DMapper){
    this.host = host;

    this.remotes = remotes;
    this.colorCollection = colorCollection;
    this.mf3dRanges = mf3dRanges;
    this.oscMF3DMapper = oscMF3DMapper;

    ControlLayer<HardwareMF3D, ControlGroupMF3D> controlLayer = hardwareMF3D.createControlLayer();
    subLayerMain = controlLayer.createSubLayer();

    int buttonCount = subLayerMain.getArcadeButtonCount();
    for (int i = 0; i < buttonCount; i++) {

      if(i < 8) {
        buttonIsToggle.add(false);
        setupButton(i, remotes.get(i));
      } else {
        buttonIsToggle.add(true);
        setupButton(i, remotes.get(i));
      }
    }

    controlLayer.setSubLayerActive(subLayerMain, true);
    hardwareMF3D.setLayerActive(controlLayer, true);
  }

  private void setupButton(final int i, OwRemoteControl owRemoteControl) {
    RemoteControl remoteControl = owRemoteControl.getRemoteControl();

    //Pressed Action
    Runnable pressed = ()->{
      if( buttonIsToggle.get(i)) {
        if (owRemoteControl.getRemoteControl().value().get() > 0.5) {
          remoteControl.value().setImmediately(mf3dRanges.getMin(i));
        } else {
          remoteControl.value().setImmediately(mf3dRanges.getMax(i));
        }
      } else {
        remoteControl.value().setImmediately(mf3dRanges.getMax(i));
      }
      oscMF3DMapper.setSelectedIndex(i);
    };

    //Released Action
    Runnable released = ()->{
      if(!buttonIsToggle.get(i)) {
        remoteControl.value().setImmediately(mf3dRanges.getMin(i));
      }
    };
    HardwareActionBindable pressedAction = host.createAction(pressed, ()->"Project Remote Press");
    subLayerMain.addArcadeButtonPressedAction(i, pressedAction);

    HardwareActionBindable releasedAction = host.createAction(released, ()->"Project Remote Release");
    subLayerMain.addArcadeButtonReleasedAction(i, releasedAction);

    subLayerMain.addArcadeButtonLed(i, ()->{
      if(owRemoteControl.exists()) {
        if(remoteControl.value().get()>0.5) {
          return RemoteColors.LED_ON;
        } else {
          return RemoteColors.LED_OFF;
        }
      } else {
        return RemoteColors.LED_EMPTY;
      }
    });
  }
}

